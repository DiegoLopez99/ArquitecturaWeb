package com.entregable3.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "carrera_estudiante")
public class Carrera_Estudiante {
    @EmbeddedId
    private Carrera_EstudiantePK primaryKeys;
    @Column(name = "anio_inscripcion", nullable = false)
    private Timestamp fechaInscripcion;
    @Column(nullable = true)
    private Timestamp graduacion;
    @Column
    private int antiguedad;

    public Carrera_Estudiante(Carrera_EstudiantePK pk, Timestamp inscripcion, Timestamp graduacion, int antiguedad) {
        this.primaryKeys = pk;
        this.fechaInscripcion = inscripcion;
        this.graduacion = graduacion;
        this.antiguedad = antiguedad;
    }

    public Carrera_Estudiante() {
    }

    public Timestamp getAnioInscripcion() {
        return fechaInscripcion;
    }

    public Carrera_EstudiantePK getPrimaryKeys() {
        return primaryKeys;
    }

    public Timestamp getGraduacion() {
        return graduacion;
    }

    public void setGraduacion(Timestamp graduacion) {
        this.graduacion = graduacion;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }
}
