package com.ecommerce.pago_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PagoResponseDto {

    private Long id;
    private Long ordenId;
    private Long usuarioId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
}