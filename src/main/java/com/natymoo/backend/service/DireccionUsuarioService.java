package com.natymoo.backend.service;

import com.natymoo.backend.dto.DireccionUsuarioRequest;
import com.natymoo.backend.entity.DireccionUsuario;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.DireccionUsuarioRepository;
import com.natymoo.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionUsuarioService {

    private final DireccionUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;

    public List<DireccionUsuario> findAll() {
        return repository.findAll();
    }

    public DireccionUsuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro direccion de usuario con id: " + id));
    }

    public DireccionUsuario create(DireccionUsuarioRequest direccionUsuarioRequest) {
        Usuario usuario = usuarioRepository.findById(direccionUsuarioRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("USuario no encontrado con id:" + direccionUsuarioRequest.getUsuarioId()));

        DireccionUsuario direccionUsuario = DireccionUsuario.builder()
                .usuario(usuario)
                .alias(direccionUsuarioRequest.getAlias())
                .destinatario(direccionUsuarioRequest.getDestinatario())
                .telefonoContacto(direccionUsuarioRequest.getTelefonoContacto())
                .calle(direccionUsuarioRequest.getCalle())
                .ciudad(direccionUsuarioRequest.getCiudad())
                .departamento(direccionUsuarioRequest.getDepartamento())
                .codigoPostal(direccionUsuarioRequest.getCodigoPostal())
                .pais(direccionUsuarioRequest.getPais())
                .esPrincipal(direccionUsuarioRequest.getEsPrincipal())
                .build();
        return repository.save(direccionUsuario);
    }

    public DireccionUsuario update(Long id, DireccionUsuarioRequest direccionUsuarioRequest) {
        DireccionUsuario existente = findById(id);

        if (direccionUsuarioRequest.getUsuarioId() != null && !direccionUsuarioRequest.getUsuarioId().equals(existente.getUsuario().getId())){
            Usuario nuevoUsuario = usuarioRepository.findById(direccionUsuarioRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id:" + direccionUsuarioRequest.getUsuarioId()));
            existente.setUsuario(nuevoUsuario);
        }
        existente.setAlias(direccionUsuarioRequest.getAlias());
        existente.setDestinatario(direccionUsuarioRequest.getDestinatario());
        existente.setTelefonoContacto(direccionUsuarioRequest.getTelefonoContacto());
        existente.setCalle(direccionUsuarioRequest.getCalle());
        existente.setNumero(direccionUsuarioRequest.getNumero());
        existente.setCiudad(direccionUsuarioRequest.getCiudad());
        existente.setDepartamento(direccionUsuarioRequest.getDepartamento());
        existente.setCodigoPostal(direccionUsuarioRequest.getCodigoPostal());

        if (direccionUsuarioRequest.getPais() != null){
            existente.setPais(direccionUsuarioRequest.getPais());
        }
        if (direccionUsuarioRequest.getEsPrincipal() != null){
            existente.setEsPrincipal(direccionUsuarioRequest.getEsPrincipal());
        }

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
