package com.natymoo.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListaDeseosRequest {

    private Long usuarioId;
    private Long productoId;
}
