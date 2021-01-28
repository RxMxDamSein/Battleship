package GUI;

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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.*;

import logic.save.SAFE_SOME;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Klasse für das Spiel: Spieler gegen Bot
 */
public class GameGridController implements Initializable, Serializable {
    private static final long serialVersionUID = 1337L;
    //@FXML private AnchorPane anchoroanegamegrid;
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
     * Button zum Speichern
     */
    @FXML
    private Button speicherbutton;
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
     * x ist die Spielfeldgroesse, bot ist welche bot-schwierigkeit ausgewählt worden ist und count ist eine einfach Zähl Variable.
     */
    private Integer x, bot, count = 0;
    /**
     * Variablen zum setzen von Schiffen
     */
    private int sx = -1, sy = -1, ex = -1, ey = -1;
    /**
     * zusatz Klasse mit verschiedenen Funktionen
     */
    private nuetzlicheMethoden methoden;
    /**
     * Ein Spiel aus dem Logic Packege
     */
    private Spiel GOETTLICHESSPIELDERVERNICHTUNGMITbot;
    /**
     * Eins Bot aus dem Logic Packege
     */
    private Bot ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST;

    public GameGridController() { }

    /**
     * setzt die Variablen, welche zuvor vom User festgelegt wurden
     * @param a Größe des Spielfelds
     * @param b Botschwierigkeit
     */
    public void setInteger(Integer a, Integer b) {
        methoden = new nuetzlicheMethoden(a);
        x = a;
        bot = b;
        Gridinit();
        Spielinit();
        methoden.initspeichern(GOETTLICHESSPIELDERVERNICHTUNGMITbot,speicherbutton);
    }

    /**
     * Wird aufgerufen um ein Gespeichertes SAFE-Game zu laden
     * @param SAFE gespeichertes Spiel
     */
    public void gameloader(SAFE_SOME SAFE) {
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = SAFE.spiele[0];
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = SAFE.bots[0];
        /*
        SaveGame save = (SaveGame) SaveGame.load(id);
        Spiel s = save.g;
        Bot b = save.b;

         */
        /*
        Bot b =(Bot) Bot.load(id+"-B");
        Spiel s = Spiel.load(id+"-S");
         */
        x = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getSizeX();
        methoden = new nuetzlicheMethoden(x);
        methoden.initspeichern(GOETTLICHESSPIELDERVERNICHTUNGMITbot,speicherbutton);
        //GOETTLICHESSPIELDERVERNICHTUNGMITbot = s;
        //ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = b;

        Gridinit();
        GridUpdater();

        int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
        if (spieler != -1) {
            spielstatus = true;
        }

        //System.out.println("Abschussspieler: "+GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler());
        if (!GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver() && spieler == 0) {
            //System.out.println("BOT SCHUSS NACH LADEN°°°°°°°°");
            Botschiesst();
        }
        if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver() || ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.isFinOver())
            System.out.println("SPIEL ENDE");


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

    /**
     * Startet das Spiel und legt fest welcher Bot verwendet wird.
     */
    public void Spielinit() {
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = new Spiel(x, x, true);
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.setVerbose(false);
        switch (bot) {
            case 1:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new RDM_Bot(x, x);
                break;
            case 2:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_lvl_2(x, x);
                break;
            case 3:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_nightmare(x, x, GOETTLICHESSPIELDERVERNICHTUNGMITbot);
                break;
            case 4:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_schwer(x, x);
                break;
            default:
                System.err.println("Bot Auswahl Fehler!!");

        }
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getDasSpiel().setVerbose(false);
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.init();
    }

