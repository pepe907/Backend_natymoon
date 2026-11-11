package com.natymoo.backend.service;

import com.natymoo.backend.dto.ProductoRequest;
import com.natymoo.backend.entity.Categoria;
import com.natymoo.backend.entity.Genero;
import com.natymoo.backend.entity.Producto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CategoriaRepository;
import com.natymoo.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro producto con id: " + id));
    }

    public Producto create(ProductoRequest productoRequest) {

        Categoria categoria = categoriaRepository.findById(productoRequest.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));

        Producto producto = Producto.builder()
                .categoria(categoria)
                .nombre(productoRequest.getNombre())
                .descripcion(productoRequest.getDescripcion())
                .precioBase(productoRequest.getPrecioBase())
                .precioOferta(productoRequest.getPrecioOferta())
                .genero(productoRequest.getGenero())
                .material(productoRequest.getMaterial())
                .activo(productoRequest.getActivo() != null ? productoRequest.getActivo() : true)
                .build();
        return repository.save(producto);
    }

    public Producto update(Long id, ProductoRequest productoRequest) {
        Producto existente = findById(id);

        if (productoRequest.getCategoriaId() != null){
            Categoria categoria = categoriaRepository.findById(productoRequest.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));

            existente.setCategoria(categoria);
        }
        if (productoRequest.getNombre() != null){
            existente.setNombre(productoRequest.getNombre());
        }
        if (productoRequest.getDescripcion() != null){
            existente.setDescripcion(productoRequest.getDescripcion());
        }
        if (productoRequest.getPrecioBase() != null){
            existente.setPrecioBase(productoRequest.getPrecioBase());
        }
        if (productoRequest.getPrecioOferta() != null){
            existente.setPrecioOferta(productoRequest.getPrecioOferta());
        }
        if (productoRequest.getGenero() != null){
            existente.setGenero(productoRequest.getGenero());
        }
        if (productoRequest.getMaterial() != null){
            existente.setMaterial(productoRequest.getMaterial());
        }
        if (productoRequest.getActivo() != null){
            existente.setActivo(productoRequest.getActivo());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro producto con id: " + id);
        }
        repository.deleteById(id);
    }
}
