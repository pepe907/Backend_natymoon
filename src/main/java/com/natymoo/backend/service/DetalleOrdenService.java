package com.natymoo.backend.service;

import com.natymoo.backend.entity.DetalleOrden;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.DetalleOrdenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleOrdenService {

    private final DetalleOrdenRepository repository;

    public List<DetalleOrden> findAll() {
        return repository.findAll();
    }

    public DetalleOrden findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro detalle de orden con id: " + id));
    }

    public DetalleOrden create(DetalleOrden entidad) {
        return repository.save(entidad);
    }

    public DetalleOrden update(Long id, DetalleOrden datosActualizados) {
        DetalleOrden existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro detalle de orden con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(DetalleOrden existente, DetalleOrden nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id");
    }
}
