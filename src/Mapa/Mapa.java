package Mapa;

import Infrastruktura.*;
import sample.Controller;
import sample.Main;

import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.Position;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mapa {
    public static final Integer BROJ_KOLONA_MAPE = 30;
    public static final Integer BROJ_VRSTA_MAPE = 30;
    public static  String MAPA_PATH = null;
    public static HashMap<String,int[]> pruzniPrelazi = new HashMap<>();
    public static HashMap<String,ArrayList<int[]>> putevi = new HashMap<>();
    public static Object mapLock = new Object();
    public static String[][] pomocnaMapa = new String[BROJ_VRSTA_MAPE][BROJ_KOLONA_MAPE];
    private static Element mapa[][];
    public static String [] stranPuteva = {"PUT1_L","PUT1_D","PUT2_L","PUT2_D","PUT3_L","PUT3_D"};
    public static String [] segmentiSaPrelazima = {"AB","BA","BC","CB","CE","EC"};

    public Mapa(){
        super();
        mapa = new Element[BROJ_VRSTA_MAPE][BROJ_KOLONA_MAPE];
        for(int i = 0 ; i < BROJ_VRSTA_MAPE ; i++){
            for(int j = 0 ; j < BROJ_KOLONA_MAPE ; j++){
                mapa[i][j] = new Element(i,j);
            }
        }
    }
    public Element getElementAt(int x,int y){
        return mapa[x][y];
    }
    public void setElementAt(int x,int y,Object obj){
        mapa[x][y].dodajObjekatUListu(obj);
    }

    public void ucitajMapu(){

        File path = new File(MAPA_PATH);
        String[][] mapaStringova = new String[BROJ_VRSTA_MAPE][BROJ_KOLONA_MAPE];
        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line = "";
            int counter = 0;
            while((line = in.readLine())!=null){
                mapaStringova[counter] = line.split(" ");
                counter++;
            }
            pomocnaMapa = mapaStringova;

            for(int i = 0 ; i < BROJ_VRSTA_MAPE ; i++){
                for(int j = 0 ; j < BROJ_KOLONA_MAPE ; j++){
                    if("1".equals(mapaStringova[i][j])){
                        mapa[i][j].dodajObjekatUListu(new Pruga(i,j));
                    }
                    else if("2".equals(mapaStringova[i][j])){
                        mapa[i][j].dodajObjekatUListu(new Put(i,j," "));
                    }
                    else if ("p".equals(mapaStringova[i][j])){
                       mapa[i][j].dodajObjekatUListu(new PruzniPrelaz(i,j));
                    }
                    else if("s".equals(mapaStringova[i][j])){
                        if(i == 27 && j == 2 ) {
                            mapa[i][j].dodajObjekatUListu(new Stanica("A", i, j));
                        }
                        else if(i == 6 && j == 6){
                            mapa[i][j].dodajObjekatUListu(new Stanica("B", i, j));
                        }
                        else if(i == 1 && j == 26){
                            mapa[i][j].dodajObjekatUListu(new Stanica("D", i, j));
                        }
                        else if(i == 12 && j == 20){
                            mapa[i][j].dodajObjekatUListu(new Stanica("C", i, j));
                        }
                        else {
                            mapa[i][j].dodajObjekatUListu(new Stanica("E", i, j));
                        }
                    }
                    else{
                        mapa[i][j].dodajObjekatUListu(new Polje());
                    }
                }
            }
        } catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }

    public void ispisiMapu() {
        for (int i = 0; i < BROJ_VRSTA_MAPE; i++) {
            System.out.println("\n");
            for (int j = 0; j < BROJ_KOLONA_MAPE; j++) {
                ArrayList<Object> lista = mapa[i][j].getObjektiNaPolju();
                for (Object ob : lista) {
                    if (ob instanceof Pruga) {
                        System.out.print("1 ");
                    } else if (ob instanceof Put) {
                        System.out.print("2 ");
                    } else if (ob instanceof PruzniPrelaz) {
                        System.out.print("P ");
                    } else if (ob instanceof Stanica) {
                        System.out.print("S ");
                    } else {
                        System.out.print("0 ");
                    }
                }
            }
        }
    }
    public static int[] koordinateStanice(String imeStanice){
        int[] koordinate = new int[2];

        for(int i = 0 ; i <  BROJ_VRSTA_MAPE ; i++){
            for(int j = 0 ; j < BROJ_KOLONA_MAPE ; j++){
               if(Main.MAPA.getElementAt(i,j).elementImaStanicu()){
                   Stanica stanica = (Stanica) Main.MAPA.getElementAt(i,j).getObjektiNaPolju().get(0);
                   if(stanica.getNazivStanice().equals(imeStanice)){
                       koordinate[0] = i;
                       koordinate[1] = j;
                   }
               }
            }
        }
        return koordinate;
    }

    public static void unesiPruznePrelaze(){
       File path = new File(Stanica.PRUZNI_PRELAZI_PATH);
       try{
           BufferedReader in = new BufferedReader(new FileReader(path));
           String line = "";
           while((line = in.readLine())!=null){
               String[] array = line.split("#");
               int[] koordinate = new int[4];
               koordinate[0] = Integer.parseInt(array[1]);
               koordinate[1] = Integer.parseInt(array[2]);
               koordinate[2] = Integer.parseInt(array[3]);
               koordinate[3] = Integer.parseInt(array[4]);
               pruzniPrelazi.put(array[0],koordinate);
           }
           in.close();
       }catch (IOException ex){
           Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
       }
       Iterator<String> iterator = pruzniPrelazi.keySet().iterator();
       Iterator<int[]> iterator1 = pruzniPrelazi.values().iterator();
       while(iterator.hasNext() && iterator1.hasNext()){
           String line = iterator.next() + Arrays.toString(iterator1.next());
           System.out.println(line);
       }
    }
    public static synchronized void zakljucajPrelaz(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(pruzniPrelazi.containsKey(key1)){
            int x1 = pruzniPrelazi.get(key1)[0];
            int y1 = pruzniPrelazi.get(key1)[1];
            int x2 = pruzniPrelazi.get(key1)[2];
            int y2 = pruzniPrelazi.get(key1)[3];
            Main.MAPA.getElementAt(x1,y1).getPruzniPrelaz().setZauzetPrelaz(true);
            Main.MAPA.getElementAt(x2,y2).getPruzniPrelaz().setZauzetPrelaz(true);
        }
        else  if(pruzniPrelazi.containsKey(key2)){
            int x1 = pruzniPrelazi.get(key2)[0];
            int y1 = pruzniPrelazi.get(key2)[1];
            int x2 = pruzniPrelazi.get(key2)[2];
            int y2 = pruzniPrelazi.get(key2)[3];
            Main.MAPA.getElementAt(x1,y1).getPruzniPrelaz().setZauzetPrelaz(true);
            Main.MAPA.getElementAt(x2,y2).getPruzniPrelaz().setZauzetPrelaz(true);
        }
    }

    public static synchronized void otkljucajPrelaz(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(pruzniPrelazi.containsKey(key1)){
            int x1 = pruzniPrelazi.get(key1)[0];
            int y1 = pruzniPrelazi.get(key1)[1];
            int x2 = pruzniPrelazi.get(key1)[2];
            int y2 = pruzniPrelazi.get(key1)[3];
            Main.MAPA.getElementAt(x1,y1).getPruzniPrelaz().setZauzetPrelaz(false);
            Main.MAPA.getElementAt(x2,y2).getPruzniPrelaz().setZauzetPrelaz(false);

        }
        else  if(pruzniPrelazi.containsKey(key2)){
            int x1 = pruzniPrelazi.get(key2)[0];
            int y1 = pruzniPrelazi.get(key2)[1];
            int x2 = pruzniPrelazi.get(key2)[2];
            int y2 = pruzniPrelazi.get(key2)[3];
            Main.MAPA.getElementAt(x1,y1).getPruzniPrelaz().setZauzetPrelaz(false);
            Main.MAPA.getElementAt(x2,y2).getPruzniPrelaz().setZauzetPrelaz(false);
        }
    }

    public static synchronized boolean prelazJeZauzet(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(pruzniPrelazi.containsKey(key1)) {
            int x1 = pruzniPrelazi.get(key1)[0];
            int y1 = pruzniPrelazi.get(key1)[1];
            int x2 = pruzniPrelazi.get(key1)[2];
            int y2 = pruzniPrelazi.get(key1)[3];
            if (Main.MAPA.getElementAt(x1, y1).getPruzniPrelaz().isZauzetPrelaz() &&
                    Main.MAPA.getElementAt(x2, y2).getPruzniPrelaz().isZauzetPrelaz()) {
                return true;
            }
        }
        else  if(pruzniPrelazi.containsKey(key2)){
            int x1 = pruzniPrelazi.get(key2)[0];
            int y1 = pruzniPrelazi.get(key2)[1];
            int x2 = pruzniPrelazi.get(key2)[2];
            int y2 = pruzniPrelazi.get(key2)[3];
            if (Main.MAPA.getElementAt(x1, y1).getPruzniPrelaz().isZauzetPrelaz() &&
                    Main.MAPA.getElementAt(x2, y2).getPruzniPrelaz().isZauzetPrelaz()) {
                return true;

            }
        }
        return false;
    }

    public static int[] koordinatePruznogPrelaza(String trenutnaStanica,String sledecaStanica){
        String key1 = trenutnaStanica + sledecaStanica;
        String key2 = sledecaStanica + trenutnaStanica;
        if(pruzniPrelazi.containsKey(key1)){
            return pruzniPrelazi.get(key1);
        }
        else{
            return pruzniPrelazi.get(key2);
        }
    }

    public static void ucitajPuteve(){
        File path = new File(Controller.PUT_PATH);
        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line = "";
            while((line = in.readLine()) != null){
                String array[] = line.split("#");
                ArrayList<int []> jednaStranaPuta = new ArrayList<>();
                for(String str : array[1].split(",")){
                    String koordinate[] = str.split(" ");
                     int x = Integer.parseInt(koordinate[1]);
                     int y = Integer.parseInt(koordinate[0]);
                    jednaStranaPuta.add(new int[]{x,y});
                }
                putevi.put(array[0],jednaStranaPuta);
            }
        } catch (Exception ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }
}
