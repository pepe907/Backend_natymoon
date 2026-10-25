package com.natymoo.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersResponse {
    private Long id;
    private Long rolId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
}