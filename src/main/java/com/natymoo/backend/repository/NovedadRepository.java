package com.natymoo.backend.repository;

import com.natymoo.backend.entity.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovedadRepository extends JpaRepository<Novedad, Long> {
    List<Novedad> findByUsuarioId(Long usuarioId);
}
