package com.natymoo.backend.service;

import com.natymoo.backend.entity.ImagenProducto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ImagenProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImagenProductoService {

    private final ImagenProductoRepository repository;

    public List<ImagenProducto> findAll() {
        return repository.findAll();
    }

    public ImagenProducto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro imagen de producto con id: " + id));
    }

    public ImagenProducto create(ImagenProducto entidad) {
        return repository.save(entidad);
    }

    public ImagenProducto update(Long id, ImagenProducto datosActualizados) {
        ImagenProducto existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro imagen de producto con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(ImagenProducto existente, ImagenProducto nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id");
    }
}
