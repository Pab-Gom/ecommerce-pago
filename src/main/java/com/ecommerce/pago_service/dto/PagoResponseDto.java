package com.ecommerce.pago_service.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de respuesta con los datos de un pago creado")
public class PagoResponseDto {

    @Schema(description = "ID único del pago")
    private Long id;

    @Schema(description = "ID de la orden asociada al pago")
    private Long ordenId;

    @Schema(description = "ID del usuario que realizó el pago")
    private Long usuarioId;

    @Schema(description = "Monto del pago")
    private Double monto;

    @Schema(description = "Método de pago utilizado")
    private String metodoPago;

    @Schema(description = "Estado del pago (PENDIENTE, PAGADO, RECHAZADO)")
    private String estado;

    @Schema(description = "Fecha y hora del pago")
    private LocalDateTime fechaPago;
}
