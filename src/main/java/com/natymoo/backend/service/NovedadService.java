package com.natymoo.backend.service;

import com.natymoo.backend.dto.NovedadRequest;
import com.natymoo.backend.entity.Novedad;
import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.NovedadRepository;
import com.natymoo.backend.repository.OrdenCompraRepository;
import com.natymoo.backend.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    @Transactional(readOnly = true)
    public List<Novedad> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Novedad findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la novedad con id: " + id));
    }

    public Novedad create(NovedadRequest novedadRequest) {
        Usuario usuario = usuarioRepository.findById(novedadRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        OrdenCompra orden = null;

        if (novedadRequest.getOrdenId() != null){
            orden = ordenCompraRepository.findById(novedadRequest.getOrdenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
        }

        Novedad novedad = Novedad.builder()
                .usuario(usuario)
                .orden(orden)
                .asunto(novedadRequest.getAsunto())
                .descripcion(novedadRequest.getDescripcion())
                .respuesta(novedadRequest.getRespuesta())
                .build();

        return repository.save(novedad);
    }

    public Novedad update(Long id, NovedadRequest novedadRequest) {
        Novedad existente = findById(id);
        if (novedadRequest.getUsuarioId() != null){
            Usuario usuario = usuarioRepository.findById(novedadRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            existente.setUsuario(usuario);
        }
        OrdenCompra orden = null;
        if (novedadRequest.getOrdenId() != null){
            orden = ordenCompraRepository.findById(novedadRequest.getOrdenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            existente.setOrden(orden);
        }
        if (novedadRequest.getAsunto() != null){
            existente.setAsunto(novedadRequest.getAsunto());
        }
        if (novedadRequest.getDescripcion() != null){
            existente.setDescripcion(novedadRequest.getDescripcion());
        }
        if (novedadRequest.getRespuesta() != null){
            existente.setRespuesta(novedadRequest.getRespuesta());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro la novedad con id: " + id);
        }
        repository.deleteById(id);
    }
}
