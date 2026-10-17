package com.natymoo.backend.service;

import com.natymoo.backend.entity.ListaDeseos;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ListaDeseosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ListaDeseosService {

    private final ListaDeseosRepository repository;

    public List<ListaDeseos> findAll() {
        return repository.findAll();
    }

    public ListaDeseos findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro lista de deseos con id: " + id));
    }

    public ListaDeseos create(ListaDeseos entidad) {
        return repository.save(entidad);
    }

    public ListaDeseos update(Long id, ListaDeseos datosActualizados) {
        ListaDeseos existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro lista de deseos con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(ListaDeseos existente, ListaDeseos nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fechaAgregado");
    }
}
