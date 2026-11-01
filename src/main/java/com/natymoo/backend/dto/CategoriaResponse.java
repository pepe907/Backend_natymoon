package com.natymoo.backend.dto;

import lombok.Data;

@Data
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long categoriaPadre;
    private String nombreCategoriaPadre;
    private Boolean activo;
}
