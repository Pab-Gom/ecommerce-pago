package com.ecommerce.pago_service.controller;
import com.ecommerce.pago_service.dto.PagoRequestDto;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.model.Pago;
import com.ecommerce.pago_service.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "API de gestión de pagos")
public class PagoController{

    @Autowired
    private PagoService pagoService;

    // **** CREACION DE PAGO
    @PostMapping
    @Operation(summary = "Crear un nuevo pago",
               description = "Crea un pago asociado a una orden. " +
                             "Valida que la orden exista y esté en estado PENDIENTE, " +
                             "luego cambia el estado del pago a PAGADO y actualiza la orden.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago creado exitosamente",
                     content = @Content(schema = @Schema(implementation = PagoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o la orden no existe/ya fue pagada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<PagoResponseDto> crearPago(@Valid @RequestBody PagoRequestDto dto) {
        return ResponseEntity.ok(pagoService.crearPago(dto));
    }

    // **** OBTIENE UNA LISTA DE TODOS LOS PAGOS (ADMIN)
    @GetMapping
    @Operation(summary = "Obtener todos los pagos",
               description = "Obtiene la lista completa de pagos. " +
                             "Los ADMIN ven todos; los USUARIO solo ven sus propios pagos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol ADMIN")
    })
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    // **** OBTIENE LOS PAGOS REALIZADOS POR LA ID DEL USUARIO LOGGEADO
    @GetMapping("/mis-pagos")
    @Operation(summary = "Obtener mis pagos",
               description = "Obtiene los pagos del usuario autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos del usuario obtenidos",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<Pago>> obtenerMisPagos() {
        return ResponseEntity.ok(pagoService.obtenerMisPagos());
    }

    // **** OBTIENE PAGO POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID",
               description = "Obtiene un pago específico por su ID. " +
                             "Si no es ADMIN, solo puede ver sus propios pagos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Pago> obtenerPorId(@Parameter(description = "ID del pago", required = true)@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    // **** OBTIENE PAGOS POR ID DE LA ORDEN 
    @GetMapping("/orden/{ordenId}")
    @Operation(summary = "Obtener pagos por ID de orden",
               description = "Obtiene todos los pagos asociados a una orden específica. " +
                             "Si no es ADMIN, solo ve los pagos que le pertenecen.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos de la orden encontrados",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<Pago>> obtenerPorOrden(@Parameter(description = "ID de la orden", required = true)@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.obtenerPorOrden(ordenId));
    }

    // **** OBTIENE PAGOS POR ID DEL USUARIO
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pagos por ID de usuario",
               description = "Obtiene todos los pagos de un usuario específico. " +
                             "Si no es ADMIN, solo puede consultar sus propios pagos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos del usuario encontrados",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "400", description = "No existen pagos para el usuario"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<Pago>> obtenerPorUsuario(@Parameter(description = "ID del usuario", required = true)@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }

    // **** OBTIENE PAGO POR EL ESTADO
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pagos por estado",
               description = "Filtra pagos por estado (PENDIENTE, PAGADO, RECHAZADO). " +
                             "ADMIN ve todos; USUARIO solo los suyos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos filtrados por estado",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<Pago>> obtenerPorEstado(@Parameter(description = "Estado del pago (PENDIENTE, PAGADO, RECHAZADO)", required = true)@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.obtenerPorEstado(estado));
    }

    // **** OBTIENE PAGO POR ID
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago completo",
               description = "Actualiza todos los campos editables de un pago. " +
                             "Solo accesible para ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago actualizado",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Pago> actualizarPago(@Parameter(description = "ID del pago", required = true)@PathVariable Long id, @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, pago));
    }

    // **** ACTUALIZA EL ESTADO DEL PAGO
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de un pago",
               description = "Actualiza solo el estado de un pago. " +
                             "Valores válidos: PENDIENTE, PAGADO, RECHAZADO. " +
                             "Solo accesible para ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado",
                     content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Pago> actualizarEstado(@Parameter(description = "ID del pago", required = true)@PathVariable Long id,
        @Parameter(description = "Nuevo estado (PENDIENTE, PAGADO, RECHAZADO)",required = true)@RequestParam String estado) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }
    
    // **** ELIMINA EL PAGO POR ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago por ID",
               description = "Elimina un pago específico. Solo accesible para ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPorId(@Parameter(description = "ID del pago a eliminar", required = true)@PathVariable Long id) {
        pagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}