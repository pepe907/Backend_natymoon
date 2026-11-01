package com.natymoo.backend.dto;

import com.natymoo.backend.entity.TipoDescuento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CuponRequest {
    private Long id;
    private String codigo;
    private TipoDescuento tipoDescuento;
    private BigDecimal valor;
    private BigDecimal montoMinimo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer usoMaximo;
    private Integer usosActuales;
    private Boolean activo;
}
