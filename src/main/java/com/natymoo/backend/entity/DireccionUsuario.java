package com.natymoo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "direcciones_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La dirección debe pertenecer a un usuario")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 50)
    private String alias;

    @NotBlank(message = "El nombre del destinatario es obligatorio")
    @Column(nullable = false, length = 150)
    private String destinatario;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @NotBlank(message = "La calle es obligatoria")
    @Column(nullable = false, length = 200)
    private String calle;

    @Column(length = 20)
    private String numero;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(length = 100)
    private String departamento;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Builder.Default
    @Column(nullable = false, length = 100)
    private String pais = "Colombia";

    @Builder.Default
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
