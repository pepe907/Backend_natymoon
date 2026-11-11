package com.natymoo.backend.dto;

import com.natymoo.backend.entity.EstadoPago;
import com.natymoo.backend.entity.MetodoPago;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PagoResponse {

    private Long id;
    private Long ordenId;
    private BigDecimal monto;
    private EstadoPago estadoPago;
    private String referenciaTransaccion;
    private MetodoPago metodoPago;
    private LocalDateTime fechaPago;
}