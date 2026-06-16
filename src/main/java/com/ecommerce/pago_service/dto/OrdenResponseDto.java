package com.ecommerce.pago_service.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la respuesta del microservicio de órdenes (orden-service)")
public class OrdenResponseDto {

    @Schema(description = "ID de la orden")
    private Long id;

    @Schema(description = "ID del usuario propietario de la orden")
    private Long usuarioId;

    @Schema(description = "Monto total de la orden")
    private Double total;

    @Schema(description = "Estado de la orden (PENDIENTE, PAGADO, ENVIADO)")
    private String estado;

    @Schema(description = "Fecha de creación de la orden")
    private LocalDateTime fechaCreacion;
}