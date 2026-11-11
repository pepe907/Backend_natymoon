package com.natymoo.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImagenProductoRequest {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String url;

    private Integer orden;

    private Boolean esPrincipal;
}