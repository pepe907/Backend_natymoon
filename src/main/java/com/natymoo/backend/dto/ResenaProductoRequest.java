package com.natymoo.backend.dto;

import lombok.Data;

@Data
public class ResenaProductoRequest {
    private Long productoId;
    private Long usuarioId;
    private Short calificacion;
    private String comentario;

}
