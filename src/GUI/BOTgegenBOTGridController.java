package GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;
import logic.save.SAFE_SOME;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Klasse für das Spiel: Bot gegen Bot
 */
public class BOTgegenBOTGridController implements Initializable {
    /**
     * Slider zum einstellen der Botgeschwindigkeit
     */
    @FXML
    private Slider BotSpeedSlider;
    /**
     * changingSlider ist true wenn der Slider veraendert worden ist.
     */
    private boolean changingSlider = false,load=false;
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

    @FXML
    private Label GameTopLabel;
    @FXML
    private Label GameTopLabel1;
    /**
     * Button zum starten des Spiels
     */
    @FXML
    Button gameStartButton;
    private boolean spielstatus = false;
    /**
     * rechtes Spielfeld
     */
    private GridPane GameGrid;
    /**
     * linkes Spielfeld
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
     * bot1 schwierigkeit des 1.Bots
     * <br>
     * bot2 schwierigkeit des 2.Bots
     */
    private Integer x, bot1, bot2, count = 0;
    /**
     * Timeline zum schießen der beiden Bots
     */
    private Timeline oneSecondsWonder;
    /**
     * Standartgeschwindigkeit der Timeline
     */
    private final int Ospeed = 500;
    /**
     * neu eingestellte Geschwindigkeit fuer den Bot
     */
    private int speed = Ospeed;
    /**
     * Spielfeld
     */
    private int[][][] feld;
    /**
     * Klasse mit nuetzliche Methoden
     */
    private nuetzlicheMethoden methoden;
    /**
     * 1.Bot des Spiels
     */
    private Bot ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST;
    /**
     * 2.Bot des Spiels
     */
    private Bot WUNDERVOLLERGEGNERBOT;

    public BOTgegenBOTGridController() {}

    /**
     * initialize methode von JavaFX, welche den Botslider einstellt
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Duration duration = new Duration();
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
        speed = (int) (Ospeed * (value / 100));
        System.out.println("Speed: " + speed + " newValue: " + value);
        if (oneSecondsWonder != null) {
            oneSecondsWonder.stop();
            //oneSecondsWonder.setDelay(Duration.millis(speed));
            initoneSecondsWonder();
        }
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

    /**
     * Funktion die verschiedene Variablen setzt und das Spiel initialisiert
     * @param a Spielfeldgroesse
     * @param b1 Bot.1-Schwierigkeit
     * @param b2 Bot.2-Schwierigkeit
     */
    public void setInteger(Integer a, Integer b1, Integer b2) {
        methoden = new nuetzlicheMethoden(a);
        x = a;
        bot1 = b2;
        bot2 = b1;
        Spielinit();
        Gridinit();
        GridUpdater();

    }

    /**
     * Funktion um ein Spiel zu intitialisiern mit einem save-game
     * @param SAFE Ein gespeichertes Spiel
     */
    public void gameloader(SAFE_SOME SAFE){
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = SAFE.bots[0];
        WUNDERVOLLERGEGNERBOT = SAFE.bots[1];
        methoden = new nuetzlicheMethoden(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getSizeX());
        x = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getSizeX();
        feld = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld();
        Gridinit();
        GridUpdater();
    }

