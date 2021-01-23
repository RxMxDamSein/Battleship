package GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.save.SAFE_SOME;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Klasse fuer das Host-Menu
 */
public class HostMenuController implements Initializable {
    /**
     * TextField zum zum Eingeben des Ports
     */
    @FXML
    private TextField PortText;
    /**
     * TextField zum zum Eingeben der Spielfeldgroesse
     */
    @FXML
    private TextField SpielGroesseText;
    /**
     * ChoiceBox zum Einstellen der Spielart
     */
    @FXML
    private ChoiceBox<String> SpielartChoice;
    /**
     * ChoiceBox zum auswaehlen der Bot-Schwierigkeit
     */
    @FXML
    private ChoiceBox<String> BotChoice;
    /**
     * zusatz Klasse mit verschiedenen Funktionen
     */
    private nuetzlicheMethoden methoden;

    /**
     * initialize Funktion von JavaFX, welche die CHoiceBoxen initiealisiert
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BotChoice.setVisible(false);
        methoden = new nuetzlicheMethoden();
        BotChoice.getItems().add("Einfach");
        BotChoice.getItems().add("Mittel");
        BotChoice.getItems().add("Schwer");
        BotChoice.setValue("Einfach");
        SpielartChoice.getItems().add("Spieler");
        SpielartChoice.getItems().add("Bot");
        SpielartChoice.setValue("Spieler");
        SpielartChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() == 1) {
                    BotChoice.setVisible(true);
                } else if (newValue.intValue() == 0) {
                    BotChoice.setVisible(false);
                }
            }
        });
    }

    /**
     * Button um zuruck zum MainMenu zu kommen
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
     * Button um ein Spiel zu laden
     * @param event
     * @throws IOException
     */
    public void ladenbutton(ActionEvent event) throws IOException {
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

        SAFE_SOME SAFE = SAFE_SOME.load(id);
        if (SAFE.game < 4 || SAFE.game > 5) {
            System.err.println("Falsche speicher Datei!!");
            return;
        }

        Integer p;
        try {
            p = Integer.parseInt(PortText.getText());
        } catch (NumberFormatException e) {
            p = 420;
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

        if (ausw.equals("Spieler")) {
            methoden.setHostVariablen(p, SAFE, id);
            methoden.HostwarteBildschirm();
            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostSpielerGrid.fxml"));
            Parent r = loader.load();
            MultiHostSpielerController controller = loader.getController();
            controller.setVariables(p,SAFE,id);
            Scene s = new Scene(r);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.setTitle("Host Spieler");
            window.show();
             */
        } else if (ausw.equals("Bot")) {
            methoden.setHostVariablen(p, bot, SAFE, id);
            methoden.HostwarteBildschirm();
        }

    }

    /**
     * Button-Funktion um den Server mit den angegebenen Werten zu Starten
     * @param event
     * @throws IOException
     */
    public void StartButton(ActionEvent event) throws IOException {
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
        String ch = BotChoice.getValue();
        Integer bot = 2;

        if (ch.equals("Einfach")) {
            bot = 1;
        }
        if (ch.equals("Mittel")) {
            bot = 2;
        }
        if (ch.equals("Schwer")) {
            bot = 3;
        }

        if (ausw.equals("Spieler")) {
            methoden.setHostVariablen(p, g);
            methoden.HostwarteBildschirm();
            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostSpielerGrid.fxml"));
            Parent r = loader.load();
            MultiHostSpielerController controller = loader.getController();
            controller.setVariables(p,g);
            Scene s = new Scene(r);
            MainMenuController.primaryStage.setScene(s);
            MainMenuController.primaryStage.setTitle("Host Spieler");
            MainMenuController.primaryStage.show();

             */
        } else if (ausw.equals("Bot")) {
            methoden.setHostVariablen(p, g, bot);
            methoden.HostwarteBildschirm();
        }


    }
}
