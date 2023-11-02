package com.tpe.microserviciousarios.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(name = "nro_telefono", nullable = false)
    private Long nroTelefono;
    @Column(nullable = false)
    private String email;
    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;
    @Column(nullable = false)
    private String contrasenia;
    @ManyToMany
    @JoinTable(
            name = "usuario_cuenta",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_cuenta")
    )
    private Set<Cuenta> cuentas = new HashSet<>();

    public Usuario(String nombre, String apellido, Long nroTelefono, String email, String nombreUsuario, String contrasenia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nroTelefono = nroTelefono;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }
}
