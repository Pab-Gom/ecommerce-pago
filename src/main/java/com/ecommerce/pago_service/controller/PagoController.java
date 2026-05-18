package com.ecommerce.pago_service.controller;
import com.ecommerce.pago_service.dto.PagoRequestDto;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.model.Pago;
import com.ecommerce.pago_service.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pagos")

public class PagoController{

    @Autowired
    private PagoService pagoService;

    // **** CREACION DE PAGO
    @PostMapping
    public ResponseEntity<PagoResponseDto> crearPago(@Valid @RequestBody PagoRequestDto dto) {
        return ResponseEntity.ok(pagoService.crearPago(dto));
    }

    // **** OBTIENE UNA LISTA DE TODOS LOS PAGOS (ADMIN)
    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    // **** OBTIENE LOS PAGOS REALIZADOS POR LA ID DEL USUARIO LOGGEADO
    @GetMapping("/mis-pagos")
    public ResponseEntity<List<Pago>> obtenerMisPagos() {
        return ResponseEntity.ok(pagoService.obtenerMisPagos());
    }

    // **** OBTIENE PAGO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    // **** OBTIENE PAGOS POR ID DE LA ORDEN 
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<Pago>> obtenerPorOrden(@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.obtenerPorOrden(ordenId));
    }

    // **** OBTIENE PAGOS POR ID DEL USUARIO
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }

    // **** OBTIENE PAGO POR EL ESTADO
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.obtenerPorEstado(estado));
    }

    // **** OBTIENE PAGO POR ID
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, pago));
    }

    // **** ACTUALIZA EL ESTADO DEL PAGO
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }
    
    // **** ELIMINA EL PAGO POR ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        pagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}