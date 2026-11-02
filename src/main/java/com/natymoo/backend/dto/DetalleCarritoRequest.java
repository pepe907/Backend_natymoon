package com.natymoo.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleCarritoRequest {
    private Long id;
    private Long carritoCompraId;
    private Long varianteProductoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;

}
