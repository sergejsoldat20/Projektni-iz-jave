package sample;

import Infrastruktura.Stanica;
import Lokomotive.Kompozicija;
import Lokomotive.Lokomotiva;
import Mapa.Mapa;
import Vagoni.Vagon;
import Vozila.Automobil;
import Vozila.Vozilo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.net.ssl.SNIHostName;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static final String LOGGER_PATH = "src" + File.separator + "Log" +File.separator + "exceptions.log";
    public static final String LOGGER_IME  = "Logger";
    public static Handler handler;

    public static Mapa MAPA = new Mapa();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Programski jezici - Sergej Soldat");
        primaryStage.setScene(new Scene(root, 600,670 ));
        primaryStage.show();
    }

    public static void main(String[] args) {

        try{
            handler = new FileHandler(LOGGER_PATH,true);
            Logger logger = Logger.getLogger(LOGGER_IME);
            logger.addHandler(handler);
        } catch (IOException ex){
            Logger.getLogger(LOGGER_IME).log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }
        launch(args);
    }
}
