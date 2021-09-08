package Vagoni;

import java.security.spec.EdECPoint;

public class TeretniVagon extends Vagon{

    private double nosivostVagona;

    public TeretniVagon(String oznakaVagona, double duzinaVagona,int x, int y,double nosivostVagona){
        super(oznakaVagona, duzinaVagona, x, y);
        this.nosivostVagona = nosivostVagona;

    }

    public double getNosivostVagona() {
        return nosivostVagona;
    }

    public void setNosivostVagona(double nosivostVagona) {
        this.nosivostVagona = nosivostVagona;
    }
}
