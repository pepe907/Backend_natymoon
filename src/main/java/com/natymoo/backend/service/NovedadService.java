package com.natymoo.backend.service;

import com.natymoo.backend.entity.Novedad;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.NovedadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NovedadService {

    private final NovedadRepository repository;

    public List<Novedad> findAll() {
        return repository.findAll();
    }

    public Novedad findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la novedad con id: " + id));
    }

    public Novedad create(Novedad novedad) {
        return repository.save(novedad);
    }

    public Novedad update(Long id, Novedad datosActualizados) {
        Novedad existente = findById(id);
        BeanUtils.copyProperties(datosActualizados, existente, "id", "fechaCreacion");
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro la novedad con id: " + id);
        }
        repository.deleteById(id);
    }
}
