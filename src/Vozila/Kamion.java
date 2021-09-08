package Vozila;

public class Kamion extends Vozilo {

    private double nosivost;

    public Kamion(String marka,String model,int godiste,double nosivost,int x,int y){
        super(marka,model,godiste,x,y);
        this.nosivost = nosivost;
    }

    public void setNosivost(double nosivost) {
        this.nosivost = nosivost;
    }

    public double getNosivost() {
        return nosivost;
    }
}
