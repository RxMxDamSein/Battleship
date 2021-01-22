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
 * Menu des Spiels Bot gegen Bot
 */
public class BOTgegenBOTMenuController implements Initializable {
    /**
     * ChoiceBox um die Schwierigkeit des 1.Bot einzustellen
     */
    @FXML
    private ChoiceBox<String> choiceBox1;
    /**
     * ChoiceBox um die Schwierigkeit des 1.Bot einzustellen
     */
    @FXML
    private ChoiceBox<String> choiceBox2;
    /**
     * TextField zum eingeben der Feldgroesse
     */
    @FXML
    private TextField GridSize;
    /**
     * x = Feldgroesse
     * <br>
     * bot1 = Schwierigkeit des 1.Bot
     * <br>
     * bot2 = Schwierigkeit des 2.Bot
     */
    private Integer x = 0, bot1 = 0, bot2 = 0;

    /**
     * initialize methode von JAVAFX, welche die choiceBox1 und choiceBox2 initialisiert
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox1.getItems().add("Einfach");
        choiceBox1.getItems().add("Mittel");
        choiceBox1.getItems().add("Schwer");
        choiceBox1.getItems().add("Nightmare");
        choiceBox1.setValue("Einfach");

        choiceBox2.getItems().add("Einfach");
        choiceBox2.getItems().add("Mittel");
        choiceBox2.getItems().add("Schwer");
        choiceBox2.getItems().add("Nightmare");
        choiceBox2.setValue("Einfach");

    }

    /**
     * zurück button zum MeinMenu
     * @param event
     * @throws IOException
     */
    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MainMenu");
        window.show();
    }

    /**
     * loescht die Eingabe im Gridsize Textfield
     * @param event
     */
    public void clearButton(ActionEvent event) {
        GridSize.clear();
    }

    /**
     * startet das Spiel mit den eingestellten Werten
     * @param event
     * @throws IOException
     */
    public void StartButton(ActionEvent event) throws IOException {
        String sx = String.valueOf(GridSize.getText()); //Text aus der Benutzereingabe
        Object value1 = choiceBox1.getValue();
        Object value2 = choiceBox2.getValue();
        String ch1 = (String) value1;
        String ch2 = (String) value2;

        //für test zwecke
        if (sx.equals("")) {
            sx = "10";
        }
        x = Integer.parseInt(sx);

        //Bot1 festlegen
        if (ch1.equals("Einfach")) {
            bot1 = 1;
        }
        if (ch1.equals("Mittel")) {
            bot1 = 2;
        }
        if (ch1.equals("Nightmare")) {
            bot1 = 3;
        }
        if (ch1.equals("Schwer")) {
            bot1 = 4;
        }
        //Bot2 festlegen
        if (ch2.equals("Einfach")) {
            bot2 = 1;
        }
        if (ch2.equals("Mittel")) {
            bot2 = 2;
        }
        if (ch2.equals("Nightmare")) {
            bot2 = 3;
        }
        if (ch2.equals("Schwer")) {
            bot2 = 4;
        }
        System.out.println("_______________");
        System.out.println("GridSize: " + x);
        System.out.println("Bot 1: " + ch1 + " Bot 2: " + ch2);
        System.out.println("_______________");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("BotgegenBOTGrid.fxml"));
        Parent r = loader.load();
        BOTgegenBOTGridController controller = loader.getController();
        controller.setInteger(x, bot1, bot2);
        Scene s = new Scene(r);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("BvB");
        window.show();
    }
}
