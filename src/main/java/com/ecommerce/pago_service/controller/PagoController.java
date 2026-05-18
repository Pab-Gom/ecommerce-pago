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

    @PostMapping
    public ResponseEntity<PagoResponseDto> crearPago(@Valid @RequestBody PagoRequestDto dto) {
        return ResponseEntity.ok(pagoService.crearPago(dto));
    }

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/mis-pagos")
    public ResponseEntity<List<Pago>> obtenerMisPagos() {
        return ResponseEntity.ok(pagoService.obtenerMisPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<Pago>> obtenerPorOrden(@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.obtenerPorOrden(ordenId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.obtenerPorEstado(estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, pago));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        pagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}