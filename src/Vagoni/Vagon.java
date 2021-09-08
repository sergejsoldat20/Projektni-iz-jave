package Vagoni;

public abstract class Vagon {
    private int x;
    private int y;
    private double duzinaVagona;
    private String oznakaVagona;

    public Vagon(){
        super();
    }
    public Vagon(String oznakaVagona,double duzinaVagona,int x,int y){
        super();
        this.oznakaVagona = oznakaVagona;
        this.duzinaVagona = duzinaVagona;
        this.x = x;
        this.y = y;

    }

    public void setOznakaVagona(String oznakaVagona) {
        this.oznakaVagona = oznakaVagona;
    }

    public void setDuzinaVagona(double duzinaVagona) {
        this.duzinaVagona = duzinaVagona;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getOznakaVagona() {
        return oznakaVagona;
    }

    public double getDuzinaVagona() {
        return duzinaVagona;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Vagon{" +
                "x=" + x +
                ", y=" + y +
                ", duzinaVagona=" + duzinaVagona +
                ", oznakaVagona='" + oznakaVagona + '\'' +
                '}';
    }
}