    /**
     * Aktualissiert die beiden Spielfelder
     */
    public void GridUpdater() {
        int feld1[][][] = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld();
        //for (int s=0;s<2;s++){
        for (int a = x - 1; a >= 0; a--) {
            for (int b = x - 1; b >= 0; b--) {
                switch (feld1[0][a][b]) {
                    default:
                        break;
                    case 1:
                        labels2[a][b] = methoden.textureSchiff(labels2[a][b], x);

                        //labels2[a][b].setStyle("-fx-background-color: grey");
                        break;
                    case 2:
                        labels2[a][b] = methoden.textureSchiffTreffer(labels2[a][b], x);
                        //labels2[a][b].setStyle("-fx-background-color: red");
                        break;
                    case 3:
                        labels2[a][b] = methoden.textureWasserTreffer(labels2[a][b], x);
                        //labels2[a][b].setStyle("-fx-background-color: blue");
                        break;
                    case 4:
                        labels2[a][b] = methoden.textureversenkt(labels2[a][b], x);
                        break;
                }
                /*
                switch (feld1[1][a][b]) {
                    default:
                        break;
                    case 1:
                        labels[a][b].setStyle("-fx-background-color: black");
                        break;
                    case 2:
                        labels[a][b].setStyle("-fx-background-color: red");
                        break;
                    case 3:
                        labels[a][b].setStyle("-fx-background-color: blue");
                        break;
                    case 4:
                        labels[a][b].setStyle("-fx-background-color: black");
                        break;
                }

                 */
            }
        }
        //}

        int feld2[][][] = WUNDERVOLLERGEGNERBOT.dasSpiel.getFeld();
        //for (int s=0;s<2;s++){
        for (int a = x - 1; a >= 0; a--) {
            for (int b = x - 1; b >= 0; b--) {
                switch (feld2[0][a][b]) {
                    default:
                        break;
                    case 1:
                        labels[a][b] = methoden.textureSchiff(labels[a][b], x);
                        //labels[a][b].setStyle("-fx-background-color: grey");
                        break;
                    case 2:
                        labels[a][b] = methoden.textureSchiffTreffer(labels[a][b], x);
                        //labels[a][b].setStyle("-fx-background-color: red");
                        break;
                    case 3:
                        labels[a][b] = methoden.textureWasserTreffer(labels[a][b], x);
                        //labels[a][b].setStyle("-fx-background-color: blue");
                        break;
                    case 4:
                        labels[a][b] = methoden.textureversenkt(labels[a][b],x);
                        break;
                }
                /*
                switch (feld2[1][a][b]) {
                    default:
                        break;
                    case 1:
                        labels2[a][b].setStyle("-fx-background-color: black");
                        break;
                    case 2:
                        labels2[a][b].setStyle("-fx-background-color: red");
                        break;
                    case 3:
                        labels2[a][b].setStyle("-fx-background-color: blue");
                        break;
                    case 4:
                        labels2[a][b].setStyle("-fx-background-color: black");
                        break;
                }

                 */
            }
        }
        //}

    }

