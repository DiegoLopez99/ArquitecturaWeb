package com.tpe.viaje.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Time;


@Entity
@Table(name = "pausa")

public class Pausa implements Serializable {
    @Id
    long id;
    @Column
    Time tiempo;

    String motivo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje")
    @JsonIgnore
    Viaje viaje;


    public Pausa(long id,Time tiempo, String motivo) {
        this.id=id;
        this.tiempo=tiempo;
        this.motivo = motivo;
    }

    public Time getTiempo() {
        return tiempo;
    }

    public void setTiempo(Time tiempo) {
        this.tiempo = tiempo;
    }

    public Pausa() {

    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public long getId() {
        return id;
    }



    public String getMotivo() {
        return motivo;
    }

    public Viaje getViaje() {
        return viaje;
    }

    @Override
    public String toString() {
        return "Pausa{" +
                "id=" + id +
                ", motivo='" + motivo + '\'' +
                ", viaje_id=" + viaje.getId() +
                '}';
    }
}
