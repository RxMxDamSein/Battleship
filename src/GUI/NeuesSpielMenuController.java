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

/**
 * Klasse fuer das Neues Spiel Menu
 */
public class NeuesSpielMenuController implements Initializable {
    /**
     * TextField zum eingeben der Spielfeldgroesse
     */
    @FXML
    private TextField GridSize;
    /**
     * ChoiceBox zum auswaehlen der Bot-Schwierigkeit
     */
    @FXML
    private ChoiceBox<String> choiceBox;
    /**
     * x ist die Spielfeldgroesse
     * <br>
     * bot ist die Bot-Schwierigkeit
     */
    private Integer x = 0, bot = 0;

    /**
     * initialize Funktion von JavaFX und initielisiert die ChoiceBox
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox.getItems().add("Einfach");
        choiceBox.getItems().add("Mittel");
        choiceBox.getItems().add("Schwer");
        choiceBox.getItems().add("Nightmare");
        choiceBox.setValue("Einfach");

    }

    /**
     * Button zum starten des Spiels mit den angegebenen Werten
     * @param event
     * @throws IOException
     */
    public void StartButton(ActionEvent event) throws IOException {
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
        if (ch.equals("Schwer")) {
            bot = 4;
        }
        System.out.println("_______________");
        System.out.println("GridSize: " + x);
        System.out.println("Bot: " + ch);
        System.out.println("_______________");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGrid.fxml"));
        Parent r = loader.load();
        GameGridController controller = loader.getController();
        controller.setInteger(x, bot);
        Scene s = new Scene(r);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("Game");
        window.show();

    }


    /**
     * Button um zuruck zum EinzelspielerMenu zu kommen
     * @param event
     * @throws IOException
     */
    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("EinzelspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("EinzelspielerMenu");
        window.show();
    }

    /**
     * loescht die Gridsize eingabe
     * @param event
     */
    public void clearButton(ActionEvent event) {
        GridSize.clear();
    }
}
