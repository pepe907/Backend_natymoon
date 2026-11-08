package com.natymoo.backend.dto;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
}