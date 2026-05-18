package com.ecommerce.pago_service.service;
import com.ecommerce.pago_service.client.OrdenClient;
import com.ecommerce.pago_service.dto.OrdenResponseDto;
import com.ecommerce.pago_service.dto.PagoRequestDto;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.exception.PagoNoEncontradoException;
import com.ecommerce.pago_service.model.Pago;
import com.ecommerce.pago_service.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j

public class PagoService{

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private OrdenClient ordenClient;

    // **** METODO QUE TOMA EL ID DEL USUARIO LOGGEADO
    private Long getUsuarioIdFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getCredentials();
    }

    // **** METODO PARA AUTORIZAR SI EL USUARIO INGRESADO TIENE ROL DE ADMIN
    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // **** METODO PARA VERIFICACION DE ORDEN, CREACION Y MAPEO DE PAGO
    public PagoResponseDto crearPago(PagoRequestDto dto){

        log.info("Creando pago para orden {}", dto.getOrdenId());

        OrdenResponseDto orden = ordenClient.obtenerOrdenPorId(dto.getOrdenId());

        if (orden == null) {
            log.warn("Orden no encontrada {}", dto.getOrdenId());
            throw new RuntimeException("La orden no existe");
        }
        if (!orden.getEstado().equals("PENDIENTE")) {
            log.warn("Orden no válida para pago {}", dto.getOrdenId());
            throw new RuntimeException("La orden ya fue pagada o no es válida");
        }
        if (dto.getMetodoPago() == null || dto.getMetodoPago().isEmpty()) {
            throw new RuntimeException("Método de pago obligatorio");
        }

        Pago pago = new Pago();
        pago.setOrdenId(orden.getId());
        pago.setUsuarioId(orden.getUsuarioId());
        pago.setMonto(orden.getTotal());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(LocalDateTime.now());

        Pago guardado = pagoRepository.save(pago);

        log.info("Pago creado con id {}", guardado.getId());

        // **** ACTUALIZACION DE ESTADO A PAGADO EN MICROSERVICIO ORDEN
        guardado.setEstado("PAGADO");

        pagoRepository.save(guardado);
        ordenClient.actualizarEstadoOrden(guardado.getOrdenId(), "PAGADO");

        return mapToDTO(guardado);
    }

    // **** METODO PARA OBTENER TODOS LOS PAGOS (ADMIN)
    public List<Pago> obtenerTodos() {
        if (esAdmin()) {
            return pagoRepository.findAll();
        }
        Long usuarioId = getUsuarioIdFromToken();
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    // **** METODO PARA OBTENER PAGOS DEL USUARIO LOGGEADO POR ID
    public List<Pago> obtenerMisPagos() {
        Long usuarioId = getUsuarioIdFromToken();
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    // **** METODO PARA OBTENER PAGO POR ID
    public Pago obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNoEncontradoException("No existe pago con esta id: " + id));
        if (!esAdmin()) {
            Long usuarioId = getUsuarioIdFromToken();
            if (!pago.getUsuarioId().equals(usuarioId)) {
                throw new PagoNoEncontradoException("No existe pago con esta id: " + id);
            }
        }
        return pago;
    }

    // **** METODO PARA OBTENER ORDEN POR ID
    public List<Pago> obtenerPorOrden(Long ordenId) {
        if (esAdmin()) {
            return pagoRepository.findByOrdenId(ordenId);
        }
        Long usuarioId = getUsuarioIdFromToken();
        return pagoRepository.findByOrdenId(ordenId).stream()
                .filter(p -> p.getUsuarioId().equals(usuarioId))
                .toList();
    }

    // **** METODO PARA OBTENER PAGO POR ID DE USUARIO
    public List<Pago> obtenerPorUsuario(Long usuarioId) {
        if (!esAdmin()) {
            Long miId = getUsuarioIdFromToken();
            if (!miId.equals(usuarioId)) {
                throw new RuntimeException("No existen pagos para el usuario: " + usuarioId);
            }
        }
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    // **** METODO PARA OBTENER PAGO POR ESTADO
    public List<Pago> obtenerPorEstado(String estado) {
        if (esAdmin()) {
            return pagoRepository.findByEstado(estado);
        }
        Long usuarioId = getUsuarioIdFromToken();
        return pagoRepository.findByEstado(estado).stream()
                .filter(p -> p.getUsuarioId().equals(usuarioId))
                .toList();
    }

    // **** METODO PARA ACTUALIZAR PAGO
    public Pago actualizarPago(Long id, Pago nuevoPago) {
        Pago pago = obtenerPorId(id);
        pago.setMetodoPago(nuevoPago.getMetodoPago());
        pago.setMonto(nuevoPago.getMonto());
        return pagoRepository.save(pago);
    }

    // **** METODO PARA OBTENER ESTADO DE PAGO
    public Pago actualizarEstado(Long id, String estado) {
        Pago pago = obtenerPorId(id);
        if (!estado.equals("PENDIENTE") &&
            !estado.equals("PAGADO") &&
            !estado.equals("RECHAZADO")) {
            throw new RuntimeException("Estado inválido");
        }
        pago.setEstado(estado);
        return pagoRepository.save(pago);
    }

    // **** METODO PARA ELIMINAR PAGO POR ID
    public void eliminarPorId(Long id) {
        Pago pago = obtenerPorId(id);
        pagoRepository.delete(pago);
    }
    
    // **** MAPEO DE PAGO A DTO PARA RESPONSE
    private PagoResponseDto mapToDTO(Pago pago) {
        PagoResponseDto dto = new PagoResponseDto();
        dto.setId(pago.getId());
        dto.setOrdenId(pago.getOrdenId());
        dto.setUsuarioId(pago.getUsuarioId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}