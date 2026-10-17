package com.natymoo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El código del cupón es obligatorio")
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @NotNull(message = "Selecciona el tipo de descuento")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private TipoDescuento tipoDescuento;

    @NotNull(message = "El valor del descuento es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a cero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Builder.Default
    @Column(name = "monto_minimo")
    private BigDecimal montoMinimo = BigDecimal.ZERO;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "uso_maximo")
    private Integer usoMaximo;

    @Builder.Default
    @Column(name = "usos_actuales", nullable = false)
    private Integer usosActuales = 0;

    @Builder.Default
    private Boolean activo = true;
}
