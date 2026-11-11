package com.natymoo.backend.controller;

import com.natymoo.backend.dto.ListaDeseosRequest;
import com.natymoo.backend.entity.ListaDeseos;
import com.natymoo.backend.service.ListaDeseosService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/lista-deseos")
@RequiredArgsConstructor
@Tag(name = "Carrito - Lista de deseos", description = "Favoritos del usuario.")
public class ListaDeseosController {

    private final ListaDeseosService service;

    @GetMapping
    public ResponseEntity<List<ListaDeseos>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaDeseos> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ListaDeseos> crear(@Valid @RequestBody ListaDeseosRequest listaDeseosRequest) {
        ListaDeseos creado = service.create(listaDeseosRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaDeseos> actualizar(@PathVariable Long id, @Valid @RequestBody ListaDeseosRequest listaDeseosRequest) {
        return ResponseEntity.ok(service.update(id, listaDeseosRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
