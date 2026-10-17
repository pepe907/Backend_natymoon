package com.natymoo.backend.repository;

import com.natymoo.backend.entity.ResenaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaProductoRepository extends JpaRepository<ResenaProducto, Long> {
    List<ResenaProducto> findByProductoId(Long productoId);
}
