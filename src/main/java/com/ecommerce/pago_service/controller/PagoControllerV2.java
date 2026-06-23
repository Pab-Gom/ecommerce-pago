package com.ecommerce.pago_service.controller;

import com.ecommerce.pago_service.assembler.PagoModelAssembler;
import com.ecommerce.pago_service.dto.PagoRequestDto;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.model.Pago;
import com.ecommerce.pago_service.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/pagos")
@Tag(name = "Pagos V2", description = "API de gestión de pagos con HATEOAS")
public class PagoControllerV2 {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoModelAssembler assembler;

    @PostMapping
    @Operation(summary = "Crear un nuevo pago (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o la orden no existe/ya fue pagada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<EntityModel<PagoResponseDto>> crearPago(@Valid @RequestBody PagoRequestDto dto) {
        PagoResponseDto dtoResponse = pagoService.crearPago(dto);
        Pago pago = pagoService.obtenerPorId(dtoResponse.getId());
        return ResponseEntity.ok(assembler.toModel(pago));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pagos (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDto>>> obtenerTodos() {
        List<Pago> pagos = pagoService.obtenerTodos();
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }

    @GetMapping("/mis-pagos")
    @Operation(summary = "Obtener mis pagos (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos del usuario obtenidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDto>>> obtenerMisPagos() {
        List<Pago> pagos = pagoService.obtenerMisPagos();
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponseDto>> obtenerPorId(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(pago));
    }

    @GetMapping("/orden/{ordenId}")
    @Operation(summary = "Obtener pagos por ID de orden (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos de la orden encontrados"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDto>>> obtenerPorOrden(@PathVariable Long ordenId) {
        List<Pago> pagos = pagoService.obtenerPorOrden(ordenId);
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pagos por ID de usuario (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos del usuario encontrados"),
        @ApiResponse(responseCode = "400", description = "No existen pagos para el usuario"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDto>>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<Pago> pagos = pagoService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pagos por estado (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos filtrados por estado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDto>>> obtenerPorEstado(@PathVariable String estado) {
        List<Pago> pagos = pagoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago completo (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponseDto>> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        Pago actualizado = pagoService.actualizarPago(id, pago);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de un pago (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponseDto>> actualizarEstado(@PathVariable Long id,
                                                                          @RequestParam String estado) {
        Pago actualizado = pagoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago por ID (V2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        pagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}