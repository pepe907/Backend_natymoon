package com.natymoo.backend.service;

import com.natymoo.backend.dto.ListaDeseosRequest;
import com.natymoo.backend.dto.ListaDeseosResponse;
import com.natymoo.backend.entity.ListaDeseos;
import com.natymoo.backend.entity.Producto;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.ListaDeseosRepository;
import com.natymoo.backend.repository.ProductoRepository;
import com.natymoo.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ListaDeseosService {

    private final ListaDeseosRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ListaDeseos> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ListaDeseos findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro lista de deseos con id: " + id));
    }

    public ListaDeseos create(ListaDeseosRequest listaDeseosRequest) {

        Usuario usuario = usuarioRepository.findById(listaDeseosRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));


        Producto producto = productoRepository.findById(listaDeseosRequest.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));


        ListaDeseos lista = ListaDeseos.builder()
                .usuario(usuario)
                .producto(producto)
                .build();

        return repository.save(lista);
    }

    public ListaDeseos update(Long id, ListaDeseosRequest listaDeseosRequest) {
        ListaDeseos existente = findById(id);

        if (listaDeseosRequest.getUsuarioId() != null){
            Usuario usuario = usuarioRepository.findById(listaDeseosRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            existente.setUsuario(usuario);
        }

        if (listaDeseosRequest.getProductoId() != null){
            Producto producto = productoRepository.findById(listaDeseosRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            existente.setProducto(producto);
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro lista de deseos con id: " + id);
        }
        repository.deleteById(id);
    }

}
