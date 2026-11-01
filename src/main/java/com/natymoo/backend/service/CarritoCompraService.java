package com.natymoo.backend.service;

import com.natymoo.backend.dto.CarritoCompraRequest;
import com.natymoo.backend.entity.CarritoCompra;
import com.natymoo.backend.entity.EstadoCarrito;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CarritoCompraRepository;
import com.natymoo.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoCompraService {

    private final CarritoCompraRepository repository;
    private final UsuarioRepository usuarioRepository;

    public List<CarritoCompra> findAll() {
        return repository.findAll();
    }

    public CarritoCompra findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro carrito de compra con id: " + id));
    }

    public CarritoCompra create(CarritoCompraRequest carritoCompraRequest) {

        Usuario usuario = usuarioRepository.findById(carritoCompraRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        CarritoCompra carrito = CarritoCompra.builder()
                .usuario(usuario)
                .estado(EstadoCarrito.ACTIVO)
                .build();
        return repository.save(carrito);
    }

    public CarritoCompra update(Long id, CarritoCompraRequest carritoCompraRequest) {
        CarritoCompra existente = findById(id);

        if (carritoCompraRequest.getUsuarioId() != null){
            Usuario usuario = usuarioRepository.findById(carritoCompraRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            existente.setUsuario(usuario);
        }

        if (carritoCompraRequest.getEstado() != null){
            existente.setEstado(EstadoCarrito.valueOf(carritoCompraRequest.getEstado()));
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro carrito de compra con id: " + id);
        }
        repository.deleteById(id);
    }
}
