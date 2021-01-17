package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class EinzelspielerController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MainMenu");
        window.show();
    }

    public void neuesSpiel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NeuesSpielMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("NeuesSpielMenu");
        window.show();
    }

    public void spielladen(ActionEvent event) throws IOException {
        //String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        //System.out.println(currentPath);
        Node source = (Node) event.getSource();
        Window theStage = source.getScene().getWindow();
        //Id für Spiel Datei
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wähle Spiel Datei!");
        new File("./save/").mkdirs();
        fileChooser.setInitialDirectory(new File("./save"));
        System.out.println("Wähle Spiel Datei!");
        File file = fileChooser.showOpenDialog(theStage);
        //System.out.println("Path: "+file.getAbsolutePath());
        String id = file.getName();
        //System.out.println("Name: "+id1);

        /*
        //Id für Bot Datei
        fileChooser.setTitle("Wähle Bot Datei!");
        fileChooser.setInitialDirectory(new File("C:\\Users\\Dennis\\Documents\\Hochschule Aalen\\Semester 3\\ProgrammierPraktikum\\Battleship\\save"));
        System.out.println("Wähle Bot Datei!");
        File i = fileChooser.showOpenDialog(theStage);
        String id2 = i.getName();
        System.out.println("");
         */

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGrid.fxml"));
        Parent r = loader.load();
        GameGridController controller = loader.getController();
        controller.gameloader(id);
        Scene s = new Scene(r);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("GameGrid");
        window.show();
    }

    public void BOTvsBOT(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("BOTgegenBOTMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("BvBMenu");
        window.show();
    }
}
