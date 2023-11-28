package microservice.Model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "monopatin")
public class Monopatin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String estado;
    @Column
    String ubicacion;
    @Column
    float km_recorridos;

    public Monopatin( String estado, String ubicacion, float km_recorridos) {
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.km_recorridos = km_recorridos;
    }

    public Monopatin() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public float getKm_recorridos() {
        return km_recorridos;
    }

    public void setKm_recorridos(float km_recorridos) {
        this.km_recorridos = km_recorridos;
    }
}
