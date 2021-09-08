package Lokomotive;

import Infrastruktura.Stanica;
import IstorijaKretanja.IstorijaKretanja;
import Mapa.Element;
import Mapa.Mapa;
import Vagoni.Vagon;
import com.sun.javafx.collections.MappingChange;
import javafx.beans.binding.ObjectExpression;
import javafx.scene.control.Control;
import sample.Controller;
import sample.Main;

import javax.naming.SizeLimitExceededException;
import javax.sql.rowset.spi.SyncFactory;
import javax.swing.*;
import java.awt.image.ImagingOpException;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Kompozicija extends Thread{

    private ArrayList<Object> kompozicija = new ArrayList<>();
    private ArrayList<String> stanice = new ArrayList<>();
    private boolean vozNaUlazu = false;
    private int brojKomponenataUStanici;
    private int brojKomponenataVanStanice;
    private String pocetnaStanica;
    private String krajnjaStanica;
    private IstorijaKretanja istorijaKretanja = new IstorijaKretanja();
    private ArrayList<Element> predjenaPoljaZadnjegElementa = new ArrayList<>();
    private long brzinaVoza;
    private long privremenaBrzina = 495;
    private ArrayList<Element> predjenaPolja = new ArrayList<>();
    private ArrayList<Element> poljaPodNaponom = new ArrayList<>();
    public void setVozNaUlazu(boolean vozNaUlazu) {
        this.vozNaUlazu = vozNaUlazu;
    }
    public boolean isVozNaUlazu() {
        return vozNaUlazu;
    }
    public boolean kompozicijaJeElektricna;
    private String name;

    public Kompozicija(ArrayList<Lokomotiva> lokomotive, ArrayList<Vagon> vagoni, String pocetnaStanica,String krajnjaStanica,long brzinaVoza) {


        //provjera da li ima elektricnih lokomotiva
        for(Lokomotiva l : lokomotive){
            if(l.isElektricnaLokomotiva()) {
               this.kompozicijaJeElektricna = true;
            }
        }
        //dodaj polje pod naponom na pocetak
        if(kompozicijaJeElektricna){
            this.kompozicija.add(new NaponLokomotiva(0,"oznaka", "NAPON",0,0));
        }
        this.kompozicija.addAll(lokomotive);
        this.kompozicija.addAll(vagoni);
        //dodaj polje pod naponom na kraj
        if(kompozicijaJeElektricna){
            this.kompozicija.add(new NaponLokomotiva(0,"oznaka","NAPON",0,0));
        }
        Random rand = new Random();
        this.brzinaVoza = brzinaVoza;
        this.pocetnaStanica = pocetnaStanica;
        this.krajnjaStanica = krajnjaStanica;
        this.stanice = Stanica.pribaviStanice(pocetnaStanica,krajnjaStanica);
        this.name = Controller.randomStringGenerator();
        this.stanice.stream().forEach(System.out::println);
    }

    public void setKompozicija(ArrayList<Object> kompozicija) {
        this.kompozicija = kompozicija;
    }

    public void setBrzinaVoza(int brzinaVoza) {
        this.brzinaVoza = brzinaVoza;
    }

    public ArrayList<Object> getKompozicija() {
        return kompozicija;
    }

    public long getBrzinaVoza() {
        return brzinaVoza;
    }

    public void setStanice(ArrayList<String> stanice) {
        this.stanice = stanice;
    }

    public void setPocetnaStanica(String pocetnaStanica) {
        this.pocetnaStanica = pocetnaStanica;
    }

    public String getPocetnaStanica() {
        return pocetnaStanica;
    }

    public String getKrajnjaStanica() {
        return krajnjaStanica;
    }

    public void setKrajnjaStanica(String krajnjaStanica) {
        this.krajnjaStanica = krajnjaStanica;
    }

    public ArrayList<String> getStanice() {
        return stanice;
    }

    public int[] detektujSledecePolje() {
        Lokomotiva prvaLokomotiva = (Lokomotiva)kompozicija.get(0);
        int prviX = prvaLokomotiva.getX();
        int prviY = prvaLokomotiva.getY();
        for(Object obj : kompozicija){
            if(obj instanceof Lokomotiva){
                int x = ((Lokomotiva) obj).getX();
                int y = ((Lokomotiva) obj).getY();
                predjenaPolja.add(Main.MAPA.getElementAt(x,y));
            } else if (obj instanceof Vagon) {
                int x = ((Vagon) obj).getX();
                int y = ((Vagon) obj).getY();
                predjenaPolja.add(Main.MAPA.getElementAt(x,y));
            }
        }
        int sledecePolje[] = new int[2];

        //PROVJERA ZA POLJA U 4 PRAVCA DA LI IMAJU PRUGU ILI PRUZNI PRELAZ
        if((Main.MAPA.getElementAt(prviX+1,prviY).elementImaPrugu() || Main.MAPA.getElementAt(prviX+1,prviY).elementImaPruzniPrelaz() )
                && !predjenaPolja.contains(Main.MAPA.getElementAt(prviX+1,prviY))){
            if(Main.MAPA.getElementAt(prviX+1,prviY).elementImaStanicu()){
                vozNaUlazu = true;
            }
            sledecePolje[0] = prviX+1;
            sledecePolje[1] = prviY;
        }
        else if((Main.MAPA.getElementAt(prviX-1,prviY).elementImaPrugu() || Main.MAPA.getElementAt(prviX-1,prviY).elementImaPruzniPrelaz())
                && !predjenaPolja.contains(Main.MAPA.getElementAt(prviX-1,prviY))){
            sledecePolje[0] = prviX-1;
            sledecePolje[1] = prviY;
        }
        else if((Main.MAPA.getElementAt(prviX,prviY+1).elementImaPrugu() || Main.MAPA.getElementAt(prviX,prviY+1).elementImaPruzniPrelaz())
                && !predjenaPolja.contains(Main.MAPA.getElementAt(prviX,prviY+1))){
            sledecePolje[0] = prviX;
            sledecePolje[1] = prviY+1;
        }
        else if((Main.MAPA.getElementAt(prviX,prviY-1).elementImaPrugu() || Main.MAPA.getElementAt(prviX,prviY-1).elementImaPruzniPrelaz())
                && !predjenaPolja.contains(Main.MAPA.getElementAt(prviX,prviY-1))){
            sledecePolje[0] = prviX;
            sledecePolje[1] = prviY-1;
        }
        //DA LI JE SLEDECE POLJE STANICA
        else if(Main.MAPA.getElementAt(prviX+1,prviY).elementImaStanicu()){
            sledecePolje[0] = prviX+1;
            sledecePolje[1] = prviY;
            vozNaUlazu = true;
        }
        else if(Main.MAPA.getElementAt(prviX-1,prviY).elementImaStanicu()){
            sledecePolje[0] = prviX-1;
            sledecePolje[1] = prviY;
            vozNaUlazu = true;
        }
        else if(Main.MAPA.getElementAt(prviX,prviY+1).elementImaStanicu()){
            sledecePolje[0] = prviX;
            sledecePolje[1] = prviY+1;
            vozNaUlazu = true;
        }
        else if(Main.MAPA.getElementAt(prviX,prviY-1).elementImaStanicu()){
            sledecePolje[0] = prviX;
            sledecePolje[1] = prviY-1;
            vozNaUlazu = true;
        }
        return sledecePolje;
    }

    public void pomjeriKompoziciju(int duzinaKompozicije) {
        int noveKoordinate[][] = new int[duzinaKompozicije][2];
        noveKoordinate[0][0] = detektujSledecePolje()[0];
        noveKoordinate[0][1] = detektujSledecePolje()[1];
        istorijaKretanja.dodajNovuKoordinatu(new int[]{noveKoordinate[0][0],noveKoordinate[0][1]});
        skiniKompozicijuSaMape(duzinaKompozicije); //skini kompoziciju
        //zamjeni stare koordinate
        for (int i = 0; i < duzinaKompozicije - 1; i++) {
            if(vozNaUlazu) {
                break;
            }
            if (kompozicija.get(i) instanceof Lokomotiva) {
                noveKoordinate[i + 1][0] = ((Lokomotiva) kompozicija.get(i)).getX();
                noveKoordinate[i + 1][1] = ((Lokomotiva) kompozicija.get(i)).getY();
            } else if (kompozicija.get(i) instanceof Vagon) {
                noveKoordinate[i + 1][0] = ((Vagon) kompozicija.get(i)).getX();
                noveKoordinate[i + 1][1] = ((Vagon) kompozicija.get(i)).getY();
            }
        }
        for (int i = 0; i < duzinaKompozicije; i++) {
            if(vozNaUlazu) {
                break;
            }
            if (kompozicija.get(i) instanceof Lokomotiva) {
                ((Lokomotiva) kompozicija.get(i)).setX(noveKoordinate[i][0]);
                ((Lokomotiva) kompozicija.get(i)).setY(noveKoordinate[i][1]);
            } else if (kompozicija.get(i) instanceof Vagon) {
                ((Vagon) kompozicija.get(i)).setX(noveKoordinate[i][0]);
                ((Vagon) kompozicija.get(i)).setY(noveKoordinate[i][1]);
            }
        }
        //dodaj koordinate zadnjeg elementa u arrayListu
        if(kompozicija.size() == duzinaKompozicije){
            dodajZadnjiElementUListu();
        }
        //postavi kompoziciju
        postaviKompozicijuNaMapu(duzinaKompozicije);
    }

    public void dodajZadnjiElementUListu(){

            int length = kompozicija.size();
            Object zadnjiElement = kompozicija.get(length - 1);

            if(zadnjiElement instanceof Lokomotiva){
                int zadnjiElementX = ((Lokomotiva) zadnjiElement).getX();
                int zadnjiElementY = ((Lokomotiva) zadnjiElement).getY();

                Element pozicijaZadnjegElementa = Main.MAPA.getElementAt(zadnjiElementX,zadnjiElementY);
                predjenaPoljaZadnjegElementa.add(pozicijaZadnjegElementa);
            }
            else  if(zadnjiElement instanceof Vagon){
                int zadnjiElementX = ((Vagon) zadnjiElement).getX();
                int zadnjiElementY = ((Vagon) zadnjiElement).getY();

                Element pozicijaZadnjegElementa = Main.MAPA.getElementAt(zadnjiElementX,zadnjiElementY);
                predjenaPoljaZadnjegElementa.add(pozicijaZadnjegElementa);
            }
    }
    ///provjeri da li je kompozicija presla pruzni prelaz
    public synchronized boolean kompozicijaPreslaPrelaz(String trenutnaStanica,String sledecaStanica){
        int[] koordinatePruznogPrelaza =  new int[4];

        //nemaju svi segmenti pruzni prelaz
        if(Mapa.koordinatePruznogPrelaza(trenutnaStanica,sledecaStanica) == null){
            return false;
        }
        else{
            koordinatePruznogPrelaza = Mapa.koordinatePruznogPrelaza(trenutnaStanica,sledecaStanica);
        }
        int x1 = koordinatePruznogPrelaza[0];
        int y1 = koordinatePruznogPrelaza[1];
        int x2 = koordinatePruznogPrelaza[2];
        int y2 = koordinatePruznogPrelaza[3];

        Element prelaz1 = Main.MAPA.getElementAt(x1,y1);
        Element prelaz2 = Main.MAPA.getElementAt(x2,y2);

        if(predjenaPoljaZadnjegElementa.contains(prelaz1) && predjenaPoljaZadnjegElementa.contains(prelaz2)){
            return true;
        }
        return false;

    }

    public void postaviKompozicijuNaMapu(int duzinaKompozicije) {
        for(int i = 0 ; i < duzinaKompozicije ; i++){
            Object obj  = kompozicija.get(i);
            if(obj instanceof Lokomotiva){
                int x = ((Lokomotiva) obj).getX();
                int y = ((Lokomotiva) obj).getY();
                Main.MAPA.setElementAt(x, y, obj);
            }
            else if(obj instanceof Vagon){
                int x = ((Vagon) obj).getX();
                int y = ((Vagon) obj).getY();
                Main.MAPA.setElementAt(x, y, obj);
            }
        }
    }

    public void skiniKompozicijuSaMape(int duzinaKompozicije) {
        for(int i = 0; i < duzinaKompozicije; i++) {
            Object obj  = kompozicija.get(i);
            if(obj instanceof Lokomotiva){
                int x = ((Lokomotiva) obj).getX();
                int y = ((Lokomotiva) obj).getY();
                Main.MAPA.getElementAt(x,y).ukloniObjekatSaListe(obj);
            }
            else if(obj instanceof Vagon){
                int x = ((Vagon) obj).getX();
                int y = ((Vagon) obj).getY();
                Main.MAPA.getElementAt(x,y).ukloniObjekatSaListe(obj);
            }
        }
    }

    public void ispisiKompoziciju(){
        kompozicija.stream().forEach(System.out::println);
    }

    public int[][] trenutneKoordinateVoza() {
        int[][] koordinateKompozicije = new int[2][kompozicija.size() + 2];
        int x = ((Lokomotiva) kompozicija.get(0)).getX();
        int y = ((Lokomotiva) kompozicija.get(0)).getY();
        for (int i = 0; i < kompozicija.size(); i++) {
            Object ob = kompozicija.get(i);
            if (ob instanceof Lokomotiva) {
                koordinateKompozicije[0][i] = ((Lokomotiva) ob).getX();
                koordinateKompozicije[1][i] = ((Lokomotiva) ob).getY();
            } else if (ob instanceof Vagon) {
                koordinateKompozicije[0][i] = ((Vagon) ob).getX();
                koordinateKompozicije[1][i] = ((Vagon) ob).getY();
            }
        }
        return koordinateKompozicije;
    }

    public void udjiNaStanicu(String trenutnaStanica,String sledecaStanica,long brzinaNaUlazu) {
        int[][] koordinateKompozicije = new int[2][kompozicija.size() + 2];
        int x = ((Lokomotiva) kompozicija.get(0)).getX();
        int y = ((Lokomotiva) kompozicija.get(0)).getY();
        for (int i = 0; i < kompozicija.size(); i++) {
            Object ob = kompozicija.get(i);
            if (ob instanceof Lokomotiva) {
                koordinateKompozicije[0][i] = ((Lokomotiva) ob).getX();
                koordinateKompozicije[1][i] = ((Lokomotiva) ob).getY();
            } else if (ob instanceof Vagon) {
                koordinateKompozicije[0][i] = ((Vagon) ob).getX();
                koordinateKompozicije[1][i] = ((Vagon) ob).getY();
            }
        }
        for (int i = 0; i < kompozicija.size(); i++) {
            int counter = 0;
            // ispisiKompoziciju();
            skiniKompozicijuSaMape(kompozicija.size());
            synchronized (Mapa.mapLock){
                for (int j = i + 1; j < kompozicija.size(); j++) {
                    Object ob = kompozicija.get(j);
                    if (ob instanceof Lokomotiva) {
                        ((Lokomotiva) ob).setX(koordinateKompozicije[0][counter]);
                        ((Lokomotiva) ob).setY(koordinateKompozicije[1][counter]);
                        Main.MAPA.setElementAt(koordinateKompozicije[0][counter],
                                koordinateKompozicije[1][counter], ob);
                        counter++;
                    } else if (ob instanceof Vagon) {
                        ((Vagon) ob).setX(koordinateKompozicije[0][counter]);
                        ((Vagon) ob).setY(koordinateKompozicije[1][counter]);
                        Main.MAPA.setElementAt(koordinateKompozicije[0][counter],
                                koordinateKompozicije[1][counter], ob);
                        counter++;
                    }
                }
                dodajZadnjiElementUListu();
                if(kompozicijaPreslaPrelaz(trenutnaStanica,sledecaStanica) && Stanica.brojKompozicijaNaSegmentu(trenutnaStanica,sledecaStanica) == 1) {
                 Mapa.otkljucajPrelaz(trenutnaStanica, sledecaStanica);
                }
            }
            try {
                Thread.sleep(brzinaNaUlazu);
                Thread.yield();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
            }
        }
    }

    @Override
    public void run() {

        long pocetnoVrijeme = 0;
        int brojStanica = stanice.size();
        String trenutnaStanica = "";
        String sledecaStanica = "";
        int x = 0;
        int y = 0;
        int counter = 0;//brojac koji prebacuje na sledecu stanicu
        while (brojStanica > 1) {
            trenutnaStanica = stanice.get(counter);
            sledecaStanica = stanice.get(counter + 1);
            synchronized (Mapa.mapLock) {
                try {
                    boolean segmentJeZauzet = Stanica.smjerZauzet(trenutnaStanica, sledecaStanica);
                    if (segmentJeZauzet) {
                        Mapa.mapLock.wait();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                }
            }
            if (Stanica.suprotanSmjerZauzet(trenutnaStanica, sledecaStanica) || Stanica.segmentJeSlobodan(trenutnaStanica, sledecaStanica)) {
                long brzina = brzinaVoza;
                synchronized (Mapa.mapLock) {
                    //ako postoji vise kompozicija na segmentu u istom smjeru uskladiti njihove brzine
                    if (Stanica.suprotanSmjerZauzet(trenutnaStanica,sledecaStanica)) {
                        brzina = privremenaBrzina;
                    }
                    Stanica.zauzmiSuprotanSmjer(trenutnaStanica, sledecaStanica);
                    Mapa.zakljucajPrelaz(trenutnaStanica, sledecaStanica);
                    x = Stanica.pronadjiIzlazneKoordinate(trenutnaStanica, sledecaStanica)[0];
                    y = Stanica.pronadjiIzlazneKoordinate(trenutnaStanica, sledecaStanica)[1];
                    //ako je pocetak zauzet cekaj
                    if (Main.MAPA.getElementAt(x, y).isZakljucanoPolje()) {
                        continue;
                    } else {
                        //ako je pocetak slobodan zakljucaj
                        Main.MAPA.getElementAt(x, y).setZakljucanoPolje(true);
                        Stanica.inkrementujBrojKompozicija(trenutnaStanica, sledecaStanica);
                        pocetnoVrijeme = new Date().getTime();
                        if (Stanica.brojKompozicijaNaSegmentu(trenutnaStanica, sledecaStanica) > 1) {
                            try {
                                Thread.sleep(privremenaBrzina);
                                Thread.yield();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                            }
                        }
                    }
                    //postavljanje prve pozicije na izlazu iz stanice
                    ((Lokomotiva) kompozicija.get(0)).setX(x);
                    ((Lokomotiva) kompozicija.get(0)).setY(y);
                    istorijaKretanja.dodajNovuKoordinatu(new int[]{x, y});
                    postaviKompozicijuNaMapu(1);
                }
                try {
                    if(Stanica.brojKompozicijaNaSegmentu(trenutnaStanica,sledecaStanica) > 1){
                        Thread.sleep(1500);
                        Thread.yield();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                }
                //izlaz iz stanice
                for (int i = 2; i < kompozicija.size() + 1; i++) {
                    synchronized (Mapa.mapLock) {
                        pomjeriKompoziciju(i);
                    }
                    try {
                        Thread.sleep(brzina);
                        Thread.yield();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                    }
                }
                //otkljucaj prvo polje
                Main.MAPA.getElementAt(x, y).setZakljucanoPolje(false);

                //put do sledece stanice
                while (!vozNaUlazu) {
                    synchronized (Mapa.mapLock) {
                        pomjeriKompoziciju(kompozicija.size());
                        //ako je kompozicija presla prelaz i ako je jedina na segmentu onda otkljucaj prelaz
                        if (kompozicijaPreslaPrelaz(trenutnaStanica, sledecaStanica) &&
                                Stanica.brojKompozicijaNaSegmentu(trenutnaStanica, sledecaStanica) == 1) {
                            Mapa.otkljucajPrelaz(trenutnaStanica, sledecaStanica);
                        }
                    }
                    try {
                        Thread.sleep(brzina);
                        Thread.yield();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                    }
                }
                //ulaz u stanicu
                if (vozNaUlazu) {
                    udjiNaStanicu(trenutnaStanica, sledecaStanica, brzina);
                    vozNaUlazu = false;
                    brojStanica--;
                    counter++;
                    //smanji broj kompozicija na segmentu
                    Stanica.dekrementujBrojKompozicija(trenutnaStanica, sledecaStanica);
                    //ako je broj kompozicija nula oslobodi segment u oba pravca
                    System.out.println(Stanica.brojKompozicijaNaSegmentu(trenutnaStanica, sledecaStanica));
                    if (Stanica.brojKompozicijaNaSegmentu(trenutnaStanica, sledecaStanica) == 0) {
                        Stanica.oslobodiSuprotanSmjer(trenutnaStanica, sledecaStanica);
                    }
                    synchronized (Mapa.mapLock) {
                        try {
                            Mapa.mapLock.notify();
                        } catch (IllegalMonitorStateException ex) {
                            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                        }
                    }
                }
            }
        }
        //dodaj vrijeme kretanja
        istorijaKretanja.setVrijemeTrajanja((new Date().getTime() - pocetnoVrijeme) / 1000);
        istorijaKretanja.setNaziv(name);
        //serijalizacija istorije kretanja
        serijalizujIstorijuKretanja(istorijaKretanja);
    }
    public void serijalizujIstorijuKretanja(IstorijaKretanja istorijaKretanja){
        File path = new File(Controller.KRETANJE_PATH);

        try{
            FileOutputStream file = new FileOutputStream(Controller.KRETANJE_PATH + this.name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(istorijaKretanja);
            out.close();
            file.close();
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }
}