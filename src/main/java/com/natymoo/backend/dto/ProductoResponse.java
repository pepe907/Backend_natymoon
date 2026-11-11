package com.natymoo.backend.dto;

import com.natymoo.backend.entity.Genero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal precioBase;
    private BigDecimal precioOferta;
    private Genero genero;
    private String material;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}