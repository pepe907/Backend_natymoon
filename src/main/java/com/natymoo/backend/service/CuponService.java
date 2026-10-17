package com.natymoo.backend.service;

import com.natymoo.backend.entity.Cupon;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CuponService {

    private final CuponRepository repository;

    public List<Cupon> findAll() {
        return repository.findAll();
    }

    public Cupon findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro cupon con id: " + id));
    }

    public Cupon create(Cupon entidad) {
        return repository.save(entidad);
    }

    public Cupon update(Integer id, Cupon datosActualizados) {
        Cupon existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro cupon con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(Cupon existente, Cupon nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id");
    }
}
