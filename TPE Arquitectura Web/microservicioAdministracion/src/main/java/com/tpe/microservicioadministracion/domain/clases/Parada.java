package com.tpe.microservicioadministracion.domain.clases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
public class Parada {
    private Long id;
    private String ubicacion;
    private int capacidad;
}
