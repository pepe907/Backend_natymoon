package com.natymoo.backend.service;

import com.natymoo.backend.dto.ImagenProductoRequest;
import com.natymoo.backend.entity.ImagenProducto;
import com.natymoo.backend.entity.Producto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ImagenProductoRepository;
import com.natymoo.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImagenProductoService {

    private final ImagenProductoRepository repository;
    private final ProductoRepository productoRepository;

    @Transactional (readOnly = true)
    public List<ImagenProducto> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ImagenProducto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro imagen de producto con id: " + id));
    }

    public ImagenProducto create(ImagenProductoRequest imagenProductoRequest) {
        Producto producto = productoRepository.findById(imagenProductoRequest.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        boolean esPrincipal = imagenProductoRequest.getEsPrincipal();

        if (esPrincipal){
            List<ImagenProducto> imagenesExistentes = repository.findByProductoId(producto.getId());
            for (ImagenProducto img: imagenesExistentes){
                img.setEsPrincipal(false);
                repository.save(img);
            }
        }

        ImagenProducto imagen = ImagenProducto.builder()
                .producto(producto)
                .url(imagenProductoRequest.getUrl())
                .orden(imagenProductoRequest.getOrden())
                .esPrincipal(esPrincipal)
                .build();
        return repository.save(imagen);
    }

    public ImagenProducto update(Long id, ImagenProductoRequest imagenProductoRequest) {
        ImagenProducto existente = findById(id);

        if (imagenProductoRequest.getProductoId() != null) {
            Producto producto = productoRepository.findById(imagenProductoRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            existente.setProducto(producto);
        }

        if (imagenProductoRequest.getUrl() != null) {
            existente.setUrl(imagenProductoRequest.getUrl());
        }

        if (imagenProductoRequest.getOrden() != null) {
            existente.setOrden(imagenProductoRequest.getOrden());
        }

        if (imagenProductoRequest.getEsPrincipal() != null) {
            boolean nuevoEsPrincipal = imagenProductoRequest.getEsPrincipal();
            if (nuevoEsPrincipal && !existente.getEsPrincipal()) {
                List<ImagenProducto> imagenesExistentes = repository.findByProductoId(existente.getProducto().getId());
                for (ImagenProducto img : imagenesExistentes) {
                    if (!img.getId().equals(existente.getId())) {
                        img.setEsPrincipal(false);
                        repository.save(img);
                    }
                }
            }
            existente.setEsPrincipal(nuevoEsPrincipal);
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro imagen de producto con id: " + id);
        }
        repository.deleteById(id);
    }
}