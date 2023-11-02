package com.tpe.microserviciousarios.domain;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cuenta")
@Getter @ToString @Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Double saldo;
    @Column(name = "fecha_alta")
    private Timestamp fechaAlta;
    @Column
    private String estado;
    @ManyToMany(mappedBy = "cuentas")
    private Set<Usuario> usuarios = new HashSet<>();

    public Cuenta(Double saldo, Timestamp fechaAlta, String estado) {
        this.saldo = saldo;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
    }
}
