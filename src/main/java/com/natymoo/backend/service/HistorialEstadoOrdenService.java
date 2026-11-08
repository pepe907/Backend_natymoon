package com.natymoo.backend.service;

import com.natymoo.backend.dto.HistorialEstadoOrdenRequest;
import com.natymoo.backend.entity.EstadoOrden;
import com.natymoo.backend.entity.HistorialEstadoOrden;
import com.natymoo.backend.entity.OrdenCompra;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.HistorialEstadoOrdenRepository;
import com.natymoo.backend.repository.OrdenCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.IllegalFormatCodePointException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HistorialEstadoOrdenService {

    private final HistorialEstadoOrdenRepository repository;
    private final OrdenCompraRepository ordenCompraRepository;

    @Transactional(readOnly = true)
    public List<HistorialEstadoOrden> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public HistorialEstadoOrden findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro historial de estado de orden con id: " + id));
    }

    public HistorialEstadoOrden create(HistorialEstadoOrdenRequest historialEstadoOrdenRequest) {

        OrdenCompra ordenCompra = ordenCompraRepository.findById(historialEstadoOrdenRequest.getOrdenCompraId())
                .orElseThrow(() -> new ResourceNotFoundException("id de orden compra no encontrado"));


        HistorialEstadoOrden historial = HistorialEstadoOrden.builder()
                .orden(ordenCompra)
                .estado(historialEstadoOrdenRequest.getEstadoOrden())
                .comentario(historialEstadoOrdenRequest.getComentario())
                .build();

        return repository.save(historial);
    }

    public HistorialEstadoOrden update(Long id, HistorialEstadoOrdenRequest historialEstadoOrdenRequest) {
        HistorialEstadoOrden existente = findById(id);

        if (historialEstadoOrdenRequest.getOrdenCompraId() != null){
            OrdenCompra orden = ordenCompraRepository.findById(historialEstadoOrdenRequest.getOrdenCompraId())
                    .orElseThrow(() -> new ResourceNotFoundException("id de orden compra no encontrado"));

            existente.setOrden(orden);
        }

        if (historialEstadoOrdenRequest.getEstadoOrden() != null){
            existente.setEstado(historialEstadoOrdenRequest.getEstadoOrden());
        }

        if (historialEstadoOrdenRequest.getComentario() != null){
            existente.setComentario(historialEstadoOrdenRequest.getComentario());
        }
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro historial de estado de orden con id: " + id);
        }
        repository.deleteById(id);
    }

}