    /**
     * initialisiert die Bots und fuegt diesen ihre Schiffe hinzu
     */
    public void Spielinit() {
        //GOETTLICHESSPIELDERVERNICHTUNGMITbot = new Spiel(x,x,true);
        System.out.println(x + " " + x);
        switch (bot1) {
            case 1:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new RDM_Bot(x, x);
                break;
            case 2:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_lvl_2(x, x);
                break;
            case 3:
                //ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_nightmare(x,x,WUNDERVOLLERGEGNERBOT.dasSpiel);
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_nightmare(x, x, null);
                break;
            case 4:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_schwer(x, x);
                break;
            default:
                System.err.println("Bot Auswahl Fehler!!");

        }
        switch (bot2) {
            case 1:
                WUNDERVOLLERGEGNERBOT = new RDM_Bot(x, x);
                break;
            case 2:
                WUNDERVOLLERGEGNERBOT = new Bot_lvl_2(x, x);
                break;
            case 3:
                WUNDERVOLLERGEGNERBOT = new Bot_nightmare(x, x, ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel);
                break;
            case 4:
                WUNDERVOLLERGEGNERBOT = new Bot_schwer(x, x);
                break;
            default:
                System.err.println("Bot Auswahl Fehler!!");

        }
        if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST instanceof Bot_nightmare) {
            ((Bot_nightmare) ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST).cheat = WUNDERVOLLERGEGNERBOT.dasSpiel;
        }
        int[] add = Bot.calcships(x, x);
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.shipSizesToAdd(add);
        //ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getDasSpiel().setVerbose(false);
        WUNDERVOLLERGEGNERBOT.shipSizesToAdd(add);
        //WUNDERVOLLERGEGNERBOT.getDasSpiel().setVerbose(false);
        feld = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld();
    }

    /**
     * intitialliesiert die beiden Spielfelder
     */
    public void Gridinit() {
        //initialisieren Label und Grid (1)
        GameGrid = new GridPane();
        labels = new Label[x][x];
        //initialisieren Label und Grid (2)
        GameGrid2 = new GridPane();
        labels2 = new Label[x][x];
        //lustige scale sachen
        for (int a = x - 1; a >= 0; a--) {
            //System.out.println("for 2");
            for (int b = x - 1; b >= 0; b--) {

                //Game Labels (1)
                labels[a][b] = new Label();
                labels[a][b].setMinSize(methoden.minsizeberechner(x), methoden.minsizeberechner(x));
                //labels[a][b].setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

                //labels[a][b].setStyle("-fx-background-color: #03fcf4");
                labels[a][b] = methoden.textureWasser(labels[a][b], x);

                //lustige bilder zu den Labels
                /*
                Image img = new Image("GUI/Textures/WundervollesWasser.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(methoden.minsizeberechner(x));
                view.setPreserveRatio(true);
                labels[a][b].setGraphic(view);

                 */

                //Wasser texture setzen
                //labels[a][b] = methoden.textureWasser(labels[a][b],x);


                GridPane.setConstraints(labels[a][b], a, b, 1, 1, HPos.CENTER, VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);

                //Game Labels (2)
                labels2[a][b] = new Label();
                //labels2[a][b].setMinSize(50,50);
                labels2[a][b].setMinSize(methoden.minsizeberechner(x), methoden.minsizeberechner(x));

                //labels2[a][b].setStyle("-fx-background-color: #03fcf4");

                //Wundervolles Image
                /*
                Image img2 = new Image("GUI/Textures/42.jpg");
                ImageView view2 = new ImageView(img2);
                view2.setFitHeight(minsizeberechner());
                view2.setPreserveRatio(true);
                labels2[a][b].setGraphic(view2);

                 */
                labels2[a][b] = methoden.textureWasser(labels2[a][b], x);


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

    /**
     * Funktion welche das Spiel startet
     * @param event
     */
    public void gameStart(ActionEvent event) {
        gameStartButton.setVisible(false);
        /*
        if (!load) {
            WUNDERVOLLERGEGNERBOT.dasSpiel.setAbschussSpieler((ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getAbschussSpieler() == 1) ? 0 : 1);
        }

         */
        WUNDERVOLLERGEGNERBOT.dasSpiel.setAbschussSpieler((ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getAbschussSpieler() == 1) ? 0 : 1);
        initoneSecondsWonder();
    }

    /**
     * Initialisiert die Timeline oneSecondsWonder
     */
    private void initoneSecondsWonder() {
        oneSecondsWonder = new Timeline(new KeyFrame(Duration.millis(speed), e -> {
            if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.isOver() || WUNDERVOLLERGEGNERBOT.isFinOver()) {
                oneSecondsWonder.stop();
                System.out.println("SPIEL ENDE");
            } else {
                shoot();
            }
        }));
        oneSecondsWonder.setCycleCount(Animation.INDEFINITE);
        oneSecondsWonder.play();
    }

    /**
     * laesst je nach dem welcher Bot am zug ist diessen schiessen
     */
    private void shoot() {
        if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getAbschussSpieler() == 0) {
            int[] xy = WUNDERVOLLERGEGNERBOT.getSchuss();
            if (!ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.shoot(xy[0], xy[1], 0, 0, false)) {
                return;
            }
            WUNDERVOLLERGEGNERBOT.setSchussFeld(xy[0], xy[1], feld[0][xy[0]][xy[1]], ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.istVersenkt());
            if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.isOver()) {
                methoden.GameEnd(true);
            }
        } else {
            int[] xy = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getSchuss();
            if (!WUNDERVOLLERGEGNERBOT.dasSpiel.shoot(xy[0], xy[1], 0, 0, false)) {
                return;
            }
            ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.setSchussFeld(xy[0], xy[1], WUNDERVOLLERGEGNERBOT.dasSpiel.getFeld()[0][xy[0]][xy[1]], WUNDERVOLLERGEGNERBOT.dasSpiel.istVersenkt());
            if (WUNDERVOLLERGEGNERBOT.dasSpiel.isOver()) {
                methoden.GameEnd(false);
            }
        }
        GridUpdater();
    }

    /**
     * Speichert das Spiel
     * @param event
     */
    public void Speichern(ActionEvent event) {
        if (oneSecondsWonder != null) {
            oneSecondsWonder.stop();
        }
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
            name = name + "-B";
            System.out.println("Name: " + name);
            String hash = "" + this.hashCode();
            new SAFE_SOME(new Bot[]{ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST,WUNDERVOLLERGEGNERBOT},null,3,hash,name);
            //new SaveGame(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST, WUNDERVOLLERGEGNERBOT, name);
            /*
            GOETTLICHESSPIELDERVERNICHTUNGMITbot.saveGame(name+"-S");
            ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.saveGame(name+"-B");
             */
            newStage.close();
            oneSecondsWonder.play();
        });
        comp.getChildren().add(DateiName);
        comp.getChildren().add(Save);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
    }

    /**
     * Button um zurueck zum BOTgegenBOTMenu zu kommen
     * @param event
     * @throws IOException
     */
    public void BacktoMenu(ActionEvent event) throws IOException {
        if (oneSecondsWonder != null) {
            oneSecondsWonder.stop();
        }
        Parent root = FXMLLoader.load(getClass().getResource("BOTgegenBOTMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("BOTgegenBOTMenu");
        window.show();
    }

    public void printfeld(ActionEvent event) {
        logic.logicOUTput.printFeld(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld(), true);
    }
}
