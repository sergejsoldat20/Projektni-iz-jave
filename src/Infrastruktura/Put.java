package Infrastruktura;

public class Put extends Polje {
    private int x;
    private int y;

    private String imePuta;

    public Put(int x,int y,String imePuta){
        this.imePuta = imePuta;
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public String getImePuta() {
        return imePuta;
    }

}
