package com.tpe.viaje.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.Date;

@Document(collection = "pausa")
@Data
public class Pausa implements Serializable {
    @Id
    String id;
    String id_viaje;
    Date tiempo;
    String motivo;

    public Pausa(String id,Date tiempo, String motivo, String id_viaje) {
        this.id=id;
        this.id_viaje = id_viaje;
        this.tiempo=tiempo;
        this.motivo = motivo;
    }

    public Pausa() {

    }

    @Override
    public String toString() {
        return "Pausa{" +
                "id=" + id +
                ", motivo='" + motivo + '\'' +
                ", viaje_id=" + this.id_viaje +
                '}';
    }
}
