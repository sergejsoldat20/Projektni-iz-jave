package sample;

import IstorijaKretanja.IstorijaKretanja;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IstorijaKretanjaController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<IstorijaKretanja> tableView;
    @FXML
    private TableColumn<IstorijaKretanja, String> kompozicije;
    @FXML
    private TableColumn<?, ?> vrijeme;
    @FXML
    private TableColumn<?, ?> stanice;
    @FXML
    private TableColumn<?, ?> koordinate;

    @FXML
    private void initialize(){
        File path = new File(Controller.KRETANJE_PATH);
        kompozicije.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        vrijeme.setCellValueFactory(new PropertyValueFactory<>("vrijemeTrajanja"));
        stanice.setCellValueFactory(new PropertyValueFactory<>("zaustavljanjaNaStanicama"));
        koordinate.setCellValueFactory(new PropertyValueFactory<>("predjeneKoordinate"));
        for(File file : path.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir,String filename){
                return filename.endsWith(".ser");
            }
        })) {
            try{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                IstorijaKretanja istorija = (IstorijaKretanja) ois.readObject();
                tableView.getItems().add(istorija);
                ois.close();
                fis.close();

            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
