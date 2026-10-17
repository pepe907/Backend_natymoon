package com.natymoo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "variantes_producto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"producto_id", "talla", "color"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La variante debe pertenecer a un producto")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @NotBlank(message = "La talla es obligatoria")
    @Column(nullable = false, length = 10)
    private String talla;

    @NotBlank(message = "El color es obligatorio")
    @Column(nullable = false, length = 50)
    private String color;

    @Builder.Default
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock = 0;

    @Builder.Default
    @Column(name = "precio_adicional", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioAdicional = BigDecimal.ZERO;

    @Builder.Default
    private Boolean activo = true;
}
