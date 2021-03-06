package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Bot;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JoinMenuController implements Initializable {
    @FXML
    private Label joinLabel;
    @FXML
    private TextField IPText, PortText;
    @FXML
    private ChoiceBox<String> SpielartChoice, BotChoice;
    private nuetzlicheMethoden methoden;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methoden = new nuetzlicheMethoden();
        BotChoice.getItems().add("Einfach");
        BotChoice.getItems().add("Mittel");
        BotChoice.getItems().add("Schwer");
        BotChoice.setValue("Mittel");
        //BotChoice.hide();

        SpielartChoice.getItems().add("Spieler");
        SpielartChoice.getItems().add("Bot");
        SpielartChoice.setValue("Spieler");

    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MehrspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MehrspielerMenu");
        window.show();
    }

    public void StartButton(ActionEvent event) throws IOException {
        Integer p;
        try {
            p = Integer.parseInt(PortText.getText());
        } catch (NumberFormatException e) {
            p = 420;
        }
        String ip;
        try {
            ip = IPText.getText();
        } catch (NumberFormatException e) {
            ip = "127.0.0.1";
        }
        String ausw = SpielartChoice.getValue();
        String ch = BotChoice.getValue();
        int bot = 2;

        if (ch.equals("Einfach")) {
            bot = 1;
        }
        if (ch.equals("Mittel")) {
            bot = 2;
        }
        if (ch.equals("Schwer")) {
            bot = 3;
        }
        //Verbinden mit Host
        if (ausw.equals("Spieler")) {

        }

        if (ausw.equals("Spieler")) {
            Client Client = new Client(ip, p);
            methoden.warteBildschirm(Client, null);

        } else if (ausw.equals("Bot")) {
            BotClient Client = new BotClient(ip, p, bot);
            methoden.warteBildschirm(null, Client);

        }
    }

}
