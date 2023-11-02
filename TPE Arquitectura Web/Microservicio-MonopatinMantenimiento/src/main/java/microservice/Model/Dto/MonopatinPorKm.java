package microservice.Model.Dto;

public class MonopatinPorKm {
    private Long idMonopatin;
    private int PausaH;
    private int PausaM;
    private boolean reparacion;

    public MonopatinPorKm(Long idMonopatin, int pausaH, int pausaM, boolean reparacion) {
        this.idMonopatin = idMonopatin;
        this.PausaH = pausaH;
        this.PausaM = pausaM;
        this.reparacion = reparacion;
    }

    public Long getIdMonopatin() {
        return idMonopatin;
    }

    public void setIdMonopatin(Long idMonopatin) {
        this.idMonopatin = idMonopatin;
    }

    public int getPausaH() {
        return PausaH;
    }

    public void setPausaH(int pausaH) {
        PausaH = pausaH;
    }

    public int getPausaM() {
        return PausaM;
    }

    public void setPausaM(int pausaM) {
        PausaM = pausaM;
    }

    public boolean isReparacion() {
        return reparacion;
    }

    public void setReparacion(boolean reparacion) {
        this.reparacion = reparacion;
    }
}
