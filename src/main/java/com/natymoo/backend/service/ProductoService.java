package com.natymoo.backend.service;

import com.natymoo.backend.entity.Producto;
import com.natymoo.backend.exception.ResourceNotFoundException;
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

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Producto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro producto con id: " + id));
    }

    public Producto create(Producto entidad) {
        return repository.save(entidad);
    }

    public Producto update(Long id, Producto datosActualizados) {
        Producto existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro producto con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(Producto existente, Producto nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fechaCreacion");
    }
}
