package com.tpe.viaje.DTO;

import lombok.Data;

@Data
public class MonopatinPorUso {
    private Long id_monopatin;
    private int horas;
    private int minutos;

    public MonopatinPorUso(Long id_monopatin, int horas, int minutos) {
        this.id_monopatin = id_monopatin;
        this.horas = horas;
        this.minutos = minutos;
    }
}
