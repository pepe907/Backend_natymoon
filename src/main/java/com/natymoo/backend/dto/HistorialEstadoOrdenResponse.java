package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoOrden;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialEstadoOrdenResponse {
    private Long id;
    private Long ordenCompraId;
    private EstadoOrden estadoOrden;
    private String comentario;
    private LocalDateTime localDateTime;
}