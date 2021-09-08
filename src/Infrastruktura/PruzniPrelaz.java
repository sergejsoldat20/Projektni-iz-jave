package Infrastruktura;

public class PruzniPrelaz extends Polje {

    private boolean zauzetPrelaz;

    private int x;
    private int y;

    public PruzniPrelaz(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZauzetPrelaz(boolean zauzetPrelaz) {
        this.zauzetPrelaz = zauzetPrelaz;
    }

    public boolean isZauzetPrelaz() {
        return zauzetPrelaz;
    }
}
