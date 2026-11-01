package com.natymoo.backend.service;

import com.natymoo.backend.dto.CategoriaRequest;
import com.natymoo.backend.entity.Categoria;
import com.natymoo.backend.exception.ResourceNotFoundException;
import com.natymoo.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository repository;

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Categoria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro categoria con id: " + id));
    }

    public Categoria create(CategoriaRequest categoriaRequest) {
        Categoria categoriaPadre = null;

      if (categoriaRequest.getCategoriaPadre() != null){
          categoriaPadre = repository.findById(categoriaRequest.getCategoriaPadre())
                  .orElseThrow(() -> new ResourceNotFoundException("No se encontro la categoria padre"));
      }
        Categoria categoria = Categoria.builder()
                .nombre(categoriaRequest.getNombre())
                .descripcion(categoriaRequest.getDescripcion())
                .activo(true)
                .categoriaPadre(categoriaPadre)
                .build();

        return repository.save(categoria);
    }

    public Categoria update(Long id, CategoriaRequest categoriaRequest) {
        Categoria existente = findById(id);

        existente.setNombre(categoriaRequest.getNombre());
        existente.setDescripcion(categoriaRequest.getDescripcion());

        if (categoriaRequest.getActivo() != null){
            existente.setActivo(categoriaRequest.getActivo());
        }

        if (categoriaRequest.getCategoriaPadre() != null){
            Categoria categoriaPadre = repository.findById(categoriaRequest.getCategoriaPadre())
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontro la categoria padre"));
            existente.setCategoriaPadre(categoriaPadre);
        }else {
            existente.setCategoriaPadre(null);
        }

        return repository.save(existente);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro categoria con id: " + id);
        }
        repository.deleteById(id);
    }

}
