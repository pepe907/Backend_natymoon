package com.natymoo.backend.dto;

import com.natymoo.backend.entity.TipoDescuento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CuponRequest {

    @NotBlank(message = "El codigo es obligatorio")
    @Size(max = 50, message = "El codigo no puede superar 50 caracteres")
    private String codigo;

    @NotNull(message = "El tipo de descuento es obligatorio")
    private TipoDescuento tipoDescuento;

    @NotNull(message = "El valor es obligatorio")
    @Positive(message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    @PositiveOrZero(message = "El monto minimo no puede ser negativo")
    private BigDecimal montoMinimo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    @Positive(message = "El uso maximo debe ser mayor a 0")
    private Integer usoMaximo;

    private Boolean activo;
}