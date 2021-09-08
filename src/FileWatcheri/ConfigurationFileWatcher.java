package FileWatcheri;

import javafx.scene.control.Control;
import sample.Controller;
import sample.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class ConfigurationFileWatcher extends Thread {
    public static String CONFIG_PATH = null;

    public ConfigurationFileWatcher(){

    }
    @Override
    public void run(){
        try{
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(CONFIG_PATH);
            dir.register(watcher,ENTRY_MODIFY);
            while(true){
                WatchKey key;
                try{
                    key = watcher.take();
                }catch (InterruptedException ex){
                    Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
                    break;
                }
                for(WatchEvent<?> event:key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    if (filename.endsWith("konfiguracioni fajl.txt")) {
                        BufferedReader in = new BufferedReader(new FileReader(CONFIG_PATH + filename.toString()));
                        String prvaLinija = in.readLine();
                        String drugaLinija = in.readLine();
                        String trecaLinija = in.readLine();

                        String[] array = prvaLinija.split("#");
                        if(array[0].equals(Controller.LISTA_PUTEVA[0])){
                            int maksBrzina = Integer.parseInt(array[1]);
                            int maksBrojVozila = Integer.parseInt(array[2]);
                            if(maksBrzina> Controller.putevi.get(0).getMaksimalnaBrzinaSekcije()){
                                Controller.putevi.get(0).setMaksimalnaBrzinaSekcije(maksBrzina);
                            }
                            if(maksBrojVozila > Controller.putevi.get(0).getMaksimalanBrojVozila()){
                                Controller.putevi.get(0).setMaksimalanBrojVozila(maksBrojVozila);
                            }
                        }
                        array = prvaLinija.split("#");
                        if(array[0].equals(Controller.LISTA_PUTEVA[1])){
                            int maksBrzina = Integer.parseInt(array[1]);
                            int maksBrojVozila = Integer.parseInt(array[2]);
                            if(maksBrzina> Controller.putevi.get(1).getMaksimalnaBrzinaSekcije()){
                                Controller.putevi.get(1).setMaksimalnaBrzinaSekcije(maksBrzina);
                            }
                            if(maksBrojVozila > Controller.putevi.get(1).getMaksimalanBrojVozila()){
                                Controller.putevi.get(1).setMaksimalanBrojVozila(maksBrojVozila);
                            }
                        }
                        array = prvaLinija.split("#");
                        if(array[0].equals(Controller.LISTA_PUTEVA[2])){
                            int maksBrzina = Integer.parseInt(array[1]);
                            int maksBrojVozila = Integer.parseInt(array[2]);
                            if(maksBrzina> Controller.putevi.get(2).getMaksimalnaBrzinaSekcije()){
                                Controller.putevi.get(2).setMaksimalnaBrzinaSekcije(maksBrzina);
                            }
                            if(maksBrojVozila > Controller.putevi.get(2).getMaksimalanBrojVozila()){
                                Controller.putevi.get(2).setMaksimalanBrojVozila(maksBrojVozila);
                            }
                        }
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }catch (IOException | NullPointerException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }
}
