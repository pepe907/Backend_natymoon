package com.natymoo.backend.service;

import com.natymoo.backend.dto.ResenaProductoRequest;
import com.natymoo.backend.entity.Producto;
import com.natymoo.backend.entity.ResenaProducto;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ProductoRepository;
import com.natymoo.backend.repository.ResenaProductoRepository;
import com.natymoo.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResenaProductoService {

    private final ResenaProductoRepository repository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<ResenaProducto> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ResenaProducto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro resena de producto con id: " + id));
    }

    public ResenaProducto create(ResenaProductoRequest resenaProductoRequest) {

        boolean yaResenado = repository.existsByProductoIdAndUsuarioId(
                resenaProductoRequest.getProductoId(),
                resenaProductoRequest.getUsuarioId()
        );
        if (yaResenado){
            throw new IllegalStateException("El usuario ya ha dejado una reseña para este producto");
        }

        Producto producto = productoRepository.findById(resenaProductoRequest.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Usuario usuario = usuarioRepository.findById(resenaProductoRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ResenaProducto resena = ResenaProducto.builder()
                .usuario(usuario)
                .producto(producto)
                .calificacion(resenaProductoRequest.getCalificacion())
                .comentario(resenaProductoRequest.getComentario())
                .build();
        return repository.save(resena);
    }

    public ResenaProducto update(Long id, ResenaProductoRequest resenaProductoRequest) {
        ResenaProducto existente = findById(id);

        if (resenaProductoRequest.getUsuarioId() != null){
            Usuario usuario = usuarioRepository.findById(resenaProductoRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            existente.setUsuario(usuario);
        }

        if (resenaProductoRequest.getProductoId() != null){
            Producto producto = productoRepository.findById(resenaProductoRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            existente.setProducto(producto);
        }

        if (resenaProductoRequest.getComentario() != null){
            existente.setComentario(resenaProductoRequest.getComentario());
        }

        if (resenaProductoRequest.getCalificacion() != null){
            existente.setCalificacion(resenaProductoRequest.getCalificacion());
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro resena de producto con id: " + id);
        }
        repository.deleteById(id);
    }

}
