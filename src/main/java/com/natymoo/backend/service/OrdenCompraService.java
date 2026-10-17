package com.natymoo.backend.service;

import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.OrdenCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraService {

    private final OrdenCompraRepository repository;

    public List<OrdenCompra> findAll() {
        return repository.findAll();
    }

    public OrdenCompra findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro orden de compra con id: " + id));
    }

    public OrdenCompra create(OrdenCompra entidad) {
        return repository.save(entidad);
    }

    public OrdenCompra update(Long id, OrdenCompra datosActualizados) {
        OrdenCompra existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro orden de compra con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(OrdenCompra existente, OrdenCompra nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fechaCreacion");
    }
}
