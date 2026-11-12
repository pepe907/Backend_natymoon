package com.natymoo.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VarianteProductoResponse {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private String sku;
    private String talla;
    private String color;
    private Integer stock;
    private BigDecimal precioAdicional;
    private Boolean activo;
}