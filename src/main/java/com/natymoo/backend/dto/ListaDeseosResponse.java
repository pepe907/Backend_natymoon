package com.natymoo.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListaDeseosResponse {
    private Long id;
    private Long usuarioId;
    private Long productoId;
    private LocalDateTime localDateTime;
}
