package com.natymoo.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VarianteProductoRequest {
    private Long productoId;
    private String sku;
    private String talla;
    private String color;
    private Integer stock;
    private BigDecimal precioAdicional;
    private Boolean activo;
}
