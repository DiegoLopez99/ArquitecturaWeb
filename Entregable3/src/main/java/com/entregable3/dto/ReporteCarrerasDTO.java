package com.entregable3.dto;

public class ReporteCarrerasDTO {
    private String carrera;
    private Long cant_inscriptos;
    private Long cant_graduados;
    private int anio;

    public ReporteCarrerasDTO(String carrera, Integer anio, Long cant_inscriptos, Long cant_graduados) {
        this.carrera = carrera;
        this.anio = (anio != null) ? anio : 0;
        this.cant_inscriptos = (cant_inscriptos != null) ? cant_inscriptos : 0;
        this.cant_graduados = (cant_graduados != null) ? cant_graduados : 0;
    }

    public String getCarrera() {
        return carrera;
    }
    public int getAnio() {
        return anio;
    }
    public Long getCant_inscriptos() {
        return cant_inscriptos;
    }
    public Long getCant_graduados() {
        return cant_graduados;
    }

    @Override
    public String toString() {
        return "ReporteDTO => Carrera: "+this.carrera+", año: "+this.anio+", inscriptos: "+this.cant_inscriptos+", graduados: "+this.cant_graduados;
    }
}
