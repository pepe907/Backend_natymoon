package com.natymoo.backend.service;

import com.natymoo.backend.entity.CarritoCompra;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CarritoCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoCompraService {

    private final CarritoCompraRepository repository;

    public List<CarritoCompra> findAll() {
        return repository.findAll();
    }

    public CarritoCompra findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro carrito de compra con id: " + id));
    }

    public CarritoCompra create(CarritoCompra entidad) {
        return repository.save(entidad);
    }

    public CarritoCompra update(Long id, CarritoCompra datosActualizados) {
        CarritoCompra existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro carrito de compra con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(CarritoCompra existente, CarritoCompra nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id", "fechaCreacion");
    }
}
