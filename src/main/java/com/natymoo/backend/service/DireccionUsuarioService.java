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

    public DireccionUsuario create(DireccionUsuario entidad) {
        return repository.save(entidad);
    }

    public DireccionUsuario update(Long id, DireccionUsuario datosActualizados) {
        DireccionUsuario existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro direccion de usuario con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(DireccionUsuario existente, DireccionUsuario nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fechaCreacion");
    }
}
