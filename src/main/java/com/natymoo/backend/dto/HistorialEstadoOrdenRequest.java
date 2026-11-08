package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoOrden;
import lombok.Data;

@Data
public class HistorialEstadoOrdenRequest {
    private Long ordenCompraId;
    private EstadoOrden estadoOrden;
    private String comentario;
}