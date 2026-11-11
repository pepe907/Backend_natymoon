package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoNovedad;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NovedadResponse {
    private Long id;
    private Long usuarioId;
    private Long ordenId;
    private String asunto;
    private String descripcion;
    private EstadoNovedad estado;
    private String respuesta;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
