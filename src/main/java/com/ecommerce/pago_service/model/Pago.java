package com.ecommerce.pago_service.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un pago realizado")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pago")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID de la orden asociada al pago")
    private Long ordenId;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que realizó el pago")
    private Long usuarioId;

    @Column(nullable = false)
    @Schema(description = "Monto del pago")
    private Double monto;

    @Column(nullable = false, length = 30)
    @Schema(description = "Método de pago (TARJETA, TRANSFERENCIA, EFECTIVO)")
    private String metodoPago;

    @Column(nullable = false, length = 20)
    @Schema(description = "Estado del pago (PENDIENTE, PAGADO, RECHAZADO)")
    private String estado;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora del pago")
    private LocalDateTime fechaPago;
}