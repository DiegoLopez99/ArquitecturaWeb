package com.tpe.viaje.DTO;

import lombok.Getter;

@Getter
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

    public void setMesInicio(Integer mesInicio) {
        this.mesInicio = mesInicio;
    }

    public void setMesFinal(Integer mesFinal) {
        this.mesFinal = mesFinal;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public void setTotalFacturado(float totalFacturado) {
        this.totalFacturado = totalFacturado;
    }
}
