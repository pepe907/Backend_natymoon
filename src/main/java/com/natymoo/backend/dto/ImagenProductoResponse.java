package com.natymoo.backend.dto;

import lombok.Data;

@Data
public class ImagenProductoResponse {
    private Long id;
    private Long productoId;
    private String url;
    private Integer orden;
    private boolean esPrincipal;
}