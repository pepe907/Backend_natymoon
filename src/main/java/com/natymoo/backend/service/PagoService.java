package com.natymoo.backend.service;

import com.natymoo.backend.dto.PagoRequest;
import com.natymoo.backend.entity.EstadoPago;
import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.entity.Pago;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.OrdenCompraRepository;
import com.natymoo.backend.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private final PagoRepository repository;
    private final OrdenCompraRepository ordenCompraRepository;

    @Transactional(readOnly = true)
    public List<Pago> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Pago findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro pago con id: " + id));
    }

    public Pago create(PagoRequest pagoRequest) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(pagoRequest.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("La orden de la compra no encontrada"));

        Pago pago = Pago.builder()
                .metodoPago(pagoRequest.getMetodoPago())
                .estado(pagoRequest.getEstadoPago() != null ? pagoRequest.getEstadoPago(): EstadoPago.PENDIENTE)
                .monto(pagoRequest.getMonto())
                .referenciaTransaccion(pagoRequest.getReferenciaTransaccion())
                .orden(ordenCompra)
                .build();
        return repository.save(pago);
    }

    public Pago update(Long id, PagoRequest pagoRequest) {
        Pago existente = findById(id);
        if (pagoRequest.getOrdenId() != null){
            OrdenCompra ordenCompra = ordenCompraRepository.findById(pagoRequest.getOrdenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Orden de la compra no encontrada"));
            existente.setOrden(ordenCompra);

        }
        if (pagoRequest.getEstadoPago() != null){
            existente.setEstado(pagoRequest.getEstadoPago());
        }
        if (pagoRequest.getMonto() != null){
            existente.setMonto(pagoRequest.getMonto());
        }
        if (pagoRequest.getReferenciaTransaccion() != null){
            existente.setReferenciaTransaccion(pagoRequest.getReferenciaTransaccion());
        }
        if (pagoRequest.getMetodoPago() != null){
            existente.setMetodoPago(pagoRequest.getMetodoPago());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro pago con id: " + id);
        }
        repository.deleteById(id);
    }

}
