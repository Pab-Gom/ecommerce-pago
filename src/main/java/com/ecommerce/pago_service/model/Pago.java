package com.ecommerce.pago_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ordenId;

    private Long usuarioId;

    private Double monto;

    private String metodoPago; // TARJETA, TRANSFERENCIA, etc

    private String estado; // PENDIENTE, PAGADO, RECHAZADO

    private LocalDateTime fechaPago;
}