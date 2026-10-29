package com.natymoo.backend.service;

import com.natymoo.backend.dto.UsersRequest;
import com.natymoo.backend.entity.Rol;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.RolRepository;
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
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro usuario con id: " + id));
    }

    public Usuario create(UsersRequest usersRequest) {
        Rol rol = rolRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado com id: 1"));

        Usuario users = Usuario.builder()
                .nombre(usersRequest.getNombre())
                .apellido(usersRequest.getApellido())
                .email(usersRequest.getEmail())
                .passwordHash(passwordEncoder.encode(usersRequest.getPassword()))
                .telefono(usersRequest.getTelefono())
                .rol(rol)
                .activo(true)
                .build();
        return usuarioRepository.save(users);
    }

    public Usuario update(Long id, UsersRequest usersRequest) {
        Usuario usuarioExistente = findById(id);

        usuarioExistente.setNombre(usersRequest.getNombre());
        usuarioExistente.setApellido(usersRequest.getApellido());
        usuarioExistente.setEmail(usersRequest.getEmail());
        usuarioExistente.setTelefono(usuarioExistente.getTelefono());

        if (usersRequest.getPassword() != null && !usersRequest.getPassword().isBlank()){
            usuarioExistente.setPasswordHash(passwordEncoder.encode(usersRequest.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro usuario con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}