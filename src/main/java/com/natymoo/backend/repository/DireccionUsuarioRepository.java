package com.natymoo.backend.repository;

import com.natymoo.backend.entity.DireccionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionUsuarioRepository extends JpaRepository<DireccionUsuario, Long> {
    List<DireccionUsuario> findByUsuarioId(Long usuarioId);
}
