package GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;

import logic.save.ResourceManager;
import logic.save.SAFE_SOME;
import logic.save.SaveData;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
/**
 * Klasse für das Spiel: Mehrspieler-Host-Bot
 */
public class MultiHostBotController implements Initializable, Serializable {
    private static final long serialVersionUID = 1337L;
    /**
     * Slider zum einstellen der Botgeschwindigkeit
     */
    @FXML
    private Slider BotSpeedSlider;
    /**
     * linkes Spielfeld
     */
    @FXML
    private StackPane StackPane;
    /**
     * rechtes Spielfeld
     */
    @FXML
    private StackPane StackPane2;
    /**
     * Label über dem linken Feld
     */
    @FXML
    private Label GameTopLabel;
    /**
     * Label über dem rechten Feld
     */
    @FXML
    private Label GameTopLabel1;
    /**
     * Start-Button des Spiels.
     */
    @FXML
    private Button gameStartButton;
    /**
     * bool Wert für den Status des Spiels (gestartet odern nicht gestartet)
     * <br>
     * changingSlider ist true wenn der Slider veraendert worden ist
     */
    private boolean spielstatus = false,changingSlider = false;
    /**
     * linkes Spielfeld
     */
    private GridPane GameGrid;
    /**
     * rechtes Spielfeld
     */
    private GridPane GameGrid2;
    /**
     * linke Labels des Spielfeld
     */
    private Label[][] labels;
    /**
     * rechte Labels des Spielfeld
     */
    private Label[][] labels2;
    /**
     * x ist die Spielfeldgroesse
     * <br>
     * bot ist die Bto-Schwierigkeit
     */
    private Integer x, bot, count = 0;
    /**
     * Standartgeschwindigkeit der Timeline
     */
    private static final int sleeptime0 = 1000;
    /**
     * Standartgeschwindigkeit der Timeline
     */
    public static int sleeptime = sleeptime0;
    /**
     * Klasse mit nuetzliche Methoden
     */
    private nuetzlicheMethoden methoden;
    /**
     * Ein Spiel aus dem Logic Packege
     */
    private Spiel GOETTLICHESSPIELDERVERNICHTUNGMITbot;
    /**
     * Ein Bot aus dem Logic Packege
     */
    private Bot derBot;
    /**
     * Klaase BotHost, welche die Verbindung zum Client uebernimmt
     */
    public BotHost Host;
    /**
     * updateTimeline um das Spiel zu aktualissieren
     */
    private Timeline updateTimeline;

