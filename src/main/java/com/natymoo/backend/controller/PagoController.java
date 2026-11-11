package com.natymoo.backend.controller;

import com.natymoo.backend.dto.PagoRequest;
import com.natymoo.backend.entity.Pago;
import com.natymoo.backend.service.PagoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Compras - Pagos", description = "Crear está abierto a cualquier autenticado; aprobar/rechazar requiere ADMIN o EMPLEADO.")
public class PagoController {

    private final PagoService service;

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoRequest pagoRequest) {
        Pago creado = service.create(pagoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @Valid @RequestBody PagoRequest pagoRequest) {
        return ResponseEntity.ok(service.update(id, pagoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
