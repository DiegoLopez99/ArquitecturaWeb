package com.tpe.viaje.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viaje")
public class Viaje implements Serializable {
    @Id
    long id;
    @Column
    long id_monopatin;
    @Column
    LocalDateTime inicio;
    @Column
    LocalDateTime fin;
    @Column
    float km;
    @Column
    float costo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "viaje", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Pausa> pausas;

    public Viaje(long id,long id_monopatin, LocalDateTime inicio, LocalDateTime fin, float km, float costo ) {
        this.id = id;
        this.id_monopatin = id_monopatin;
        this.inicio = inicio;
        this.fin = fin;
        this.km = km;
        this.costo = costo;
        pausas = new ArrayList<>();
    }

    public Viaje() {

    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public float getKm() {
        return km;
    }

    public float getCosto() {
        return costo;
    }


    public List<Pausa> getPausas() {
        return pausas;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }


    public void addPausa(Pausa p){
        pausas.add(p);
    }
    public void removePausa(Pausa p){
        pausas.remove(p);
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
                ", pausas=" + pausas +
                '}';
    }
}