    /**
     * initialisiert und startet die updateTimeline
     */
    private void initupdateTimeline() {
        if (updateTimeline != null) {
            System.err.println("Timeline existiert bereits!!!");
            return;
        }
        updateTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver()) {
                Host.CutConnection();
                if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler() == 0) {
                    methoden.GameEnd(false);
                } else {
                    methoden.GameEnd(true);
                }
                GridUpdater();
                updateTimeline.stop();
            } else if (Host.change) {
                Host.change = false;
                GridUpdater();
            }
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.setDelay(Duration.millis((sleeptime>400)?sleeptime/8:50));
        updateTimeline.play();
    }

    //Konstruktor normal
    /**
     * initialisiert das Spiel
     * @param Client BotClient aus dem JoinMenu
     */
    public void setVariables(Integer Port, Integer FeldGroesse, int bot) {
        sleeptime=sleeptime0;
        methoden = new nuetzlicheMethoden(FeldGroesse);
        x = FeldGroesse;
        this.bot = bot;
        Gridinit();
        Spielinit();
        derBot.shipSizesToAdd(Bot.calcships(x, x));
        Host = new BotHost(Port, FeldGroesse, derBot);
        Host.init();
        GridUpdater();
        initupdateTimeline();
    }

    //Konstruktor laden

    /**
     * Konstruktor um ein Spiel zu laden
     * @param Port Port
     * @param SAFE Save-Game
     * @param id SAFE-ID
     * @param bot Bot-Schwierigkeit
     */
    public void setVariables(Integer Port, SAFE_SOME SAFE, String id, int bot) {
        if (SAFE.id != null) {
            id = SAFE.id;
        }
        x = SAFE.spiele[0].getSizeX();
        methoden = new nuetzlicheMethoden(x);
        this.bot = bot;
        Spielinit();
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = SAFE.spiele[0];
        derBot.dasSpiel = GOETTLICHESSPIELDERVERNICHTUNGMITbot;
        derBot.dasSpiel.setSpielFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld());
        Gridinit();
        Host = new BotHost(Port, x, derBot, id);
        Host.init();
        GridUpdater();
        initupdateTimeline();

    }

    /**
     * Aktuallisiert die beiden Grids des Spiels.
     */
    public void GridUpdater() {
        int feld[][][] = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld();
        //for (int s=0;s<2;s++){
        for (int a = x - 1; a >= 0; a--) {
            for (int b = x - 1; b >= 0; b--) {
                switch (feld[0][a][b]) {
                    default:
                        break;
                    case 1:
                        //labels[a][b].setStyle("-fx-background-color: grey");
                        labels[a][b] = methoden.textureSchiff(labels[a][b], x);
                        break;
                    case 2:
                        //labels[a][b].setStyle("-fx-background-color: red");
                        labels[a][b] = methoden.textureSchiffTreffer(labels[a][b], x);
                        break;
                    case 3:
                        //labels[a][b].setStyle("-fx-background-color: blue");
                        labels[a][b] = methoden.textureWasserTreffer(labels[a][b], x);
                        break;
                    case 4:
                        labels[a][b] = methoden.textureversenkt(labels[a][b], x);
                        break;
                }
                switch (feld[1][a][b]) {
                    default:
                        break;
                    case 1:
                        //labels2[a][b].setStyle("-fx-background-color: black");
                        labels2[a][b] = methoden.textureversenkt(labels2[a][b], x);
                        break;
                    case 2:
                        //labels2[a][b].setStyle("-fx-background-color: red");
                        labels2[a][b] = methoden.textureSchiffTreffer(labels2[a][b], x);
                        break;
                    case 3:
                        //labels2[a][b].setStyle("-fx-background-color: blue");
                        labels2[a][b] = methoden.textureWasserTreffer(labels2[a][b], x);
                        break;
                    case 4:
                        labels2[a][b] = methoden.textureversenkt(labels2[a][b], x);
                        break;
                }
            }
        }
        //}

    }

    //Initialisiert das Spiel und den Bot
    public void Spielinit() {
        switch (bot) {
            case 1:
                derBot = new RDM_Bot(x, x);
                break;
            case 2:
                derBot = new Bot_lvl_2(x, x);
                break;
            case 3:
                derBot = new Bot_schwer(x, x);
                break;
            default:
                System.err.println("Bot Auswahl Fehler");
                return;
        }
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = derBot.dasSpiel;
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.setAbschussSpieler(1);
    }


    //Grid und Labels initialisieren
    /**
     * initialisiert die Grids und Labels
     */
    public void Gridinit() {
        //initialisieren Label und Grid (1)
        GameGrid = new GridPane();
        labels = new Label[x][x];
        //initialisieren Label und Grid (2)
        GameGrid2 = new GridPane();
        labels2 = new Label[x][x];
        for (int a = x - 1; a >= 0; a--) {
            //System.out.println("for 2");
            for (int b = x - 1; b >= 0; b--) {

                //Game Labels (1)
                labels[a][b] = new Label();
                //labels[a][b].setMinSize(50,50);
                labels[a][b].setMinSize(methoden.minsizeberechner(x), methoden.minsizeberechner(x));
                //labels[a][b].setStyle("-fx-background-color: #03fcf4");
                labels[a][b] = methoden.textureWasser(labels[a][b], x);

                //lustige bilder zu den Labels
                /*
                Image img = new Image("GUI/Textures/42.jpg");
                ImageView view = new ImageView(img);
                view.setFitHeight(100);
                view.setPreserveRatio(true);
                labels[a][b].setGraphic(view);
                 */

                //final int ca=a,cb=b;
                int ca = a, cb = b;
                labels[a][b].setOnMouseClicked(e -> labelclick(ca, cb));
                GridPane.setConstraints(labels[a][b], a, b, 1, 1, HPos.CENTER, VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);

                //Game Labels (2)
                labels2[a][b] = new Label();
                //labels2[a][b].setMinSize(50,50);
                labels2[a][b].setMinSize(methoden.minsizeberechner(x), methoden.minsizeberechner(x));
                //labels2[a][b].setStyle("-fx-background-color: #03fcf4");
                labels2[a][b] = methoden.textureWasser(labels2[a][b], x);
                labels2[a][b].setOnMouseClicked(e -> label2click(ca, cb));


                GridPane.setConstraints(labels2[a][b], a, b, 1, 1, HPos.CENTER, VPos.CENTER);
                GameGrid2.getChildren().add(labels2[a][b]);
            }
        }

        // GameGrid 1 zu Stackpane 1 hinzufügen
        GameGrid.setAlignment(Pos.CENTER);
        GameGrid.setHgap(1);
        GameGrid.setVgap(1);
        //GameGrid
        StackPane.getChildren().add(GameGrid);
        // GameGrid 2 zu Stackpane 2 hinzufügen
        GameGrid2.setAlignment(Pos.CENTER);
        GameGrid2.setHgap(1);
        GameGrid2.setVgap(1);
        StackPane2.getChildren().add(GameGrid2);
    }

    //ActionHandler für Label 2 (Grid 2) gedrückt
    private void label2click(int a, int b) {
        System.out.println("Grid 2 pressed in x: " + a + " y: " + b);
    }



    //ActionHandler für Label 1 (Grid 1) gedrückt
    private void labelclick(int a, int b) {
        System.out.println("x= " + a + " y= " + b);
    }

    //versetzt Spiel in Feuermodus
    /**
     * Startet das Spiel
     * @param event
     */
    public void gameStart(ActionEvent event) {
        if (spielstatus || !Host.Connected) {
            System.err.println("Spiel bereits im gange!!");
            return;
        }
        spielstatus = true;
        System.out.println("Spielstatus: " + spielstatus);
        //GameTopLabel1.setText("Du schießt jetzt hier:");
        //Host.senships(Bot.getShipSizes(GOETTLICHESSPIELDERVERNICHTUNGMITbot.schiffe));
        Host.Spielstartet = true;
        Host.schuss();
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.setAbschussSpieler(1);
        gameStartButton.setVisible(false);
        methoden.setAbschussLabelTimeline(GOETTLICHESSPIELDERVERNICHTUNGMITbot, GameTopLabel, GameTopLabel1);
        initupdateTimeline();
    }
    /**
     * Button um zuruck zum MehrspielerMenu zu kommen.
     * @param event
     * @throws IOException
     */
    public void BacktoMenu(ActionEvent event) throws IOException {
        if (updateTimeline != null) {
            updateTimeline.stop();
        }

        Host.CutConnection();

        Parent root = FXMLLoader.load(getClass().getResource("MehrspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MehrspielerMenu");
        window.show();
    }
    /**
     * initialize Funktion von JavaFX und stellt den Btoslider ein
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BotSpeedSlider.setMin(1);
        BotSpeedSlider.setValue(100);
        BotSpeedSlider.setOnMouseReleased(event -> {
            changeSlider();
        });
        BotSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println("observable: "+observable+" oldValue: "+oldValue+" newValue: "+newValue);
                changeSlider();
            }
        });
    }
    /**
     * Funktion um die Geschwindigkeit des Bots zu aendern anhand des Sliders
     */
    private void changeSlider() {
        if (changingSlider)
            return;
        changingSlider = true;
        double value = BotSpeedSlider.getValue();
        sleeptime = (int) (sleeptime0 * (value / 100));
        if(updateTimeline!=null)
            updateTimeline.setDelay(Duration.millis((sleeptime>400)?sleeptime/8:50));
        System.out.println("NewSleeptime: " +sleeptime );
        Runnable runnable = () ->
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            changingSlider = false;
        };
        Thread t = new Thread(runnable);
        t.start();
    }


    public void printfeld(ActionEvent event) {
        logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(), true);
    }

    /**
     * Speichert das Spiel
     * @param event
     * @throws IOException
     */
    public void Speichern(ActionEvent event) throws IOException {
        Host.pause = true;

        //SaveData data = new SaveData();
        //ResourceManager.save(this, "1.save");
        // SAVE POP UP Fenster
        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.setPadding(new Insets(10, 10, 10, 10));
        comp.setSpacing(5);
        comp.setStyle("-fx-background-color: DARKCYAN;");
        comp.setAlignment(Pos.CENTER);
        TextField DateiName = new TextField();
        DateiName.setText("Dateiname:");
        Button Save = new Button();
        Save.setPrefSize(100, 30);
        Save.setText("Save");
        Save.setOnAction(event1 -> {
            String name = String.valueOf(DateiName.getText());
            name = name + "-M";
            System.out.println("Name: " + name);
            //Speichern
            String hash = "" + this.hashCode();
            new SAFE_SOME(null, new Spiel[]{GOETTLICHESSPIELDERVERNICHTUNGMITbot}, 4, hash, name);
            Host.save(hash);
            newStage.close();
        });
        comp.getChildren().add(DateiName);
        comp.getChildren().add(Save);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
    }
}
