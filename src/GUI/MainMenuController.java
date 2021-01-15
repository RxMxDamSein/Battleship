package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable  {
    public static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        MainMenuController.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //nuetzlicheMethoden.setStageCenter();
    }

    public void einzelspieler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("EinzelspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("EinzelspielerMenu");
        window.show();
    }

    public void mehrspieler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MehrspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MehrspielerMenu");
        window.show();
    }

    public void einstellungen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Einstellungen.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("Einstellungen");
        window.show();
    }
}
