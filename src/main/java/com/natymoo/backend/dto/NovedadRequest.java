package com.natymoo.backend.dto;

import lombok.Data;

@Data
public class NovedadRequest {
    private Long usuarioId;
    private Long ordenId;
    private String asunto;
    private String descripcion;
    private String respuesta;
}