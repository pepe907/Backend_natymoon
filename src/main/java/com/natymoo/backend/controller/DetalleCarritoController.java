package com.natymoo.backend.controller;

import com.natymoo.backend.dto.DetalleCarritoRequest;
import com.natymoo.backend.entity.DetalleCarrito;
import com.natymoo.backend.service.DetalleCarritoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/detalle-carrito")
@RequiredArgsConstructor
@Tag(name = "Carrito - Detalle de carrito", description = "El precioUnitario queda 'congelado' al momento de agregar.")
public class DetalleCarritoController {

    private final DetalleCarritoService service;

    @GetMapping
    public ResponseEntity<List<DetalleCarrito>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCarrito> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DetalleCarrito> crear(@Valid @RequestBody DetalleCarritoRequest detalleCarritoRequest) {
        DetalleCarrito creado = service.create(detalleCarritoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleCarrito> actualizar(@PathVariable Long id, @Valid @RequestBody DetalleCarritoRequest detalleCarritoRequest) {
        return ResponseEntity.ok(service.update(id, detalleCarritoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
