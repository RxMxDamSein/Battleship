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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.concurrent.TimeUnit;
/**
 * Klasse für das Spiel: Mehrspieler-Client-Bot
 */
public class MultiClientBotController implements Initializable, Serializable {
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
    private boolean spielstatus = false,changingSlider=false;
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
     * count ist eine einfach Zaehl-Variable
     */
    private Integer x, bot, count = 0;
    /**
     * Variablen zum setzen von Schiffen
     */
    private int sx = -1, sy = -1, ex = -1, ey = -1;
    /**
     * Standartgeschwindigkeit der Timeline
     */
    private static final int sleeptime0 = 1000;
    /**
     * neu eingestellte Geschwindigkeit fuer den Bot
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
     * updateTimeline um das Spiel zu aktualissieren
     */
    private Timeline updateTimeline;
    /**
     * BotClient-Klasse, welche die Verbindung zum Host uebernimmt
     */
    private BotClient Client;


    /**
     * int Variable um ein Upadete zu foren
     */
    private int forceUpdate=0;
    /**
     * initialisiert und startet die updateTimeline
     */
    private void initupdateTimeline() {
        if (updateTimeline != null) {
            System.err.println("Timeline existiert bereits!!!");
            return;
        }
        updateTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if(forceUpdate%10==0)
                GridUpdater();
            forceUpdate++;
            if (Client.change) {
                GridUpdater();
                Client.change = false;

                //GridUpdater();
                //System.out.println("update!");

            }
            if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver()) {
                Client.CutConnection();
                if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler() == 0) {
                    methoden.GameEnd(false);
                } else {
                    methoden.GameEnd(true);
                }
                GridUpdater();
                updateTimeline.stop();
                return;
            }
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.setDelay(Duration.millis((sleeptime>200)?sleeptime/4:50));
        updateTimeline.play();
    }

    /**
     * initialisiert das Spiel
     * @param Client BotClient aus dem JoinMenu
     */
    public void setVariables(BotClient Client) {
        sleeptime=sleeptime0;
        methoden = new nuetzlicheMethoden(Client.dasSpiel.getSizeX());
        x = Client.dasSpiel.getSizeX();
        //bot = b;
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = Client.dasSpiel;
        Gridinit();
        //Spielinit(); wurde davor schon erledigt
        this.Client = Client;
        //Client.init(); wurde davor schon erledigt
        GridUpdater();
        initupdateTimeline();
        spielstatus = true;
    }

    /**
     * einfache Zaehlvariable
     */
    private int ccount=0;
    /**
     * Aktuallisiert die beiden Grids des Spiels.
     */
    public void GridUpdater() {

        int feld[][][] =Client.dasSpiel.getFeld();
        //for (int s=0;s<2;s++){
       System.out.println("UPDATE "+ccount++);
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
                labels[a][b].requestLayout();
                labels2[a][b].requestLayout();
            }
        }
        //}

    }


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

    /**
     * Funktion zum setzen eines Schiffes
     */
    public void shippplace() {
        boolean shippaddo;
        int size;
        System.out.println("Place ship");
        System.out.println("sx= " + sx + " sy= " + sy + " ex= " + ex + " ey= " + ey);
        System.out.println("");
        //sx == ex horizontal
        //sy == ey vertikal
        //sonst fail
        if (sx == -1 || sy == -1 || ex == -1 || ey == -1) {
            System.err.println("Ungültiges Schiff");
            //labels[sx][sy].setStyle("-fx-background-color: blue");
            //labels[ex][ey].setStyle("-fx-background-color: blue");
            labels[sx][sy] = methoden.textureWasser(labels[sx][sy], x);
            labels[ex][ey] = methoden.textureWasser(labels[ex][ey], x);
            sx = -1;
            sy = -1;
            ex = -1;
            ey = -1;
            return;
        }
        if (sx == ex) {
            System.out.println("Vertikal Schiff");
            System.out.println("ey= " + ey + " sy= " + sy);
            //Schiff geht nach
            if (ey > sy) {
                size = ey - sy + 1;
                if (!Client.inShips(size)) {
                    illegalesSchiff();
                    return;
                }
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(sx, sy, false, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
                } else {
                    Client.deleteShip(size);
                }
                for (int i = ey; i != sy - 1; i--) {
                    //System.out.println("PENIS 1");
                    //System.out.println("i= " + i);
                    labels[sx][i] = methoden.textureSchiff(labels[sx][i], x);
                    //labels[sx][i].setStyle("-fx-background-color: grey");
                }
            }
            if (sy > ey) {
                size = sy - ey + 1;
                if (!Client.inShips(size)) {
                    illegalesSchiff();
                    return;
                }

                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(ex, ey, false, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
                } else {
                    Client.deleteShip(size);
                }
                for (int i = sy; i != ey - 1; i--) {
                    //System.out.println("PENIS 2");
                    //System.out.println("i= "+i);
                    //labels[sx][i].setStyle("-fx-background-color: grey");
                    labels[sx][i] = methoden.textureSchiff(labels[sx][i], x);
                }
            }
            sx = -1;
            sy = -1;
            ex = -1;
            ey = -1;
            return;
        }
        if (sy == ey) {
            System.out.println("Horizontal Schiff");
            if (ex > sx) {
                size = ex - sx + 1;
                if (!Client.inShips(size)) {
                    illegalesSchiff();
                    return;
                }
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(sx, sy, true, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
                } else {
                    Client.deleteShip(size);
                }
                for (int i = ex; i != sx - 1; i--) {
                    //System.out.println("i= " + i);
                    //System.out.println("KAKA 1");
                    //labels[i][sy].setStyle("-fx-background-color: grey");
                    labels[i][sy] = methoden.textureSchiff(labels[i][sy], x);
                }
            }
            if (ex < sx) {
                size = sx - ex + 1;
                if (!Client.inShips(size)) {
                    illegalesSchiff();
                    return;
                }
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(ex, ey, true, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
                } else {
                    Client.deleteShip(size);
                }
                for (int i = sx; i != ex - 1; i--) {
                    //System.out.println("KAKA 2");
                    //System.out.println("i= "+i);
                    //labels[i][sy].setStyle("-fx-background-color: grey");
                    labels[i][sy] = methoden.textureSchiff(labels[i][sy], x);
                }
            }
            sx = -1;
            sy = -1;
            ex = -1;
            ey = -1;
            return;
        }
        //System.out.println("Ungültiges Schiff");
        illegalesSchiff();
    }

    /**
     * Funktion wenn man ein illegales Schiff gesetzt hat
     */
    private void illegalesSchiff() {
        System.err.println("Ungültiges Schiff");
        //labels[sx][sy].setStyle("-fx-background-color: #03fcf4");
        //labels[ex][ey].setStyle("-fx-background-color: #03fcf4");
        labels[sx][sy] = methoden.textureWasser(labels[sx][sy], x);
        labels[ex][ey] = methoden.textureWasser(labels[ex][ey], x);
        sx = -1;
        sy = -1;
        ex = -1;
        ey = -1;
    }


    /**
     * ActionHandler für Label 1, um die zwei Koordinaten zum plazieren eines Schiffes zu bekommen
     * @param a x-Koordinate
     * @param b y-Koordinate
     */
    private void labelclick(int a, int b) {
        System.out.println("x= " + a + " y= " + b);

        // sx,sy,ex,ey
        if (!spielstatus) {
            if (count == 0) {
                sx = a;
                sy = b;
                //labels[a][b].setStyle("-fx-background-color: white");
                labels[a][b] = methoden.textureauswahlWasser(labels[a][b], x);
                count++;
                return;
            }
            if (count == 1) {
                ex = a;
                ey = b;
                //labels[a][b].setStyle("-fx-background-color: white");
                labels[a][b] = methoden.textureauswahlWasser(labels[a][b], x);
                count = 0;
                shippplace();
            }
        }
    }

    //versetzt Spiel in Feuermodus

    /**
     * Startet das Spiel
     * @param event
     */
    public void gameStart(ActionEvent event) {
        if (spielstatus) {
            System.err.println("Spiel bereits im gange!!");
            return;
        }


        if (!Client.senships()) {
            System.err.println("Es wurden nicht alle Schiffe hinzugefügt!");
            return;
        }
        spielstatus = true;
        gameStartButton.setVisible(false);
        System.out.println("Spielstatus: " + spielstatus);
        GameTopLabel1.setText("Du schießt jetzt hier:");
        int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
        System.out.println("Spieler: " + spieler);
        GameTopLabel.setText("Spieler: " + spieler);
        if (spieler == 0) {
            GameTopLabel.setText("Bot schießt");
            //Clinet Schießt
            GameTopLabel.setText("Du schießt");
        }
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

        Client.CutConnection();

        Parent root = FXMLLoader.load(getClass().getResource("MehrspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();
    }

    /**
     *  initialize methode von JavaFX, welche den Botslider einstellt
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameStartButton.setVisible(false);
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
        System.out.println("NewSleeptime: " +sleeptime );
        if(updateTimeline!=null)
            updateTimeline.setDelay(Duration.millis((sleeptime>200)?sleeptime/4:50));
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
     */
    public void Speichern(ActionEvent event) throws IOException {
        if (Client.dasSpiel.getAbschussSpieler() == 0) {
            System.err.println("Du kannst nur speichern, wenn du dran bist!");
            return;
        }
        Client.pause = true;
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

            Client.save(hash, name);
            newStage.close();
        });
        comp.getChildren().add(DateiName);
        comp.getChildren().add(Save);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
    }
}
