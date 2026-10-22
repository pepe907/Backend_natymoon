package com.natymoo.backend.service;

import com.natymoo.backend.entity.DireccionUsuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.DireccionUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionUsuarioService {

    private final DireccionUsuarioRepository repository;

    public List<DireccionUsuario> findAll() {
        return repository.findAll();
    }

    public DireccionUsuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro direccion de usuario con id: " + id));
    }

    public DireccionUsuario create(DireccionUsuario direccionUsuario) {
        return repository.save(direccionUsuario);
    }

    public DireccionUsuario update(Long id, DireccionUsuario direccionUsuario) {
        DireccionUsuario existente = findById(id);

        copiarCampos(existente, direccionUsuario);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro direccion de usuario con id: " + id);
        }
        repository.deleteById(id);
    }

    private void copiarCampos(DireccionUsuario existente, DireccionUsuario nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fechaCreacion");
    }
}
