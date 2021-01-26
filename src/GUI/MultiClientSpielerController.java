package GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
 * Klasse für das Spiel: Mehrspieler-Client-Spieler
 */
public class MultiClientSpielerController implements Initializable, Serializable {
    private static final long serialVersionUID = 1337L;
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
     */
    private boolean spielstatus = false;
    /**
     * linkes Spielfeld
     */
    private GridPane GameGrid;
    /**
     * rechtes Spielfeld
     */
    private GridPane GameGrid2;
    /**
     * Labels des linken Spielfelds zum anzeigen des Spiels
     */
    private Label[][] labels;
    /**
     * Labels des rechten Spielfelds zum anzeigen des Spiels
     */
    private Label[][] labels2;
    /**
     * x ist die Spielfeldgroesse, count ist eine einfach Zähl Variable.
     */
    private Integer x, count = 0;
    /**
     * Variablen zum setzen von Schiffen
     */
    private int sx = -1, sy = -1, ex = -1, ey = -1;
    /**
     * zusatz Klasse mit verschiedenen Funktionen
     */
    private nuetzlicheMethoden methoden;
    /**
     * Ein Spiel aus dem Logic Package
     */
    private Spiel GOETTLICHESSPIELDERVERNICHTUNGMITbot;
    /**
     * updateTimeline zum aktualisieren der Spielfelder und time zum aufrufen von shiplabel()
     */
    private Timeline updateTimeline,time;
    /**
     * KLasse Cleint, welche die Verbindung mit Host uebernimmt
     */
    private Client Client;
    private int count1=0;
    /**
     * Funktion um am linken Toplabel anzuzeigen, welche schiffe plaziert werden duerfen
     */
    public void shipLabel() {
        StringBuilder s = new StringBuilder();
        s.append(Client.ships.length - count1 + " Schiffe mit groesse: ");
        for (int i = 0; i < Client.ships.length; i++) {
            if (Client.ships[i] == -1 ) {
                continue;
            }
            if (i != Client.ships.length - 1) {
                s.append(" " + Client.ships[i]);
                s.append(",");
                continue;
            }
            s.append(" " + Client.ships[i]);
        }
        System.out.println(s.toString());
        GameTopLabel.setText(s.toString());

        if (count1 - Client.ships.length == 0) {
            gameStartButton.setVisible(true);
        }
        count1++;
    }

    /**
     * initialisiert updateTimeline
     */
    private void initupdateTimeline() {
        if (updateTimeline != null) {
            System.err.println("Timeline existiert bereits!!!");
            return;
        }
        updateTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver()) {
                //Client.CutConnection();
                if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler() == 0) {
                    methoden.GameEnd(false);
                } else {
                    methoden.GameEnd(true);
                }
                GridUpdater();
                updateTimeline.stop();
            } else if (Client.change) {
                Client.change = false;
                GridUpdater();
            }
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.play();
    }
    /**
     * initialisiert das Spiel
     * @param Client Client aus dem JoinMenu
     */
    public void setVariables(Client Client) {
        gameStartButton.setVisible(false);
        GameTopLabel.setAlignment(Pos.CENTER);
        methoden = new nuetzlicheMethoden(Client.dasSpiel.getSizeX());
        x = Client.dasSpiel.getSizeX();
        //bot = b;
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = Client.dasSpiel;
        Gridinit();
        //Spielinit(); wurde davor schon erledigt
        this.Client = Client;
        //Client.init(); wurde davor schon erledigt
        if (Client.loaded) {
            GridUpdater();
            initupdateTimeline();
            spielstatus = true;
            return;
        }
        time = new Timeline(new KeyFrame(new Duration(100), event -> {
            if (Client.status == 2) {
                shipLabel();
                time.stop();
            }
        }));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
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
    /**
     * Funktion zum Schiessen des Client, wenn man auf ein Label Clickt
     * @param a x-Koordinate
     * @param b y-Koordinate
     */
    private void label2click(int a, int b) {
        System.out.println("Grid 2 pressed in x: " + a + " y: " + b);
        Client.schuss(a, b);
    }

    /**
     * Funktion zum setzen eines Schiffes
     */
    public void shippplace() {
        boolean shippaddo;
        int size;
        System.out.println("Place ship");
        System.out.println("sx= " + sx + " sy= " + sy + " ex= " + ex + " ey= " + ey);
        System.out.println("________________________________________________________ ");
        //sx == ex horizontal
        //sy == ey vertikal
        //sonst fail
        if (sx == -1 || sy == -1 || ex == -1 || ey == -1 || sx == ex && sy == ey) {
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
                    shipLabel();
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
                    shipLabel();
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
                    shipLabel();
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
                    shipLabel();
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


    //ActionHandler für Label 1 (Grid 1) gedrückt
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
        methoden.setAbschussLabelTimeline(GOETTLICHESSPIELDERVERNICHTUNGMITbot, GameTopLabel, GameTopLabel1);
        int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
        //System.out.println("Spieler: "+spieler);
        System.out.println("AbschussSpieler: " + spieler);
        GridUpdater();
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
            System.out.println("timeline should be stopped!");
        }
        time.stop();

        Client.CutConnection();

        Parent root = FXMLLoader.load(getClass().getResource("MehrspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("JoinMenu");
        window.show();
    }

    /**
     * initialize Funktion von JavaFX
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        if (Client.dasSpiel.getAbschussSpieler() == 0) {
            System.err.println("Du kannst nur speichern, wenn du dran bist!");
            return;
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
        DateiName.setText("Dateiname");
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
