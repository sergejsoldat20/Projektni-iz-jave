package Infrastruktura;

public class SekcijaPuta {
    private long maksimalnaBrzinaSekcije;
    private int maksimalanBrojVozila;
    private int brojKreiranihVozila = 0;
    private String imeSekcije;
    public SekcijaPuta(String imeSekcije){
        this.imeSekcije = imeSekcije;
    }

    public void setBrojKreiranihVozila(int brojKreiranihVozila) {
        this.brojKreiranihVozila = brojKreiranihVozila;
    }

    public void setMaksimalanBrojVozila(int maksimalanBrojVozila) {
        this.maksimalanBrojVozila = maksimalanBrojVozila;
    }

    public void setImeSekcije(String imeSekcije) {
        this.imeSekcije = imeSekcije;
    }

    public void setMaksimalnaBrzinaSekcije(long maksimalnaBrzinaSekcije) {
        this.maksimalnaBrzinaSekcije = maksimalnaBrzinaSekcije;
    }

    public long getMaksimalnaBrzinaSekcije() {
        return maksimalnaBrzinaSekcije;
    }

    public int getBrojKreiranihVozila() {
        return brojKreiranihVozila;
    }

    public int getMaksimalanBrojVozila() {
        return maksimalanBrojVozila;
    }

    public String getImeSekcije() {
        return imeSekcije;
    }
}
