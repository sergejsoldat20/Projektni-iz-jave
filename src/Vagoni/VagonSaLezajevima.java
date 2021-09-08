package Vagoni;

public class VagonSaLezajevima extends PutnickiVagon{
    private int brojMjesta;

    public VagonSaLezajevima(int brojMjesta,String oznakaVagona,double duzinaVagona,int x, int y){
        super(oznakaVagona, duzinaVagona, x, y);
        this.brojMjesta = brojMjesta;
    }

    public int getBrojMjesta() {
        return brojMjesta;
    }

    public void setBrojMjesta(int brojMjesta) {
        this.brojMjesta = brojMjesta;
    }
}
