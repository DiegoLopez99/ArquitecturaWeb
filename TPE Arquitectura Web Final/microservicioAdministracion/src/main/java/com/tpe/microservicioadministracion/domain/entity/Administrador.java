package com.tpe.microservicioadministracion.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "administrador")
@Data @EqualsAndHashCode
@NoArgsConstructor
public class Administrador implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre_usuario")
    @NotNull
    private String nombreUsuario;
    @Column
    @NotNull
    private String contrasenia;
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

    public Administrador(String nombreUsuario, String contrasenia, Rol rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }
}
