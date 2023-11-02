package com.tpe.microservicioadministracion.domain.clases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class Mantenimiento {
    private Long idMonopatin;
    private LocalDate inicio;
    private LocalDate finalizacion;
    private String motivo;
    private boolean reparado;
}
