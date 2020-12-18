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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BOTgegenBOTMenu implements Initializable {

    @FXML private ChoiceBox<String> choiceBox1;
    @FXML private ChoiceBox<String> choiceBox2;
    @FXML private TextField GridSize;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox1.getItems().add("Einfach");
        choiceBox1.getItems().add("Mittel");
        choiceBox1.getItems().add("Schwer");
        choiceBox1.setValue("Einfach");

        choiceBox2.getItems().add("Einfach");
        choiceBox2.getItems().add("Mittel");
        choiceBox2.getItems().add("Schwer");
        choiceBox2.setValue("Einfach");

    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();
    }

    public void clearButton(ActionEvent event) {
        GridSize.clear();
    }

    public void StartButton(ActionEvent event) {
    }
}
