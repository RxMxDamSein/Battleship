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
import logic.save.SAFE_SOME;


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
        SAFE_SOME SAFE = SAFE_SOME.load(id);
        switch (SAFE.game) {
            //1P vs B
            case 2:
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGrid.fxml"));
                Parent r = loader.load();
                GameGridController controller = loader.getController();
                controller.gameloader(SAFE);
                Scene s = new Scene(r);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(s);
                window.setTitle("GameGrid");
                window.show();
                return;
            //BvB
            case 3:
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("BotgegenBOTGrid.fxml"));
                Parent r2 = loader2.load();
                BOTgegenBOTGridController controller2 = loader2.getController();
                controller2.gameloader(SAFE);
                Scene s2 = new Scene(r2);
                Stage window2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window2.setScene(s2);
                window2.setTitle("BvB");
                window2.show();
                return;
            default:
                System.err.println("Falsche Datei Ausgewählt!!");
                return;
        }

        /*
        //Id für Bot Datei
        fileChooser.setTitle("Wähle Bot Datei!");
        fileChooser.setInitialDirectory(new File("C:\\Users\\Dennis\\Documents\\Hochschule Aalen\\Semester 3\\ProgrammierPraktikum\\Battleship\\save"));
        System.out.println("Wähle Bot Datei!");
        File i = fileChooser.showOpenDialog(theStage);
        String id2 = i.getName();
        System.out.println("");
         */
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
