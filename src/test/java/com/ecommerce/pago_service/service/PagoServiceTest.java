package com.ecommerce.pago_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ecommerce.pago_service.client.OrdenClient;
import com.ecommerce.pago_service.dto.OrdenResponseDto;
import com.ecommerce.pago_service.dto.PagoRequestDto;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.model.Pago;
import com.ecommerce.pago_service.repository.PagoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private OrdenClient ordenClient;

    @InjectMocks
    private PagoService pagoService;

    @BeforeEach
    void setup() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "admin",
                        1L,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void deberiaCrearPago() {

        PagoRequestDto request = new PagoRequestDto();
        request.setOrdenId(1L);
        request.setUsuarioId(10L);
        request.setMetodoPago("TARJETA");

        OrdenResponseDto orden = new OrdenResponseDto();
        orden.setId(1L);
        orden.setUsuarioId(10L);
        orden.setTotal(50000.0);
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion(LocalDateTime.now());

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(100L);
        pagoGuardado.setOrdenId(1L);
        pagoGuardado.setUsuarioId(10L);
        pagoGuardado.setMonto(50000.0);
        pagoGuardado.setMetodoPago("TARJETA");
        pagoGuardado.setEstado("PAGADO");
        pagoGuardado.setFechaPago(LocalDateTime.now());

        when(ordenClient.obtenerOrdenPorId(1L))
                .thenReturn(orden);

        when(pagoRepository.save(any(Pago.class)))
                .thenReturn(pagoGuardado);

        PagoResponseDto resultado = pagoService.crearPago(request);

        assertNotNull(resultado);
        assertEquals("PAGADO", resultado.getEstado());

        verify(pagoRepository, times(2)).save(any(Pago.class));
        verify(ordenClient).actualizarEstadoOrden(1L, "PAGADO");
    }

    @Test
    void deberiaLanzarErrorSiOrdenNoExiste() {

        PagoRequestDto request = new PagoRequestDto();
        request.setOrdenId(1L);
        request.setUsuarioId(10L);
        request.setMetodoPago("TARJETA");

        when(ordenClient.obtenerOrdenPorId(1L))
                .thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> pagoService.crearPago(request)
        );

        assertEquals("La orden no existe", exception.getMessage());
    }

    @Test
    void deberiaObtenerPagoPorId() {

        Pago pago = new Pago();
        pago.setId(1L);
        pago.setUsuarioId(10L);

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        Pago resultado = pagoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(pagoRepository).findById(1L);
    }

    @Test
    void deberiaActualizarEstadoPago() {

        Pago pago = new Pago();
        pago.setId(1L);
        pago.setEstado("PENDIENTE");

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(pagoRepository.save(any(Pago.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pago resultado = pagoService.actualizarEstado(1L, "PAGADO");

        assertEquals("PAGADO", resultado.getEstado());

        verify(pagoRepository).save(any(Pago.class));
    }
}