package com.natymoo.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DireccionUsuarioResponse {
    private Long id;
    private Long usuarioId;
    private String alias;
    private String destinatario;
    private String telefonoContacto;
    private String calle;
    private String numero;
    private String ciudad;
    private String departamento;
    private String codigoPostal;
    private String pais;
    private Boolean esPrincipal;
    private LocalDateTime fechaCreacion;
}