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

public class HostMenuController implements Initializable {

    @FXML private TextField PortText;
    @FXML private TextField SpielGroesseText;
    @FXML private ChoiceBox<String> SpielartChoice;
    @FXML private ChoiceBox<String> BotChoice;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BotChoice.getItems().add("Einfach");
        BotChoice.getItems().add("Mittel");
        BotChoice.getItems().add("Schwer");
        BotChoice.setValue("Einfach");
        //BotChoice.hide();

        SpielartChoice.getItems().add("Spieler");
        SpielartChoice.getItems().add("Bot");
        SpielartChoice.setValue("Spieler");


    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);

        window.show();
    }

    public void ladenbutton(ActionEvent event) {
    }

    public void StartButton(ActionEvent event) throws IOException {
        /*
        String sx = String.valueOf(GridSize.getText()); //Text aus der Benutzereingabe
        Object value = choiceBox.getValue();
        String ch = (String) value;

        //für test zwecke
        if (sx.equals("")) {
            sx = "10";
        }
        x = Integer.parseInt(sx);
        if (ch.equals("Einfach")) {
            bot = 1;
        }
        if (ch.equals("Mittel")) {
            bot = 2;
        }
        if (ch.equals("Nightmare")) {
            bot = 3;
        }
        System.out.println("_______________");
        System.out.println("GridSize: "+x);
        System.out.println("Bot: "+ch);
        System.out.println("_______________");

         */
        Integer p;
        try {
            p = Integer.parseInt(PortText.getText());
        } catch (NumberFormatException e) {
            p = 420;
        }
        Integer g;
        try {
            g = Integer.parseInt(SpielGroesseText.getText());
        } catch (NumberFormatException e) {
            g = 10;
        }




        String ausw = SpielartChoice.getValue();
        String bot = BotChoice.getValue();

        if (ausw.equals("Spieler")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostSpielerGrid.fxml"));
            Parent r = loader.load();
            MultiHostSpielerController controller = loader.getController();
            controller.setVariables(p,g);
            Scene s = new Scene(r);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
        }














    }
}
