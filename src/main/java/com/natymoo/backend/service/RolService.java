package com.natymoo.backend.service;

import com.natymoo.backend.entity.Rol;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RolService {

    private final RolRepository repository;

    public List<Rol> findAll() {
        return repository.findAll();
    }

    public Rol findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro rol con id: " + id));
    }

    public Rol create(Rol entidad) {
        return repository.save(entidad);
    }

    public Rol update(Integer id, Rol datosActualizados) {
        Rol existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro rol con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(Rol existente, Rol nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id");
    }
}
