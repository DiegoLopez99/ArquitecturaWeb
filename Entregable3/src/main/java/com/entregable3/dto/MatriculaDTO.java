package com.entregable3.dto;

public class MatriculaDTO {
    private int estudianteId;
    private Long carreraId;
    private String anioInscripcion;
    private String anioGraduacion;
    private int antiguedad;

    public MatriculaDTO(int estudianteId, Long carreraId, String anioInscripcion, String anioGraduacion, int antiguedad) {
        this.estudianteId = estudianteId;
        this.carreraId = carreraId;
        this.anioInscripcion = anioInscripcion;
        this.anioGraduacion = anioGraduacion;
        this.antiguedad = antiguedad;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public Long getCarreraId() {
        return carreraId;
    }

    public String getAnioInscripcion() {
        return anioInscripcion;
    }

    public String getAnioGraduacion() {
        return anioGraduacion;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public void setCarreraId(Long carreraId) {
        this.carreraId = carreraId;
    }

    public void setAnioInscripcion(String inscripcion) {
        this.anioInscripcion = inscripcion;
    }

    public void setAnioGraduacion(String graduacion) {
        this.anioGraduacion = graduacion;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    @Override
    public String toString() {
        return "MatriculaDTO{" +
                "estudianteId=" + estudianteId +
                ", carreraId=" + carreraId +
                ", inscripcion='" + anioInscripcion + '\'' +
                ", graduacion='" + anioGraduacion + '\'' +
                ", antiguedad=" + antiguedad +
                '}';
    }
}
