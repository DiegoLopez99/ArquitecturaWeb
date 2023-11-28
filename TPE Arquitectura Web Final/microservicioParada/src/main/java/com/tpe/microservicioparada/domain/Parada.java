package com.tpe.microservicioparada.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "parada")
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Parada implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String longitud;
    @Column
    private String latitud;
    @Column
    private Integer capacidad;

    public Parada(String longitud, String latitud, Integer capacidad) {
        this.longitud = longitud;
        this.latitud = latitud;
        this.capacidad = capacidad;
    }
}
