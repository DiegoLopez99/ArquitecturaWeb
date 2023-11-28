package microservice.Model.Dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class MonopatinPorUso {
    private long idMonopatin;
    private int horas;
    private int minutos;


    public MonopatinPorUso(long id,int horas, int minutos) {
        this.idMonopatin = id;
        this.horas = horas;
        this.minutos = minutos;
    }

    public long getIdMonopatin() {
        return idMonopatin;
    }

    public void setIdMonopatin(long idMonopatin) {
        this.idMonopatin = idMonopatin;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }
}
