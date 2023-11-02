package microservice.Model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
@Entity
@Table(name = "mantenimiento")
public class Mantenimiento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    LocalDate inicio;
    @Column
    LocalDate finalizacion;
    @Column
    String motivo;
    @Column
    Boolean reparado;
    @OneToOne
    Monopatin monopatin;

    public Mantenimiento( LocalDate inicio, LocalDate finalizacion, String motivo, Monopatin monopatin, boolean reparado) {
        this.inicio = inicio;
        this.finalizacion = finalizacion;
        this.motivo = motivo;
        this.monopatin = monopatin;
        this.reparado = reparado;
    }

    public Mantenimiento() {
    }

    public Mantenimiento(Boolean reparado) {
        this.reparado = reparado;
    }

    public Monopatin getMonopatin() {
        return monopatin;
    }

    public void setMonopatin(Monopatin monopatin) {
        this.monopatin = monopatin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFinalizacion() {
        return finalizacion;
    }

    public void setFinalizacion(LocalDate finalizacion) {
        this.finalizacion = finalizacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
