package Infrastruktura;

public class Pruga  extends  Polje{

    private int x;
    private int y;

    public Pruga(int x,int y){
       this.x = x;
       this.y = y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