    //Grid und Labels initialisieren
    /**
     * initialisiert die beiden Spielfelder des Spiels
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
     * Methode zum schießen auf dem linken Grid.
     * @param a x-Koordinate
     * @param b y-Koordinate
     */
    private void label2click(int a, int b) {
        int phit;
        boolean schuss;
        System.out.println("Grid 2 pressed in x: " + a + " y: " + b);
        if (spielstatus) {
            int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
            //spieler 0 = Bot
            // Spieler schießt auf Bot
            if (spieler == 1) {
                //phit: 4 = Versenkt
                phit = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.abschiesen(a, b);
                //System.out.println("phit: "+phit);
                //System.out.println("Botfeld: ");


                if (phit == 4) {
                    GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(a, b, GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler(), 1, true);
                    //labels2[a][b].setStyle("-fx-background-color: black");
                    //labels2[a][b] = methoden.textureversenkt(labels2[a][b],x);
                    GridUpdater();
                    System.out.println("TREFFER VERSENKT!!");
                    if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.isFinOver()) {
                        //GOETTLICHESSPIELDERVERNICHTUNGMITbot.setGameOver();
                        System.out.println("DU HAST GEWONNEN!!");
                        GameTopLabel1.setStyle("-fx-background-color: red");
                        GameTopLabel1.setText("DU HAST GEWONNEN!!!");
                        methoden.GameEnd(true);
                    }
                } else {
                    GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(a, b, GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler(), phit, false);
                    if (phit == 1) {
                        //labels2[a][b].setStyle("-fx-background-color: red");
                        GridUpdater();
                        //labels2[a][b] = methoden.textureSchiffTreffer(labels2[a][b],x);
                        System.out.println("TREFFER!!");
                    }
                    if (phit == 3 || phit == 0) {
                        //Treffer auf Wasser
                        System.out.println("TREFFER WASSER!!");
                        //labels2[a][b].setStyle("-fx-background-color: blue");
                        GridUpdater();
                        //labels2[a][b] = methoden.textureWasserTreffer(labels2[a][b],x);
                    }
                }
            }

            //System.out.println("nach Spieler schuss");
            spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
            if (spieler == 0)
                Botschiesst();
        }
    }

    /**
     * Methode, welche das schießen des Bot uebernimmt
     */
    private void Botschiesst() {
        //Bot schießt auf Spieler
        if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler() == 1) {
            return;
        }
        System.out.println("Bot schießt");
        int[] xy = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getSchuss();
        System.out.println("Bot schießt auf: x: " + xy[0] + " y: " + xy[1]);

        if (!GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(xy[0], xy[1], 0, 0, false)) {
            System.err.println("BOT schuss fehler");
            return;
        }
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.setSchussFeld(xy[0], xy[1], GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld()[0][xy[0]][xy[1]], GOETTLICHESSPIELDERVERNICHTUNGMITbot.istVersenkt());
        //3 wasser, 2 schiff
        int hit = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld()[0][xy[0]][xy[1]];
        System.out.println("Bot Hit: " + hit);
        GridUpdater();
        if (hit == 3 || hit == 0) {
            //Treffer auf Wasser
            GridUpdater();
            //labels[xy[0]][xy[1]] = methoden.textureWasserTreffer(labels[xy[0]][xy[1]],x);
            //labels[xy[0]][xy[1]].setStyle("-fx-background-color: blue");
            logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(), true);
            return;
        }
        //Treffer auf Schiff
        if (hit == 2 || hit == 1 || hit == 4) {
            if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.istVersenkt()) {
                GridUpdater();
                //labels[xy[0]][xy[1]] = methoden.textureversenkt(labels[xy[0]][xy[1]],x);
                //labels[xy[0]][xy[1]].setStyle("-fx-background-color: black");
                System.out.println("BOT: TREFFER VERSENKT!!");
                if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver()) {
                    //Bot hat gewonnen
                    System.out.println("BOT HAT GEWONNEN!!");
                    GameTopLabel1.setStyle("-fx-background-color: red");
                    GameTopLabel1.setText("BOT HAT GEWONNEN!!!");
                    methoden.GameEnd(false);
                    return;
                }
                if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler() == 0) {
                    Botschiesst();
                }
                logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(), true);
                return;
            }
            GridUpdater();
            //labels[xy[0]][xy[1]] = methoden.textureSchiffTreffer(labels[xy[0]][xy[1]],x);
            //labels[xy[0]][xy[1]].setStyle("-fx-background-color: red");
            System.out.println("BOT: TREFFER!!");
            logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(), true);
            Botschiesst();
            return;
        }

    }

    /**
     * Methode zum plazieren eines Schiffes
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
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(sx, sy, false, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
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
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(ex, ey, false, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
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
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(sx, sy, true, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
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
                System.out.println("Size: " + size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(ex, ey, true, size, 0);
                System.out.println(shippaddo);
                if (!shippaddo) {
                    illegalesSchiff();
                    return;
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
     * Methode zum zurücksetzen der Auswahl bei illigalem Schiff
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
     * Methode zum setzten der Schiffe, durch klicken  auf zwei verschiedene Labels
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

        /*
        int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
        if(spieler == 0) {
            if(GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(a,b,1,0,false)) {
                labels[a][b].setStyle("-fx-background-color: red");
            }
            labels[a][b].setStyle("-fx-background-color: pink");
        }

         */

    }

    //versetzt Spiel in Feuermodus

    /**
     * Start-Knopf des Spiels
     * @param event
     */
    public void gameStart(ActionEvent event) {
        if(GOETTLICHESSPIELDERVERNICHTUNGMITbot.isStarted()){
            gameStartButton.setVisible(false);
            return;
        }
        spielstatus = true;
        System.out.println("Spielstatus: " + spielstatus);
        GameTopLabel1.setText("Du schießt jetzt hier:");
        if (!ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.shipSizesToAdd(Bot.getShipSizes(GOETTLICHESSPIELDERVERNICHTUNGMITbot.schiffe))) {
            System.err.println("Bot Schiffe fehler");
            return;
        }
        gameStartButton.setVisible(false);
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.starteSpiel();
        if(bot==3){//man hat 1 chance gegen den Nightmarebot
            GOETTLICHESSPIELDERVERNICHTUNGMITbot.setAbschussSpieler(1);
            ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.setAbschussSpieler(0);
        }
        GridUpdater();
        /*
        System.out.println("GAMEOVER: "+GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver());
        int[] penis = Bot.getShipSizes(GOETTLICHESSPIELDERVERNICHTUNGMITbot.schiffe);
        System.out.println("Schiff anzahl: "+penis.length);
        for (int i = 0;i != penis.length;i++) {
        System.out.println("Schiff "+i+" größe: "+penis[i]);
        }
         */
        int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
        //System.out.println("Spieler: "+spieler);
        System.out.println("Spieler: " + spieler);
        GameTopLabel.setText("Spieler: " + spieler);
        if (spieler == 0) {
            GameTopLabel.setText("Bot schießt");
            Botschiesst();
            GameTopLabel.setText("Du schießt");
        }
    }

    /**
     * Button um zuruck zum EinzelspielerMenu zu kommen.
     * @param event
     * @throws IOException
     */
    public void BacktoMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("EinzelspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("EinzelspielerMenu");
        window.show();
    }

    /**
     * initialize von JavaFX
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //nuetzlicheMethoden.setStageCenter();
        speicherbutton.setVisible(false);
    }




    /**
     * Methode zum speicheren des Spiels
     * @param event
     * @throws IOException
     */
    public void Speichern(ActionEvent event) throws IOException {
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
            name = name + "-S";
            System.out.println("Name: " + name);
            String hash = "" + this.hashCode();
            new SAFE_SOME(new Bot[]{ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST},new Spiel[]{GOETTLICHESSPIELDERVERNICHTUNGMITbot},2,hash,name);
            //new SaveGame(GOETTLICHESSPIELDERVERNICHTUNGMITbot, ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST, name);
            /*
            GOETTLICHESSPIELDERVERNICHTUNGMITbot.saveGame(name+"-S");
            ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.saveGame(name+"-B");
             */
            newStage.close();
        });
        Label label = new Label("Dateiname:");
        label.setFont(new Font("System",14));
        comp.getChildren().add(label);
        comp.getChildren().add(DateiName);
        comp.getChildren().add(Save);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
    }
}
