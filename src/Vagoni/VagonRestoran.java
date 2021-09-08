package Vagoni;

public class VagonRestoran extends PutnickiVagon {

    private String opis;

    public VagonRestoran(String opis,String oznakaVagona,double duzinaVagona,int x, int y){
        super(oznakaVagona, duzinaVagona, x, y);
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
