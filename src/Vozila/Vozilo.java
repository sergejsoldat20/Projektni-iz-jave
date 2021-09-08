package Vozila;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import Infrastruktura.PruzniPrelaz;
import Mapa.*;
import com.sun.javafx.collections.MappingChange;
import sample.Main;

public class Vozilo extends Thread {

    private String marka;
    private String model;
    private int godiste;
    private long brzinaVozila;
    private int x;
    private int y;
    private ArrayList<int[]> put = new ArrayList<>();



    public Vozilo(){
        super();
    }
    public Vozilo(String marka,String model,int godiste,int x, int y){
        this.marka = marka;
        this.model = model;
        this.godiste = godiste;
        this.x = x;
        this.y = y;


    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getModel() {
        return model;
    }

    public String getMarka() {
        return marka;
    }

    public long getBrzinaVozila() {
        return brzinaVozila;
    }

    public int getGodiste() {
        return godiste;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setGodiste(int godiste) {
        this.godiste = godiste;
    }

    public void setPut(ArrayList<int[]> put) {
        this.put = put;
    }

    public void setBrzinaVozila(long brzinaVozila) {
        this.brzinaVozila = brzinaVozila;
    }



    @Override
    public void run() {

        int []trenutneKoordinate= null;
        while(put.size() >0){
            int koordinate[] = put.get(0);

            synchronized (Mapa.mapLock){
                PruzniPrelaz pruzniPrelaz = Main.MAPA.getElementAt(koordinate[0],koordinate[1]).getPruzniPrelaz();
                if(pruzniPrelaz != null && pruzniPrelaz.isZauzetPrelaz()){
                    try {
                        Mapa.mapLock.wait();
                        continue;
                    }catch (InterruptedException ex){
                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                    }
                }else{
                    if(!Main.MAPA.getElementAt(koordinate[0],koordinate[1]).elementImaVozilo()){

                        //provjeri da li ima vozilo neke koordinate
                         if(trenutneKoordinate != null){
                            //obrisi element sa starog polja ako ima vec neke koordinate
                            Main.MAPA.getElementAt(trenutneKoordinate[0],trenutneKoordinate[1]).ukloniObjekatSaListe(this);

                        }
                        //prebaci vozilo na novo polje
                        Main.MAPA.getElementAt(koordinate[0],koordinate[1]).dodajObjekatUListu(this);
                        trenutneKoordinate = new int[]{koordinate[0],koordinate[1]};
                        put.remove(0);
                        Mapa.mapLock.notifyAll();
                    }else{
                        try{
                            Mapa.mapLock.wait();
                            continue;
                        }catch (InterruptedException ex){
                            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                        }
                    }
                }
            }
            try{
                Thread.sleep(brzinaVozila);
            }catch (InterruptedException ex){
                Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
            }
        }
        synchronized (Mapa.mapLock){
            Main.MAPA.getElementAt(trenutneKoordinate[0],trenutneKoordinate[1]).ukloniObjekatSaListe(this);
            Mapa.mapLock.notifyAll();
        }
    }
}
