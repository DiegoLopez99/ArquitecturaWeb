package com.tpe.microservicioadministracion.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data @NoArgsConstructor
@EqualsAndHashCode
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Timestamp fecha_inicio;
    @Column(name = "tarifa_normal", nullable = false)
    private Float tarifaNormal;
    @Column(name = "tarifa_extra", nullable = true)
    private Float tarifaExtra;

    public Tarifa(Timestamp fecha_inicio, Float tarifaNormal, Float tarifaExtra) {
        this.fecha_inicio = fecha_inicio;
        this.tarifaNormal = tarifaNormal;
        this.tarifaExtra = tarifaExtra;
    }
}
