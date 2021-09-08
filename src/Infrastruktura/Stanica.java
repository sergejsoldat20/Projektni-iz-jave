package Infrastruktura;

import Mapa.Element;
import Mapa.Mapa;
import sample.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stanica extends Polje{

    private String nazivStanice;
    private int x;
    private int y;
    public static HashMap<String,Boolean> zauzetiSegmenti = new HashMap<>();//AKO JE True ZAUZETO JE

    public static HashMap<String,int[]> pocetnePozicije = new HashMap<>();
    public static String POCETNE_POZICIJE_PATH = null;
    public static String PUTANJE_VOZOVA_PATH = null;
    public static String PRUZNI_PRELAZI_PATH = null;

    public static HashMap<String,Integer> brojKompozicijaPoSegmentu = new HashMap<>();


    public Stanica(String nazivStanice, int x,int  y){
        this.nazivStanice = nazivStanice;
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getNazivStanice() {
        return nazivStanice;
    }

    public static ArrayList<String> pribaviStanice(String pocetnaStanica, String krajnjaStanica){
        String[] svePutanje = new String[20];
        File file = new File(PUTANJE_VOZOVA_PATH);
        List<String> putanjaVoza = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = "";
            int counter = 0;
            while((line = in.readLine())!=null){
                svePutanje[counter++] = line;
            }
            in.close();
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        for(int i = 0 ; i < svePutanje.length ; i++){
            if(svePutanje[i].startsWith(pocetnaStanica) && svePutanje[i].endsWith(krajnjaStanica)){
               String [] rezultat = svePutanje[i].split(",");
               putanjaVoza = Arrays.asList(rezultat);
            }
        }
        return new ArrayList<String>(putanjaVoza);
    }


    public static void pribaviPocetnePozicije(){
        File path = new File(POCETNE_POZICIJE_PATH);
        System.out.println(POCETNE_POZICIJE_PATH);
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = in.readLine()) != null){

                String[] parsedLine = line.split(",");
                String key = parsedLine[0]+parsedLine[1];
                int[] koordinate = new int[2];
                koordinate[0] = Integer.parseInt(parsedLine[2]);
                koordinate[1] = Integer.parseInt(parsedLine[3]);
                pocetnePozicije.put(key,koordinate);
            }
            in.close();
        }catch(IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }

    public static int[] pronadjiIzlazneKoordinate(String trenutnaStanica,String sledecaStanica){
        String key = trenutnaStanica + sledecaStanica;
        return pocetnePozicije.get(key);
    }

    public static synchronized void oslobodiSegment(String trenutnaStanica,String sledecaStanica){
       String key1 = trenutnaStanica + sledecaStanica;
       String key2 = sledecaStanica + trenutnaStanica;

       if(zauzetiSegmenti.get(key1) != null){
           zauzetiSegmenti.put(key1,false);
       }
       else if(zauzetiSegmenti.get(key2) != null){
           zauzetiSegmenti.put(key2,false);
       }
    }

    public static void inicijalizujZauzeteSegmente(){
        zauzetiSegmenti.put("AB",false);
        zauzetiSegmenti.put("BC",false);
        zauzetiSegmenti.put("CD",false);
        zauzetiSegmenti.put("CE",false);

        zauzetiSegmenti.put("BA",false);
        zauzetiSegmenti.put("CB",false);
        zauzetiSegmenti.put("DC",false);
        zauzetiSegmenti.put("EC",false);


    }
    public static void postaviBrojaceKompozicija(){
        brojKompozicijaPoSegmentu.put("AB",0);
        brojKompozicijaPoSegmentu.put("BC",0);
        brojKompozicijaPoSegmentu.put("CD",0);
        brojKompozicijaPoSegmentu.put("CE",0);
    }
    public static synchronized void inkrementujBrojKompozicija(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(brojKompozicijaPoSegmentu.get(key1) != null){
            int counter = brojKompozicijaPoSegmentu.get(key1);
            counter++;
            brojKompozicijaPoSegmentu.put(key1,counter);
        }
        else if(brojKompozicijaPoSegmentu.get(key2) != null){
            int counter = brojKompozicijaPoSegmentu.get(key2);
            counter++;
            brojKompozicijaPoSegmentu.put(key2,counter);
        }
    }
    public static synchronized void dekrementujBrojKompozicija(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(brojKompozicijaPoSegmentu.get(key1) != null){
            int counter = brojKompozicijaPoSegmentu.get(key1);
            counter--;
            brojKompozicijaPoSegmentu.put(key1,counter);
        }
        else if(brojKompozicijaPoSegmentu.get(key2) != null){
            int counter = brojKompozicijaPoSegmentu.get(key2);
            counter--;
            brojKompozicijaPoSegmentu.put(key2,counter);
        }
    }
    public static synchronized int brojKompozicijaNaSegmentu(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(brojKompozicijaPoSegmentu.get(key1) != null){
            return brojKompozicijaPoSegmentu.get(key1);

        }
        else if(brojKompozicijaPoSegmentu.get(key2) != null){
            return brojKompozicijaPoSegmentu.get(key2);

        }
        return 0;
    }

    //vratice true ako je zauzet,ako je slobodan vraca false
    public static synchronized boolean suprotanSmjerZauzet(String trenutnaStanica,String sledecaStanica){
       String key = sledecaStanica + trenutnaStanica;
       return zauzetiSegmenti.get(key);
    }
    //zauzima suprtan smjer
    public static synchronized void zauzmiSuprotanSmjer(String trenutnaStanica,String sledecaStanica){
        String key = sledecaStanica + trenutnaStanica;
        zauzetiSegmenti.put(key,true);
    }
    //provjerava da li je smjer zauzet
    public static synchronized boolean smjerZauzet(String trenutnaStanica,String sledecaStanica){
        String key = trenutnaStanica + sledecaStanica;
        return zauzetiSegmenti.get(key);
    }

    //oslobadja suprotan smjer
    public static synchronized void oslobodiSuprotanSmjer(String trenutnaStanica,String sledecaStanica){
        String key = sledecaStanica + trenutnaStanica;
        zauzetiSegmenti.put(key,false);
    }

    //provjerava da li su obe strane slobodne ako jesu true
    public static synchronized boolean segmentJeSlobodan(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;

        if(!zauzetiSegmenti.get(key1) && !zauzetiSegmenti.get(key2)){
            return true;
        }
        return false;
    }
















}
