package com.natymoo.backend.service;

import com.natymoo.backend.dto.OrdenCompraRequest;
import com.natymoo.backend.entity.Cupon;
import com.natymoo.backend.entity.DireccionUsuario;
import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CuponRepository;
import com.natymoo.backend.repository.DireccionUsuarioRepository;
import com.natymoo.backend.repository.OrdenCompraRepository;
import com.natymoo.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraService {

    private final OrdenCompraRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CuponRepository cuponRepository;
    private final DireccionUsuarioRepository direccionUsuarioRepository;

    @Transactional(readOnly = true)
    public List<OrdenCompra> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public OrdenCompra findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro orden de compra con id: " + id));
    }

    public OrdenCompra create(OrdenCompraRequest ordenCompraRequest) {

        Usuario usuario = usuarioRepository.findById(ordenCompraRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Cupon cupon = null;
        if (ordenCompraRequest.getCuponId() != null){
            cupon = cuponRepository.findById(ordenCompraRequest.getCuponId())
                    .orElseThrow(() -> new ResourceNotFoundException("No cumple los requisitos"));
        }

        DireccionUsuario direccionUsuario = direccionUsuarioRepository.findById(ordenCompraRequest.getDireccionId())
                .orElseThrow(() -> new ResourceNotFoundException("Direccion no valida"));

        OrdenCompra ordenCompra = OrdenCompra.builder()
                .usuario(usuario)
                .cupon(cupon)
                .direccion(direccionUsuario)
                .costoEnvio(ordenCompraRequest.getCostoEnvio())
                .descuento(ordenCompraRequest.getDescuento())
                .total(ordenCompraRequest.getTotal())
                .estado(ordenCompraRequest.getEstado())
                .build();
        return repository.save(ordenCompra);
    }

    public OrdenCompra update(Long id, OrdenCompraRequest ordenCompraRequest) {
        OrdenCompra existente = findById(id);
        if (ordenCompraRequest.getUsuarioId() != null){
            Usuario usuario = usuarioRepository.findById(ordenCompraRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            existente.setUsuario(usuario);
        }
        if (ordenCompraRequest.getCuponId() != null){
            Cupon cupon = cuponRepository.findById(ordenCompraRequest.getCuponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cupon no disponible"));
            existente.setCupon(cupon);
        }
        if (ordenCompraRequest.getDireccionId() != null){
            DireccionUsuario direccionUsuario = direccionUsuarioRepository.findById(ordenCompraRequest.getDireccionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Direccion no valida"));
            existente.setDireccion(direccionUsuario);
        }
        if (ordenCompraRequest.getCostoEnvio() != null){
            existente.setCostoEnvio(ordenCompraRequest.getCostoEnvio());
        }
        if (ordenCompraRequest.getDescuento() != null){
            existente.setDescuento(ordenCompraRequest.getDescuento());
        }
        if (ordenCompraRequest.getTotal() != null){
            existente.setTotal(ordenCompraRequest.getTotal());
        }
        if (ordenCompraRequest.getEstado() != null){
            existente.setEstado(ordenCompraRequest.getEstado());
        }
        if (ordenCompraRequest.getSubtotal() != null) {
            existente.setSubtotal(ordenCompraRequest.getSubtotal());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro orden de compra con id: " + id);
        }
        repository.deleteById(id);
    }

}
