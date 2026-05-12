package com.ecommerce.pago_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.pago_service.client.OrdenClient;
import com.ecommerce.pago_service.dto.OrdenResponseDto;
import com.ecommerce.pago_service.dto.PagoRequestDto;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.exception.PagoNoEncontradoException;
import com.ecommerce.pago_service.model.Pago;
import com.ecommerce.pago_service.repository.PagoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private OrdenClient ordenClient;

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

        //**** SIMULAR PAGO EXITOSO
        guardado.setEstado("PAGADO");
        pagoRepository.save(guardado);

        ordenClient.actualizarEstadoOrden(guardado.getOrdenId(), "PAGADO");

        return mapToDTO(guardado);
    }

    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNoEncontradoException("No existe pago con esta id: " + id));
    }

    public List<Pago> obtenerPorOrden(Long ordenId) {
        return pagoRepository.findByOrdenId(ordenId);
    }

    public List<Pago> obtenerPorUsuario(Long usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pago> obtenerPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    public Pago actualizarPago(Long id, Pago nuevoPago) {

        Pago pago = obtenerPorId(id);

        pago.setMetodoPago(nuevoPago.getMetodoPago());
        pago.setMonto(nuevoPago.getMonto());

        return pagoRepository.save(pago);
    }

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

    public void eliminarPorId(Long id) {
        Pago pago = obtenerPorId(id);
        pagoRepository.delete(pago);
    }

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