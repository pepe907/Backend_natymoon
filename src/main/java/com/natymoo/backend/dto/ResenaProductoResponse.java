package com.natymoo.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResenaProductoResponse {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Long usuarioId;
    private String usuarioNombre;
    private short calificacion;
    private String comentario;
    private LocalDateTime fecha;
}