package com.natymoo.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarritoCompraRequest {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private String estado;
}