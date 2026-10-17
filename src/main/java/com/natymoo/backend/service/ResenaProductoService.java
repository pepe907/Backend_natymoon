package com.natymoo.backend.service;

import com.natymoo.backend.entity.ResenaProducto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ResenaProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResenaProductoService {

    private final ResenaProductoRepository repository;

    public List<ResenaProducto> findAll() {
        return repository.findAll();
    }

    public ResenaProducto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro resena de producto con id: " + id));
    }

    public ResenaProducto create(ResenaProducto entidad) {
        return repository.save(entidad);
    }

    public ResenaProducto update(Long id, ResenaProducto datosActualizados) {
        ResenaProducto existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro resena de producto con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(ResenaProducto existente, ResenaProducto nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fecha");
    }
}
