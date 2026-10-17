package com.natymoo.backend.repository;

import com.natymoo.backend.entity.CarritoCompra;
import com.natymoo.backend.entity.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoCompraRepository extends JpaRepository<CarritoCompra, Long> {
    Optional<CarritoCompra> findByUsuarioIdAndEstado(Long usuarioId, EstadoCarrito estado);
}
