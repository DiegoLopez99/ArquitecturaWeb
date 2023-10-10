package com.entregable3.dto;

import com.entregable3.domain.Carrera;

import java.io.Serializable;

public class CarreraInscriptosDTO implements Serializable {
    private Carrera carrera;
    private Long inscriptos;

    public CarreraInscriptosDTO(Carrera carrera, Long inscriptos) {
        this.carrera = carrera;
        this.inscriptos = inscriptos;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Long getInscriptos() {
        return inscriptos;
    }

    public void setInscriptos(Long inscriptos) {
        this.inscriptos = inscriptos;
    }

    @Override
    public String toString() {
        return "CarreraInscriptosDTO{" +
                "carrera=" + carrera +
                ", inscriptos=" + inscriptos +
                '}';
    }
}
