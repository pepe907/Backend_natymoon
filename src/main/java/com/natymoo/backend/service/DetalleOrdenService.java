package com.natymoo.backend.service;

import com.natymoo.backend.dto.DetalleCarritoRequest;
import com.natymoo.backend.dto.DetalleOrdenRequest;
import com.natymoo.backend.dto.DetalleOrdenResponse;
import com.natymoo.backend.entity.DetalleOrden;
import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.entity.VarianteProducto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.DetalleOrdenRepository;
import com.natymoo.backend.repository.OrdenCompraRepository;
import com.natymoo.backend.repository.VarianteProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleOrdenService {

    private final DetalleOrdenRepository repository;
    private final VarianteProductoRepository varianteProductoRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    public List<DetalleOrden> findAll() {
        return repository.findAll();
    }

    public DetalleOrden findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro detalle de orden con id: " + id));
    }

    public DetalleOrden create(DetalleOrdenRequest detalleOrdenRequest) {
        VarianteProducto variante = varianteProductoRepository.findById(detalleOrdenRequest.getVarianteProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("La Variante del producto no disponible"));

        OrdenCompra orden = ordenCompraRepository.findById(detalleOrdenRequest.getOrdenCompraId())
                .orElseThrow(() -> new ResourceNotFoundException("La orden del producto no disponible"));


        DetalleOrden detalleOrden = DetalleOrden.builder()
                .variante(variante)
                .orden(orden)
                .cantidad(detalleOrdenRequest.getCantidad())
                .precioUnitario(detalleOrdenRequest.getPrecioUnitario())
                .build();
        return repository.save(detalleOrden);
    }

    public DetalleOrden update(Long id, DetalleOrdenRequest detalleOrdenRequest) {
        DetalleOrden existente = findById(id);

        if (detalleOrdenRequest.getVarianteProductoId() != null){
            VarianteProducto variante = varianteProductoRepository.findById(detalleOrdenRequest.getVarianteProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("La Variante del producto no disponible"));
            existente.setVariante(variante);
        }

        if (detalleOrdenRequest.getOrdenCompraId() != null){
            OrdenCompra orden = ordenCompraRepository.findById(detalleOrdenRequest.getOrdenCompraId())
                    .orElseThrow(() -> new ResourceNotFoundException("La orden del producto no encontrada"));
            existente.setOrden(orden);
        }

        if (detalleOrdenRequest.getCantidad() != null){
            existente.setCantidad(detalleOrdenRequest.getCantidad());
        }

        if (detalleOrdenRequest.getPrecioUnitario() != null){
            existente.setPrecioUnitario(detalleOrdenRequest.getPrecioUnitario());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro detalle de orden con id: " + id);
        }
        repository.deleteById(id);
    }

}
