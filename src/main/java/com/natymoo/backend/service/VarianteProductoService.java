package com.natymoo.backend.service;

import com.natymoo.backend.dto.VarianteProductoRequest;
import com.natymoo.backend.entity.Producto;
import com.natymoo.backend.entity.VarianteProducto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ProductoRepository;
import com.natymoo.backend.repository.VarianteProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VarianteProductoService {

    private final VarianteProductoRepository repository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<VarianteProducto> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public VarianteProducto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro variante de producto con id: " + id));
    }

    public VarianteProducto create(VarianteProductoRequest varianteProductoRequest) {

        Producto producto = productoRepository.findById(varianteProductoRequest.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encotrado"));

        VarianteProducto varianteProducto = VarianteProducto.builder()
                .producto(producto)
                .sku(varianteProductoRequest.getSku())
                .talla(varianteProductoRequest.getTalla())
                .color(varianteProductoRequest.getColor())
                .stock(varianteProductoRequest.getStock() != null ? varianteProductoRequest.getStock() : 0)
                .precioAdicional(varianteProductoRequest.getPrecioAdicional() != null ? varianteProductoRequest.getPrecioAdicional() : BigDecimal.ZERO)
                .activo(varianteProductoRequest.getActivo() != null ? varianteProductoRequest.getActivo() : true)
                .build();

        return repository.save(varianteProducto);
    }

    public VarianteProducto update(Long id, VarianteProductoRequest varianteProductoRequest) {
        VarianteProducto existente = findById(id);

        if (varianteProductoRequest.getProductoId() != null){
            Producto producto = productoRepository.findById(varianteProductoRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            existente.setProducto(producto);
        }
        if (varianteProductoRequest.getSku() != null){
            existente.setSku(varianteProductoRequest.getSku());
        }
        if (varianteProductoRequest.getTalla() != null){
            existente.setTalla(varianteProductoRequest.getTalla());
        }
        if (varianteProductoRequest.getColor() != null){
            existente.setColor(varianteProductoRequest.getColor());
        }
        if (varianteProductoRequest.getStock() != null){
            existente.setStock(varianteProductoRequest.getStock());
        }
        if (varianteProductoRequest.getPrecioAdicional() != null){
            existente.setPrecioAdicional(varianteProductoRequest.getPrecioAdicional());
        }
        if (varianteProductoRequest.getActivo() != null){
            existente.setActivo(varianteProductoRequest.getActivo());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro variante de producto con id: " + id);
        }
        repository.deleteById(id);
    }

}
