package sample;

import FileWatcheri.ConfigurationFileWatcher;
import FileWatcheri.FileWatcher;
import Infrastruktura.*;
import IstorijaKretanja.IstorijaKretanja;
import Izuzeci.PogresnaKompozicijaException;
import Lokomotive.*;
import Vagoni.*;
import Vozila.Automobil;
import Vozila.Kamion;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.ImageStorage;
import com.sun.jdi.event.ExceptionEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import Mapa.Mapa;
import Images.Images;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Controller {

    public static final String KONFIGURACIONI_FAJL = "src" + File.separator + "Config" + File.separator + "konfiguracioni fajl.txt";
    public static ArrayList<String> kompozicije = new ArrayList<>();
    public static ArrayList<SekcijaPuta> putevi = new ArrayList<>();
    public static final String ELEKTRICNI_POGON = "ELEKTRICNI";
    public static final String DIZELSKI_POGON = "DIZELSKI";
    public static final String PARNI_POGON = "PARNI";
    public static final String[] LISTA_PUTEVA = {"PUT1","PUT2","PUT3"};
    public static final String[] MARKE_VOZILA = {"Mercedes","Red Bull","McLaren","Ferrari","Williams"};
    public static final String POGRESNA_KOMPOZICIJA_PORUKA_1 = "Lokomotive moraju biti istog tipa";
    public static final String POGRESNA_KOMPOZICIJA_PORUKA_2 = "Lokomotive i vagoni moraju biti istog tipa";
    public static final String POGRESNA_KOMPOZICIJA_PORUKA_3 = "Unesena brzina mora biti manja od 500";
    public static String KOMPOZICIJE_PATH = null;
    public static String KRETANJE_PATH = null;
    public static String PUT_PATH = null;
    public static Long PUT1_BRZINA = null;
    public static Long PUT2_BRZINA = null;
    public static Long PUT3_BRZINA = null;
    @FXML
    AnchorPane ap;
    @FXML
    GridPane gp;
    @FXML
    Button button1;
    @FXML
    Button button2;
    @FXML
    Button button3;
    public static boolean refresujMapu = true;
    public static final double FIELD_HEIGHT = 19.3548;
    public static final double FIELD_WIDTH = 19.3548;
    public static final double STATION_WIDTH = 40.0;
    public static final double STATION_HEIGHT = 40.0;

    @FXML
    public void initialize() {

        gp.setGridLinesVisible(true);
        podesiKonfiguraciju();
        podesiKonfiguracijuPuteva();
        Main.MAPA.ucitajMapu();
        Stanica.pribaviPocetnePozicije();
        Stanica.inicijalizujZauzeteSegmente();
        Mapa.unesiPruznePrelaze();
        Mapa.ucitajPuteve();
        Stanica.postaviBrojaceKompozicija();
        for (int i = 0; i < Mapa.BROJ_KOLONA_MAPE; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            gp.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < Mapa.BROJ_VRSTA_MAPE; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            gp.getRowConstraints().add(rowConstraints);
        }
        for (int i = 0; i < Mapa.BROJ_VRSTA_MAPE; i++) {

            for (int j = 0; j < Mapa.BROJ_KOLONA_MAPE; j++) {
                ArrayList<Object> lista = Main.MAPA.getElementAt(i, j).getObjektiNaPolju();
                for (Object ob : lista) {
                    if(ob instanceof Pruga){
                        try {
                            Image image = new Image(new FileInputStream(new File(Images.SLIKA_PRUGA)));
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(FIELD_WIDTH);
                            imageView.setFitHeight(FIELD_HEIGHT);
                            gp.add(new TilePane(imageView), j, i);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                        }
                    }
                    else if (ob instanceof Put) {
                        try {
                            Image image = new Image(new FileInputStream(new File(Images.SLIKA_PUT)));
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(FIELD_WIDTH);
                            imageView.setFitHeight(FIELD_HEIGHT);
                            gp.add(new TilePane(imageView), j, i);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                        }

                    } else if (ob instanceof PruzniPrelaz) {
                        try {
                            Image image = new Image(new FileInputStream(new File(Images.SLIKA_PRUZNI_PRELAZ)));
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(FIELD_WIDTH);
                            imageView.setFitHeight(FIELD_HEIGHT);
                            System.out.println(gp.getPrefWidth()/(double)15);
                            gp.add(new TilePane(imageView), j, i);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                        }
                    }
                }
            }
        }
        try {
            Image image = new Image(new FileInputStream(new File(Images.SLIKA_STANICA)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(STATION_HEIGHT);
            imageView.setFitWidth(STATION_WIDTH);
            gp.add(new TilePane(imageView),2,27,3,2);
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        try {
            Image image = new Image(new FileInputStream(new File(Images.SLIKA_STANICA)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(STATION_HEIGHT);
            imageView.setFitWidth(STATION_WIDTH);
            gp.add(new TilePane(imageView),6,6,3,2);
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        try {
            Image image = new Image(new FileInputStream(new File(Images.SLIKA_STANICA)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(STATION_HEIGHT);
            imageView.setFitWidth(STATION_WIDTH);
            gp.add(new TilePane(imageView),19,12,3,2);
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        try {
            Image image = new Image(new FileInputStream(new File(Images.SLIKA_STANICA)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(STATION_HEIGHT);
            imageView.setFitWidth(STATION_WIDTH);
            gp.add(new TilePane(imageView),26,25,3,2);
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        try {
            Image image = new Image(new FileInputStream(new File(Images.SLIKA_STANICA)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(STATION_HEIGHT);
            imageView.setFitWidth(STATION_WIDTH);
            gp.add(new TilePane(imageView),26,1,3,2);
        } catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }

    @FXML
    void pokreniSimulaciju(ActionEvent event) {

        button1.setDisable(true);
        Runnable mapDrawer = ()->{
            while(refresujMapu){
                List<Node> list= (List)this.gp.getChildren().stream().filter((node)->{
                    return node instanceof ImageView;
                }).collect(Collectors.toList());
                Platform.runLater(()->{
                    this.gp.getChildren().removeAll(list);
                });
                synchronized (Mapa.mapLock){
                    for(int i = 0 ; i < Mapa.BROJ_VRSTA_MAPE ; i++){
                        for(int j = 0 ; j < Mapa.BROJ_KOLONA_MAPE ; j++){
                            final int koordinataX = i;
                            final int koordinataY = j;
                            if(Main.MAPA.getElementAt(i,j).elementImaLokomotivu() || Main.MAPA.getElementAt(i,j).elementImaVagon()) {
                                if(Main.MAPA.getElementAt(i,j).elementImaDizelLokomotivu()){
                                    try {
                                        Image image = new Image(new FileInputStream(new File(Images.SLIKA_VOZ)));
                                        ImageView imageView = new ImageView(image);
                                        imageView.setFitWidth(FIELD_WIDTH);
                                        imageView.setFitHeight(FIELD_HEIGHT);
                                        Platform.runLater(() -> {
                                            this.gp.add(imageView, koordinataY, koordinataX);
                                        });
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                    }
                                }
                                else if(Main.MAPA.getElementAt(i,j).elementImaElektricnuLokomotivu()){
                                    try {
                                        Image image = new Image(new FileInputStream(new File(Images.SLIKA_ELEKTRICNA_LOKOMOTIVA)));
                                        ImageView imageView = new ImageView(image);
                                        imageView.setFitWidth(FIELD_WIDTH);
                                        imageView.setFitHeight(FIELD_HEIGHT);
                                        Platform.runLater(() -> {
                                            this.gp.add(imageView, koordinataY, koordinataX);
                                        });
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                    }
                                }
                                else if(Main.MAPA.getElementAt(i,j).elementImaParnuLokomotivu()){
                                    try {
                                        Image image = new Image(new FileInputStream(new File(Images.SLIKA_PARNA_LOKOMOTIVA)));
                                        ImageView imageView = new ImageView(image);
                                        imageView.setFitWidth(FIELD_WIDTH);
                                        imageView.setFitHeight(FIELD_HEIGHT);
                                        Platform.runLater(() -> {
                                            this.gp.add(imageView, koordinataY, koordinataX);
                                        });
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                    }
                                }
                                else if(Main.MAPA.getElementAt(i,j).elementImaNaponLokomotivu()) {
                                    try {
                                        Image image = new Image(new FileInputStream(new File(Images.POD_NAPONOM)));
                                        ImageView imageView = new ImageView(image);
                                        imageView.setFitWidth(FIELD_WIDTH);
                                        imageView.setFitHeight(FIELD_HEIGHT);
                                        Platform.runLater(() -> {
                                            this.gp.add(imageView, koordinataY, koordinataX);
                                        });
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                    }
                                }
                                else if(Main.MAPA.getElementAt(i,j).elementImaVagon()) {
                                    try {
                                        Image image = new Image(new FileInputStream(new File(Images.SLIKA_VAGON)));
                                        ImageView imageView = new ImageView(image);
                                        imageView.setFitWidth(FIELD_WIDTH);
                                        imageView.setFitHeight(FIELD_HEIGHT);

                                        Platform.runLater(() -> {
                                            this.gp.add(imageView, koordinataY, koordinataX);
                                        });
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                    }
                                }
                            }
                            else if(Main.MAPA.getElementAt(i,j).elementImaAutomobil()){
                                try {
                                    Image image = new Image(new FileInputStream(new File(Images.SLIKA_AUTO)));
                                    ImageView imageView = new ImageView(image);
                                    imageView.setFitWidth(FIELD_WIDTH);
                                    imageView.setFitHeight(FIELD_HEIGHT);
                                    Platform.runLater(() -> {
                                        this.gp.add(imageView, koordinataY, koordinataX);
                                    });
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                }
                            }
                            else if(Main.MAPA.getElementAt(i,j).elementImaKamion()){
                                try {
                                    Image image = new Image(new FileInputStream(new File(Images.SLIKA_KAMION)));
                                    ImageView imageView = new ImageView(image);
                                    imageView.setFitWidth(FIELD_WIDTH);
                                    imageView.setFitHeight(FIELD_HEIGHT);
                                    Platform.runLater(() -> {
                                        this.gp.add(imageView, koordinataY, koordinataX);
                                    });
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                                }
                            }
                        }
                    }
                };
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                }
            }
        };
        Runnable generatorVozila =()->{
            Random rand = new Random();
            while(refresujMapu){
                Iterator<SekcijaPuta> iterator = putevi.iterator();
                while(iterator.hasNext()){
                    SekcijaPuta sekcija = iterator.next();
                    if(sekcija.getBrojKreiranihVozila() != sekcija.getMaksimalanBrojVozila()){
                        int tip_vozila = rand.nextInt(2);
                        int model = rand.nextInt(4);
                        int marka = rand.nextInt(4);
                        int godiste = rand.nextInt(21)+2000;
                        int position = rand.nextInt(2);
                        int maksBrzina = (int) sekcija.getMaksimalnaBrzinaSekcije();
                        long brzinaNaPutu = rand.nextInt(maksBrzina);
                        if(tip_vozila == 0){
                            int brojVrata = rand.nextInt(1)+4;
                            Automobil auto = new Automobil(MARKE_VOZILA[marka],randomStringGenerator(),godiste,brojVrata,0,0);
                            String name = sekcija.getImeSekcije();
                            if(position ==0){
                                name +="_L";
                            }else{
                                name+="_D";
                            }
                            auto.setBrzinaVozila( brzinaNaPutu);
                            ArrayList<int[]> put = new ArrayList<>(Mapa.putevi.get(name));
                            auto.setPut(put);
                            auto.start();
                            //kreiraj auto
                        }else{
                            int nosivost = rand.nextInt(300-50) + 50;
                            Kamion kamion = new Kamion(MARKE_VOZILA[marka],randomStringGenerator(),godiste,nosivost,0,0);
                            String name = sekcija.getImeSekcije();
                            if(position ==0){
                                name +="_L";
                            }else{
                                name+="_D";
                            }
                            kamion.setBrzinaVozila(brzinaNaPutu);
                            ArrayList<int[]> put = new ArrayList<>(Mapa.putevi.get(name));
                            kamion.setPut(put);
                            kamion.start();
                        }
                        sekcija.setBrojKreiranihVozila(sekcija.getBrojKreiranihVozila() +1);
                    }
                }
                try{
                    Thread.sleep(2000L);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            };
        };
        Thread thread = new Thread(mapDrawer);
        Thread thread1 = new Thread(generatorVozila);
        thread.start();
        thread1.start();

        FileWatcher watcher = new FileWatcher();
        ConfigurationFileWatcher watcher1 = new ConfigurationFileWatcher();
        watcher.start();
        watcher1.start();
        File path = new File(KOMPOZICIJE_PATH);
        for(File file : path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir,String filename) {
                return filename.endsWith(".txt");
            }
        })){
            //read file
            if(file.getName().startsWith("kompozicija")){
                try{
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String composition = reader.readLine();
                    if(composition != null){
                        kompozicije.add(file.getName());
                        sastaviKompoziciju(composition,file.getName());
                    }
                }catch (IOException ex){
                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                }
            }
        }
    }

    public static void podesiKonfiguracijuPuteva(){
        File path = new File(KONFIGURACIONI_FAJL);
        SekcijaPuta prvaSekcija = new SekcijaPuta(LISTA_PUTEVA[0]);
        SekcijaPuta drugaSekcija = new SekcijaPuta(LISTA_PUTEVA[1]);
        SekcijaPuta trecaSekcija = new SekcijaPuta(LISTA_PUTEVA[2]);

        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line = "";
            while((line = in.readLine()) != null) {
                String[] array = line.split("#");
                if("PUT1".equals(array[0])){
                    prvaSekcija.setMaksimalnaBrzinaSekcije(PUT1_BRZINA);
                    prvaSekcija.setMaksimalanBrojVozila(Integer.parseInt(array[2]));
                }
                else  if("PUT2".equals(array[0])){
                    drugaSekcija.setMaksimalnaBrzinaSekcije(PUT2_BRZINA);
                    drugaSekcija.setMaksimalanBrojVozila(Integer.parseInt(array[2]));
                }
                else  if("PUT3".equals(array[0])){
                    trecaSekcija.setMaksimalnaBrzinaSekcije(PUT3_BRZINA);
                    trecaSekcija.setMaksimalanBrojVozila(Integer.parseInt(array[2]));
                }
            }
            in.close();
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        putevi.add(prvaSekcija);
        putevi.add(drugaSekcija);
        putevi.add(trecaSekcija);
    }

    public static void podesiKonfiguraciju(){

        File file = new File(KONFIGURACIONI_FAJL);
        try{
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = in.readLine())!=null) {
                String[] array = line.split("#");
                System.out.println(line);
                if("MAPA_PATH".equals(array[0])){
                    Mapa.MAPA_PATH = array[1];
                }
                else if("POCETNE_POZICIJE_PATH".equals(array[0])){
                    Stanica.POCETNE_POZICIJE_PATH = array[1];
                }
                else if("PUTANJE_VOZOVA_PATH".equals(array[0])){
                    Stanica.PUTANJE_VOZOVA_PATH= array[1];
                }
                else if("KRETANJE_PATH".equals(array[0])){
                    KRETANJE_PATH = array[1];
                }
                else if("PRUZNI_PRELAZI_PATH".equals(array[0])){
                    Stanica.PRUZNI_PRELAZI_PATH = array[1];
                }
                else if("KOMPOZICIJE_PATH".equals(array[0])){
                    KOMPOZICIJE_PATH = array[1];
                }
                else if("PUT1".equals(array[0])){
                    PUT1_BRZINA = Long.parseLong(array[1]);

                }
                else if("PUT2".equals(array[0])){
                    PUT2_BRZINA = Long.parseLong(array[1]);

                }
                else if("PUT3".equals(array[0])){
                    PUT3_BRZINA = Long.parseLong(array[1]);

                }
                else if("PUT_PATH".equals(array[0])){
                    PUT_PATH = array[1];
                }
                else if("CONFIG_PATH".equals(array[0])){
                    ConfigurationFileWatcher.CONFIG_PATH = array[1];
                }
            }
            in.close();
        } catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }

    @FXML
    public void zauztaviSimulaciju(ActionEvent action){
        Platform.exit();
        System.exit(0);
    }

    public static void sastaviKompoziciju(String kompozicija, String filename){
        try{
            String[] array = kompozicija.split("#");
            boolean putnickaLokomotiva = false;
            boolean teretnaLokomotiva  = false;
            boolean univezalnaLokomotiva = false;
            boolean manevarskaLokomotiva = false;
            boolean teretniVagon = false;
            boolean putnickiVagon = false;
            boolean vagonZaPosebneNamjene = false;


            for(String str : array){
                if(str.contains("putnicka")){
                    putnickaLokomotiva = true;
                }
                else if(str.contains("univerzalna")){
                    univezalnaLokomotiva = true;
                }
                else if(str.contains("manevarska")){
                    manevarskaLokomotiva = true;
                }
                else if(str.contains("teretna")){
                    teretnaLokomotiva = true;
                }
                else if(str.contains("TVagon")){
                    teretniVagon = true;
                }
                else if(str.contains("PVagon") || str.contains("RVagon") || str.contains("LVagon") || str.contains("SVagon")){
                    putnickiVagon = true;
                }
                else if(str.contains("PNVagon")){
                    vagonZaPosebneNamjene = true;
                }
            }
            if(putnickaLokomotiva && teretnaLokomotiva){
                throw new PogresnaKompozicijaException(POGRESNA_KOMPOZICIJA_PORUKA_1+filename);
            }
            else if(putnickaLokomotiva && teretniVagon){
                throw new PogresnaKompozicijaException(POGRESNA_KOMPOZICIJA_PORUKA_2+filename);
            }
            else if (teretnaLokomotiva && putnickiVagon){
                throw new PogresnaKompozicijaException(POGRESNA_KOMPOZICIJA_PORUKA_2+filename);
            }
            else {
                konstruisiKompoziciju(kompozicija);
            }

        }catch (PogresnaKompozicijaException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }

    }

    public static void konstruisiKompoziciju(String line){
        String[] parsedLine = line.split("#");
        String pocetnaStanica = parsedLine[0];
        String krajnjaStanica = parsedLine[1];
        Random rand = new Random();
        long brzina = Long.parseLong(parsedLine[2]);
        String pogon = parsedLine[3];
        ArrayList<Lokomotiva> lokomotive = new ArrayList<>();
        ArrayList<Vagon> vagoni = new ArrayList<>();
        try{
            if(brzina > 500){
                throw new PogresnaKompozicijaException(POGRESNA_KOMPOZICIJA_PORUKA_3);
            }
            else
            {
                for(String str : parsedLine){
                    System.out.println(str);
                    if(str.contains("putnicka")){
                        lokomotive.add(new PutnickaLokomotiva(rand.nextDouble(),randomStringGenerator(),pogon,0,0));
                    }
                    else if(str.contains("teretna")){
                        lokomotive.add(new TeretnaLokomotiva(rand.nextDouble(),randomStringGenerator(),pogon,0,0));
                    }
                    else if(str.contains("manevarska")){
                        lokomotive.add(new ManevarskaLokomotiva(rand.nextDouble(),randomStringGenerator(),pogon,0,0));
                    }
                    else if(str.contains("univerzalna")){
                        lokomotive.add(new UniverzalnaLokomotiva(rand.nextDouble(),randomStringGenerator(),pogon,0,0));
                    }
                    else if(str.contains("TVagon")){
                        vagoni.add(new TeretniVagon(randomStringGenerator(),rand.nextDouble(),0,0,rand.nextDouble()));
                    }
                    else if(str.contains("PVagon"))
                    {
                        vagoni.add(new PutnickiVagon(randomStringGenerator(),rand.nextDouble(),0,0));
                    }
                    else if(str.contains("RVagon"))
                    {
                        vagoni.add(new VagonRestoran(randomStringGenerator(),randomStringGenerator(),rand.nextDouble(),0,0));
                    }
                    else if(str.contains("SVagon"))
                    {
                        vagoni.add(new VagonSaSjedistima(rand.nextInt(),randomStringGenerator(),rand.nextDouble(),0,0));
                    }
                    else if(str.contains("PNVagon"))
                    {
                        vagoni.add(new VagonZaPosebneNamjene(randomStringGenerator(),rand.nextDouble(),0,0));
                    }
                }
                Kompozicija kompozicija = new Kompozicija(lokomotive,vagoni,pocetnaStanica,krajnjaStanica,brzina);
                kompozicija.start();
            }
        }catch (PogresnaKompozicijaException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }

    public static String randomStringGenerator(){
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    @FXML
    void prikaziIstoriju(ActionEvent event){
        try{
            Stage noviProzor = new Stage();
            noviProzor.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/Istorija Kretanja.fxml"));
            IstorijaKretanjaController istorijaKretanjaController = new IstorijaKretanjaController();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            noviProzor.setTitle("Istorija kretanja");
            noviProzor.setScene(scene);
            noviProzor.show();
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }
}