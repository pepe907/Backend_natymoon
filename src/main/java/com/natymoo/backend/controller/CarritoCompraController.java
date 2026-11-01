package com.natymoo.backend.controller;

import com.natymoo.backend.dto.CarritoCompraRequest;
import com.natymoo.backend.entity.CarritoCompra;
import com.natymoo.backend.service.CarritoCompraService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/carritos-compra")
@RequiredArgsConstructor
@Tag(name = "Carrito - Carritos de compra", description = "Requiere estar autenticado.")
public class CarritoCompraController {

    private final CarritoCompraService service;

    @GetMapping
    public ResponseEntity<List<CarritoCompra>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoCompra> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CarritoCompra> crear(@Valid @RequestBody CarritoCompraRequest carritoCompraRequest) {
        CarritoCompra creado = service.create(carritoCompraRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoCompra> actualizar(@PathVariable Long id, @Valid @RequestBody CarritoCompraRequest carritoCompraRequest) {
        return ResponseEntity.ok(service.update(id, carritoCompraRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
