package com.natymoo.backend.dto;

import com.natymoo.backend.entity.OrdenCompra;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleOrdenRequest {
    private Long id;
    private Long ordenCompraId;
    private Long varianteProductoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
