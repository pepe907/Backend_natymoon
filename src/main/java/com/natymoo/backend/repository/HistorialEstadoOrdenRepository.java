package com.natymoo.backend.repository;

import com.natymoo.backend.entity.HistorialEstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoOrdenRepository extends JpaRepository<HistorialEstadoOrden, Long> {
    List<HistorialEstadoOrden> findByOrdenIdOrderByFechaAsc(Long ordenId);
}
