package org.entregable2.entity;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Carrera_EstudiantePK implements Serializable{
    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    public Carrera_EstudiantePK(Estudiante estudiante, Carrera carrera) {
        this.estudiante = estudiante;
        this.carrera = carrera;
    }
    public Carrera_EstudiantePK() {
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrera_EstudiantePK that = (Carrera_EstudiantePK) o;
        return estudiante.getDni() == that.getEstudiante().getDni() && carrera.getId() == that.getCarrera().getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(estudiante, carrera);
    }
}
