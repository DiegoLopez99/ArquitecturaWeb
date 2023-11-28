package com.tpe.microservicioadministracion.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "rol")
@Data @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class Rol implements Serializable {
    @Id
    @Column(name = "id_rol")
    private Long id;
    @Column
    private String tipo;

    public Rol(String tipo) {
        this.tipo = tipo;
    }
}
