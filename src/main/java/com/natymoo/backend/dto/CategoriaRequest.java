package com.natymoo.backend.dto;

import lombok.Data;

@Data
public class CategoriaRequest {
    private String nombre;
    private String descripcion;
    private Long categoriaPadre;
    private Boolean activo;
}