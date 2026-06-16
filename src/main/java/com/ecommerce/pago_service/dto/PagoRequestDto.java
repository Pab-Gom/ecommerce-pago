package com.ecommerce.pago_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO de solicitud para crear un pago")
public class PagoRequestDto {

    @NotNull(message = "El id de la orden es obligatorio")
    @Schema(description = "ID de la orden a pagar", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ordenId;

    @NotNull(message = "El id del usuario es obligatorio")
    @Schema(description = "ID del usuario que realiza el pago", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long usuarioId;

    @NotBlank(message = "El método de pago es obligatorio")
    @Schema(description = "Método de pago (TARJETA, TRANSFERENCIA, EFECTIVO, etc.)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String metodoPago;
}