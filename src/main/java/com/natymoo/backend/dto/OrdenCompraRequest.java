package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoOrden;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdenCompraRequest {
    private Long usuarioId;
    private Long cuponId;
    private Long direccionId;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal costoEnvio;
    private BigDecimal total;
    private EstadoOrden estado;
}
