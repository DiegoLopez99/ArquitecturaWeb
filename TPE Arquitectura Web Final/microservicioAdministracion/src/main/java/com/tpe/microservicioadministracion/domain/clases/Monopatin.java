package com.tpe.microservicioadministracion.domain.clases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Monopatin {
    private Long id;
    private String estado;
    private String ubicacion;
    private Float kmRecorridos;

}
