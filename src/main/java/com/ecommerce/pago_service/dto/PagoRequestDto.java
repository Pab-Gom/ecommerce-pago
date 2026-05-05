package com.ecommerce.pago_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PagoRequestDto {

    @NotNull(message = "El id de la orden es obligatorio")
    private Long ordenId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
}