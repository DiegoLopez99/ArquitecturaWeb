package com.tpe.viaje.DTO;

import jakarta.persistence.criteria.CriteriaBuilder;

public class TotalFacturadoDTO {
    private Integer mesInicio;
    private Integer mesFinal;
    private Integer anio;
    private float totalFacturado;

    public TotalFacturadoDTO(Integer mesInicio, Integer mesFinal, Integer anio, float totalFacturado) {
        this.mesInicio = mesInicio;
        this.mesFinal = mesFinal;
        this.anio = anio;
        this.totalFacturado = totalFacturado;
    }

    public TotalFacturadoDTO() {
    }

    public Integer getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Integer mesInicio) {
        this.mesInicio = mesInicio;
    }

    public Integer getMesFinal() {
        return mesFinal;
    }

    public void setMesFinal(Integer mesFinal) {
        this.mesFinal = mesFinal;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public float getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(float totalFacturado) {
        this.totalFacturado = totalFacturado;
    }
}
