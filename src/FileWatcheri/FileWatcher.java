package FileWatcheri;

import javafx.scene.control.Control;
import sample.Controller;
import sample.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FileWatcher extends Thread{
    public FileWatcher(){

    }
    @Override
    public void run(){
        try{
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(Controller.KOMPOZICIJE_PATH);
            dir.register(watcher,ENTRY_CREATE,ENTRY_MODIFY);
            while(true) {
                WatchKey key;
                try {
                    key = watcher.take();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    if (filename.toString().startsWith("kompozicija") && filename.toString().endsWith(".txt")) {
                        //provjeri da li je mozda medju dodatim fajlovima
                        File path = new File(Controller.KOMPOZICIJE_PATH+File.separator+filename.toString());
                        if(!Controller.kompozicije.contains(filename.toString())){
                            BufferedReader reader = new BufferedReader(new FileReader(path));
                            String line = reader.readLine();
                            if (line != null) {
                                Controller.kompozicije.add(filename.toString());
                                Controller.sastaviKompoziciju(line,filename.toString());
                            }
                            reader.close();
                        }
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }catch (IOException ex){
            Logger.getLogger(Main.LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
    }
}
