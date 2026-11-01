package com.natymoo.backend.service;

import com.natymoo.backend.dto.CuponRequest;
import com.natymoo.backend.entity.Cupon;
import com.natymoo.backend.entity.TipoDescuento;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CuponService {

    private final CuponRepository repository;

    public List<Cupon> findAll() {
        return repository.findAll();
    }

    public Cupon findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro cupon con id: " + id));
    }

    public Cupon create(CuponRequest cuponRequest) {
        Cupon cupon = Cupon.builder()
                .codigo(cuponRequest.getCodigo())
                .tipoDescuento(cuponRequest.getTipoDescuento())
                .fechaInicio(cuponRequest.getFechaInicio())
                .fechaFin(cuponRequest.getFechaFin())
                .valor(cuponRequest.getValor())
                .montoMinimo(cuponRequest.getMontoMinimo())
                .usoMaximo(cuponRequest.getUsoMaximo())
                .usosActuales(0)
                .activo(cuponRequest.getActivo() != null ? cuponRequest.getActivo() : true)
                .build();

        return repository.save(cupon);
    }

    public Cupon update(Long id, CuponRequest cuponRequest) {
        Cupon existente = findById(id);

        existente.setCodigo(cuponRequest.getCodigo());
        existente.setTipoDescuento(cuponRequest.getTipoDescuento());
        existente.setValor(cuponRequest.getValor());
        existente.setMontoMinimo(cuponRequest.getMontoMinimo());
        existente.setFechaInicio(cuponRequest.getFechaInicio());
        existente.setFechaFin(cuponRequest.getFechaFin());
        existente.setUsoMaximo(cuponRequest.getUsoMaximo());

        if (cuponRequest.getUsosActuales() != null){
            existente.setUsosActuales(cuponRequest.getUsosActuales());
        }

        if (cuponRequest.getActivo() != null){
            existente.setActivo(cuponRequest.getActivo());
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
