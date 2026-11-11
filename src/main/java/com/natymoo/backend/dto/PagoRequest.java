package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoPago;
import com.natymoo.backend.entity.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequest {
    private Long ordenId;
    private BigDecimal monto;
    private EstadoPago estadoPago;
    private String referenciaTransaccion;
    private MetodoPago metodoPago;
}
