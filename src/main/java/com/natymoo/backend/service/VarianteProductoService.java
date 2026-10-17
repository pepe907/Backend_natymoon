package com.natymoo.backend.service;

import com.natymoo.backend.entity.VarianteProducto;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.VarianteProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VarianteProductoService {

    private final VarianteProductoRepository repository;

    public List<VarianteProducto> findAll() {
        return repository.findAll();
    }

    public VarianteProducto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro variante de producto con id: " + id));
    }

    public VarianteProducto create(VarianteProducto entidad) {
        return repository.save(entidad);
    }

    public VarianteProducto update(Long id, VarianteProducto datosActualizados) {
        VarianteProducto existente = findById(id);
        copiarCampos(existente, datosActualizados);
        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro variante de producto con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copia todos los campos del objeto recibido sobre el existente,
     * preservando el id. Al usar reflexion simple via setters de Lombok
     * evitamos repetir manualmente cada campo aqui.
     */
    private void copiarCampos(VarianteProducto existente, VarianteProducto nuevo) {
        org.springframework.beans.BeanUtils.copyProperties(nuevo, existente, "id");
    }
}
