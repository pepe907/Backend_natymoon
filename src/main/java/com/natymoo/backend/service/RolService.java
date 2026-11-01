package com.natymoo.backend.service;

import com.natymoo.backend.dto.RolRequest;
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

    public Rol findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro rol con id: " + id));
    }

    public Rol create(RolRequest rolRequest) {
        Rol rolCreate = Rol.builder()
                .descripcion(rolRequest.getDescripcion())
                .build();
        return repository.save(rolCreate);
    }

    public Rol update(Long id, RolRequest rolRequest) {
        Rol existente = findById(id);

        existente.setDescripcion(rolRequest.getDescripcion());
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro rol con id: " + id);
        }
        repository.deleteById(id);
    }

}