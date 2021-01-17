package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EinstellungenController implements Initializable {

    @FXML
    private Label label1;
    public static int skin = 0;

    public EinstellungenController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (skin == 0) {
            label1.setText("false");
            return;
        }
        if (skin == 1) {
            label1.setText("true");
            return;
        }

    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MainMenu");
        window.show();
    }

    public void setskin(ActionEvent event) {
        if (skin == 0) {
            label1.setText("true");
            skin = 1;
            return;
        }
        if (skin == 1) {
            label1.setText("false");
            skin = 0;
            return;
        }
    }

    public int getSkin() {
        return skin;
    }
}
