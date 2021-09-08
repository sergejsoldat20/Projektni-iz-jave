package IstorijaKretanja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class IstorijaKretanja implements Serializable {

    private static final long serialVersionUID = 2329684823267757650L;
    private ArrayList<int[]> predjeneKoordinate = new ArrayList<>();
    private ArrayList<String> zaustavljanjaNaStanicama = new ArrayList<>();
    private long vrijemeTrajanja;
    private String naziv;

    public IstorijaKretanja(){

    }
    public void dodajNovuKoordinatu(int[] koordinata){
        predjeneKoordinate.add(koordinata);
    }
    public void dodajNovuStanicu(String stanica){
        zaustavljanjaNaStanicama.add(stanica);
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setVrijemeTrajanja(long vrijemeTrajanja) {
        this.vrijemeTrajanja = vrijemeTrajanja;
    }

    public String getPredjeneKoordinate(){
        String strKoordinate = "";
        Iterator<int[]> iterator = predjeneKoordinate.iterator();
        while(iterator.hasNext()){
            strKoordinate += Arrays.toString(iterator.next());
        }
        return strKoordinate;
    }

    public ArrayList<String> getZaustavljanjaNaStanicama() {
        return zaustavljanjaNaStanicama;
    }

    public long getVrijemeTrajanja() {
        return vrijemeTrajanja;
    }

    public String getNaziv() {
        return naziv;
    }
}
