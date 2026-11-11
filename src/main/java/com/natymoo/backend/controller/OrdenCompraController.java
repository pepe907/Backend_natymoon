package com.natymoo.backend.controller;

import com.natymoo.backend.dto.OrdenCompraRequest;
import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.service.OrdenCompraService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
@Tag(name = "Compras - Órdenes de compra", description = "Crear está abierto a cualquier autenticado (el cliente crea su propia orden); cambiar estado requiere ADMIN o EMPLEADO.")
public class OrdenCompraController {

    private final OrdenCompraService service;

    @GetMapping
    public ResponseEntity<List<OrdenCompra>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrdenCompra> crear(@Valid @RequestBody OrdenCompraRequest ordenCompraRequest) {
        OrdenCompra creado = service.create(ordenCompraRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompra> actualizar(@PathVariable Long id, @Valid @RequestBody OrdenCompraRequest ordenCompraRequest) {
        return ResponseEntity.ok(service.update(id, ordenCompraRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
