package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EinstellungenController implements Initializable {

    @FXML private Label label1;
    private int count=0,skin=0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();
    }

    public void setskin(ActionEvent event) {
        if (count == 0) {
            label1.setText("true");
            skin = 1;
            count++;

        }
        if (count == 1) {
            label1.setText("true");
            skin = 0;
            count--;
        }
    }

    public int getSkin() {
        return skin;
    }
}
