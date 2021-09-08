package Lokomotive;

import org.w3c.dom.html.HTMLOptGroupElement;
import sample.Controller;

public abstract class Lokomotiva {

    private double snagaLokomotive;
    private String oznakaLokomotive;
    private String pogonLokomotive;

    private int x;
    private int y;
    public Lokomotiva(){
        super();
    }

    public Lokomotiva(double snagaLokomotive,String oznakaLokomotive,String pogonLokomotive,int x, int y){
        super();
        this.snagaLokomotive = snagaLokomotive;
        this.oznakaLokomotive = oznakaLokomotive;
        this.pogonLokomotive = pogonLokomotive;
        this.x = x;
        this.y = y;

    }
    public boolean isElektricnaLokomotiva(){
       return pogonLokomotive.equals(Controller.ELEKTRICNI_POGON);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setSnagaLokomotive(double snagaLokomotive) {
        this.snagaLokomotive = snagaLokomotive;
    }

    public void setOznakaLokomotive(String oznakaLokomotive) {
        this.oznakaLokomotive = oznakaLokomotive;
    }

    public void setPogonLokomotive(String pogonLokomotive) {
        this.pogonLokomotive = pogonLokomotive;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getOznakaLokomotive() {
        return oznakaLokomotive;
    }

    public double getSnagaLokomotive() {
        return snagaLokomotive;
    }

    public String getPogonLokomotive() {
        return pogonLokomotive;
    }

    @Override
    public String toString() {
        return "Lokomotiva{" +
                "snagaLokomotive=" + snagaLokomotive +
                ", oznakaLokomotive='" + oznakaLokomotive + '\'' +
                ", pogonLokomotive='" + pogonLokomotive + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
