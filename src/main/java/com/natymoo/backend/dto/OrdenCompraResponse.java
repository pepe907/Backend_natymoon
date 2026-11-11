package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoOrden;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrdenCompraResponse {

    private Long id;
    private Long usuarioId;
    private Long direccionId;
    private Long cuponId;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal costoEnvio;
    private BigDecimal total;
    private EstadoOrden estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}