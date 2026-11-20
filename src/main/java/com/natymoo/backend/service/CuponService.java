package com.natymoo.backend.service;

import com.natymoo.backend.dto.CuponRequest;
import com.natymoo.backend.entity.Cupon;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CuponService {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private final CuponRepository repository;

    @Transactional(readOnly = true)
    public List<Cupon> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Cupon findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro cupon con id: " + id));
    }

    public Cupon create(CuponRequest cuponRequest) {
        Cupon cupon = Cupon.builder()
                .codigo(cuponRequest.getCodigo().trim())
                .tipoDescuento(cuponRequest.getTipoDescuento())
                .fechaInicio(cuponRequest.getFechaInicio())
                .fechaFin(cuponRequest.getFechaFin())
                .valor(cuponRequest.getValor())
                .montoMinimo(cuponRequest.getMontoMinimo())
                .usoMaximo(cuponRequest.getUsoMaximo())
                .usosActuales(0)
                .activo(!Boolean.FALSE.equals(cuponRequest.getActivo()))
                .build();

        return repository.save(cupon);
    }

    public Cupon update(Long id, CuponRequest request) {
        Cupon existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encuentra el cupon con el id: " + id));

        if (request.getUsoMaximo() != null
                && existente.getUsosActuales() != null
                && request.getUsoMaximo() < existente.getUsosActuales()) {
            throw new IllegalArgumentException("El uso maximo no puede ser menor a los usos actuales");
        }

        existente.setCodigo(request.getCodigo().trim());
        existente.setTipoDescuento(request.getTipoDescuento());
        existente.setValor(request.getValor());
        existente.setMontoMinimo(request.getMontoMinimo());
        existente.setFechaInicio(request.getFechaInicio());
        existente.setFechaFin(request.getFechaFin());
        existente.setUsoMaximo(request.getUsoMaximo());

        if (request.getActivo() != null) {
            existente.setActivo(request.getActivo());
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro cupon con id: " + id);
        }
        repository.deleteById(id);
    }
}