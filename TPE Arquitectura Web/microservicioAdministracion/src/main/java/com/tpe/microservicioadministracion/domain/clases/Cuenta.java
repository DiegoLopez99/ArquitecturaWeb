package com.tpe.microservicioadministracion.domain.clases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {
    private Long id;
    private Double saldo;
    private Timestamp fechaAlta;
    private String estado;


}
