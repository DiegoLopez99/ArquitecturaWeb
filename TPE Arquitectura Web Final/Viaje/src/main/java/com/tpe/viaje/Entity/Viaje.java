package com.tpe.viaje.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "viaje")
@Document(collection = "viaje")
@Data
public class Viaje implements Serializable {
    @Id
    String id;
    Long id_monopatin;
    LocalDateTime inicio;
    LocalDateTime fin;
    float km;
    float costo;

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "viaje", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Pausa> pausas;*/

    public Viaje(String id,Long id_monopatin, LocalDateTime inicio, LocalDateTime fin, float km, float costo ) {
        this.id = id;
        this.id_monopatin = id_monopatin;
        this.inicio = inicio;
        this.fin = fin;
        this.km = km;
        this.costo = costo;
        //pausas = new ArrayList<>();
    }

    public Viaje() {

    }

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                "id_monopatin"+id_monopatin+
                ", inicio=" + inicio +
                ", fin=" + fin +
                ", km=" + km +
                ", costo=" + costo +
                '}';
    }
}
