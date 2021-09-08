package Vozila;

public class Automobil  extends Vozilo{

    private int brojVrata;

    public Automobil(String marka,String model,int godiste,int brojVrata,int x,int y){
        super(marka,model,godiste,x,y);
    }

    public void setBrojVrata(int brojVrata) {
        this.brojVrata = brojVrata;
    }

    public int getBrojVrata() {
        return brojVrata;
    }

}
