package com.natymoo.backend.service;

import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro usuario con id: " + id));
    }

    public Usuario create(Usuario entidad) {
        String constrasena = passwordEncoder.encode(entidad.getPassword());
        entidad.setPasswordHash(constrasena);
        return usuarioRepository.save(entidad);
    }

    public Usuario update(Long id, Usuario users) {
        Usuario usuarioExistente = findById(id);

        usuarioExistente.setNombre(users.getNombre());
        usuarioExistente.setEmail(users.getEmail());
        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro usuario con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}