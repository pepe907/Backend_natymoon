package com.natymoo.backend.dto;

import com.natymoo.backend.entity.Genero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private BigDecimal precioBase;
    private BigDecimal precioOferta;
    private Genero genero;
    private String material;
    private Boolean activo;
}
