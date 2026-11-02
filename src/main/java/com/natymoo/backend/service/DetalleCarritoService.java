package com.natymoo.backend.service;

import com.natymoo.backend.dto.DetalleCarritoRequest;
import com.natymoo.backend.entity.CarritoCompra;
import com.natymoo.backend.entity.DetalleCarrito;
import com.natymoo.backend.entity.VarianteProducto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CarritoCompraRepository;
import com.natymoo.backend.repository.DetalleCarritoRepository;
import com.natymoo.backend.repository.VarianteProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleCarritoService {

    private final DetalleCarritoRepository repository;
    private final CarritoCompraRepository carritoCompraRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    public List<DetalleCarrito> findAll() {
        return repository.findAll();
    }

    public DetalleCarrito findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro detalle de carrito con id: " + id));
    }

    public DetalleCarrito create(DetalleCarritoRequest detalleCarritoRequest) {

        CarritoCompra carritoCompra = carritoCompraRepository.findById(detalleCarritoRequest.getCarritoCompraId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el carrito de compra"));

        VarianteProducto varianteProducto = varianteProductoRepository.findById(detalleCarritoRequest.getVarianteProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la variante del producto"));

        DetalleCarrito detalleCarrito = DetalleCarrito.builder()
                .carrito(carritoCompra)
                .variante(varianteProducto)
                .cantidad(detalleCarritoRequest.getCantidad())
                .precioUnitario(detalleCarritoRequest.getPrecioUnitario())
                .build();

        return repository.save(detalleCarrito);
    }

    public DetalleCarrito update(Long id, DetalleCarritoRequest detalleCarritoRequest) {
        DetalleCarrito existente = findById(id);

        if (detalleCarritoRequest.getCarritoCompraId() != null){
            CarritoCompra carritoCompra = carritoCompraRepository.findById(detalleCarritoRequest.getCarritoCompraId())
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontro el carrito de compra"));
            existente.setCarrito(carritoCompra);
        }

        if (detalleCarritoRequest.getVarianteProductoId() != null){
            VarianteProducto varianteProducto = varianteProductoRepository.findById(detalleCarritoRequest.getVarianteProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontro la variante del producto"));
            existente.setVariante(varianteProducto);
        }

        existente.setPrecioUnitario(detalleCarritoRequest.getPrecioUnitario());
        existente.setCantidad(detalleCarritoRequest.getCantidad());

        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro detalle de carrito con id: " + id);
        }
        repository.deleteById(id);
    }
}
