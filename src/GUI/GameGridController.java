package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Bot;
import logic.RDM_Bot;
import logic.Spiel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class GameGridController implements Initializable {
    //@FXML private AnchorPane anchoroanegamegrid;
    @FXML private StackPane StackPane;
    @FXML private StackPane StackPane2;
    @FXML private Button placebutton;
    @FXML private  Label GameTopLabel;
    @FXML private Label GameTopLabel1;
    private boolean spielstatus=false;
    private GridPane GameGrid;
    private GridPane GameGrid2;
    private Label[ ][ ] labels;
    private Label[ ][ ] labels2;
    private Integer x,y;
    private int sx=-1,sy=-1,ex=-1,ey=-1;

    private Spiel GOETTLICHESSPIELDERVERNICHTUNGMITbot;
    private RDM_Bot ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST;

    public GameGridController() {}
    public void setInteger(Integer a,Integer b) {
        x=a;
        y=b;
        Gridinit();
        Spielinit();
    }

    private void Spielinit() {
        GOETTLICHESSPIELDERVERNICHTUNGMITbot = new Spiel(x,y,true);
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new RDM_Bot(x,y);
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.init();
    }

    //Grid und Labels initialisieren
    public void Gridinit() {
        //initialisieren Label und Grid (1)
        GameGrid = new GridPane();
        labels = new Label[x][y];
        //initialisieren Label und Grid (2)
        GameGrid2 = new GridPane();
        labels2 = new Label[x][y];
        for(int a=x-1;a >= 0;a--) {
            //System.out.println("for 2");
            for(int b=y-1;b >= 0;b--) {

                //Game Labels (1)
                labels [a] [b] = new Label();
                labels[a][b].setMinSize(50,50);
                labels[a][b].setStyle("-fx-background-color: #03fcf4");
                //final int ca=a,cb=b;
                int ca=a,cb=b;
               labels[a][b].setOnMouseClicked(e -> labelclick(ca,cb) );
                GridPane.setConstraints(labels[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);

                //Game Labels (2)
                labels2 [a] [b] = new Label();
                labels2[a][b].setMinSize(50,50);
                labels2[a][b].setStyle("-fx-background-color: #03fcf4");
                labels2 [a][b].setOnMouseClicked(e -> label2click(ca,cb));
                GridPane.setConstraints(labels2[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid2.getChildren().add(labels2[a][b]);
            }
        }
        // GameGrid 1 zu Stackpane 1 hinzufügen
        GameGrid.setAlignment(Pos.CENTER);
        GameGrid.setHgap(1);
        GameGrid.setVgap(1);
        StackPane.getChildren().add(GameGrid);
        // GameGrid 2 zu Stackpane 2 hinzufügen
        GameGrid2.setAlignment(Pos.CENTER);
        GameGrid2.setHgap(1);
        GameGrid2.setVgap(1);
        StackPane2.getChildren().add(GameGrid2);
    }
    //ActionHandler für Label 2 (Grid 2) gedrückt
    private void label2click(int a, int b) {
        int phit;
        boolean schuss;
        System.out.println("Grid 2 pressed in x: "+a+" y: "+b);
        if (spielstatus) {
            int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
            //spieler 0 = Bot
            // Spieler schießt auf Bot
            if(spieler == 1) {
                //phit: 4 = Versenkt
                phit = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.abschiesen(a,b);
                System.out.println("phit: "+phit);
                //System.out.println("Botfeld: ");
                if (phit == 4) {
                    GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(a,b, GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler(),1,true);
                    labels2[a][b].setStyle("-fx-background-color: black");
                    System.out.println("TREFFER VERSENKT!!");
                    if(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.isFinOver()) {
                        GOETTLICHESSPIELDERVERNICHTUNGMITbot.setGameOver();
                        System.out.println("DU HAST GEWONNEN!!");
                        GameTopLabel1.setText("DU HAST GEWONNEN!!!");
                        /*
                        try {
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.exit();

                         */
                    }
                } else {
                    GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(a,b, GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler(),phit,false);
                    if (phit == 1) {
                        labels2[a][b].setStyle("-fx-background-color: red");
                        System.out.println("TREFFER!!");
                    }
                    if (phit == 3 || phit == 0) {
                        //Treffer auf Wasser
                        System.out.println("TREFFER WASSER!!");
                        labels2[a][b].setStyle("-fx-background-color: blue");
                    }
                }
            }
            System.out.println("nach Spieler schuss");
            spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
            if (spieler == 0)
                Botschiesst();
        }
    }

    private void Botschiesst() {
        //Bot schießt auf Spieler
        System.out.println("Bot schießt");
        int[] xy = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getSchuss();
        System.out.println("Bot schießt auf: x: "+xy[0]+" y: "+xy[1]);

        if (!GOETTLICHESSPIELDERVERNICHTUNGMITbot.shoot(xy[0],xy[1], 0,0,false)) {
            System.err.println("BOT schuss fehler");
            return;
        }
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.setSchussFeld(xy[0],xy[1],GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld()[0][xy[0]][xy[1]],GOETTLICHESSPIELDERVERNICHTUNGMITbot.istVersenkt());
        //3 wasser, 2 schiff
        int hit = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld()[0][xy[0]][xy[1]];
        System.out.println("Bot Hit: "+hit);
        if (hit == 3 || hit == 0) {
            //Treffer auf Wasser

            labels[xy[0]][xy[1]].setStyle("-fx-background-color: blue");
            logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(),true);
            return;
        }
        //Treffer auf Schiff
        if (hit == 2 || hit == 1) {
            if (GOETTLICHESSPIELDERVERNICHTUNGMITbot.istVersenkt()) {
                labels[xy[0]][xy[1]].setStyle("-fx-background-color: black");
                System.out.println("BOT: TREFFER VERSENKT!!");
                if(GOETTLICHESSPIELDERVERNICHTUNGMITbot.isOver()) {
                    //Bot hat gewonnen
                    System.out.println("BOT HAT GEWONNEN!!");
                    GameTopLabel1.setText("BOT HAT GEWONNEN!!!");
                    /*
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.exit();

                     */
                }
                logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(),true);
                return;
            }

            labels[xy[0]][xy[1]].setStyle("-fx-background-color: red");
            System.out.println("BOT: TREFFER!!");
            logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(),true);
            Botschiesst();
            return;
        }

    }

/*
    private void DragExit(int ca, int cb) {
        System.out.println("Drag exito x= "+ca+" y= "+cb);
    }

    private void DragEntered(int ca, int cb) {
        System.out.println("Drago Entro x= "+ca+" y= "+cb);
    }

    private void DragDetected(int ca, int cb) {
        System.out.println("Darg in x: "+ca +" y: "+cb);
    }

    private void MouseDragReleased(int a, int b) {
        int cx,dy;
        drx = a;
        dry = b;
        cx = drx - dex;
        dy = dry - dey;
        System.out.println("");
        System.out.println("Mous Pressed: x= "+dex+" y= "+dey);
        System.out.println("Mous Released: x= "+drx+" y= "+dry);
        System.out.println("Drag in x:"+cx);
        System.out.println("Drag in y:"+dy);
        System.out.println("");
    }

    private void MouseDragEnter(int a, int b) {
        dex=a;
        dey=b;

    }

 */

    public void place(ActionEvent event) {
        boolean shippaddo;
        int size;
        System.out.println("Place ship");
        System.out.println("sx= "+sx+" sy= "+sy+" ex= "+ex+" ey= "+ey);
        //sx == ex horizontal
        //sy == ey vertikal
        //sonst fail
        if (sx == -1 || sy == -1 || ex == -1 || ey == -1) {
            System.err.println("Ungültiges Schiff");
            labels[sx][sy].setStyle("-fx-background-color: blue");
            labels[ex][ey].setStyle("-fx-background-color: blue");
            sx=-1;
            sy=-1;
            ex=-1;
            ey=-1;
            return;
        }
        if(sx == ex) {
            System.out.println("Vertikal Schiff");
            System.out.println("ey= "+ey+" sy= " +sy);
            //Schiff geht nach
            if (ey > sy) {
                size = ey - sy+1;
                System.out.println("Size: "+size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(sx,sy,false,size,0);
                System.out.println(shippaddo);
                if(!shippaddo) {
                    illegalesSchiff();
                    return;
                }
                for (int i = ey; i != sy-1; i--) {
                    //System.out.println("PENIS 1");
                    //System.out.println("i= " + i);
                    labels[sx][i].setStyle("-fx-background-color: grey");
                }
            }
            if (sy > ey) {
                size = sy - ey+1;
                System.out.println("Size: "+size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(ex,ey,false,size,0);
                System.out.println(shippaddo);
                if(!shippaddo) {
                    illegalesSchiff();
                    return;
                }
                for (int i = sy; i != ey-1; i--) {
                    //System.out.println("PENIS 2");
                    //System.out.println("i= "+i);
                    labels[sx][i].setStyle("-fx-background-color: grey");
                }
            }
            sx=-1;
            sy=-1;
            ex=-1;
            ey=-1;
            return;
        }
        if(sy == ey) {
            System.out.println("Horizontal Schiff");
            if (ex > sx) {
                size = ex - sx+1;
                System.out.println("Size: "+size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(sx,sy,true,size,0);
                System.out.println(shippaddo);
                if(!shippaddo) {
                    illegalesSchiff();
                    return;
                }
                for (int i = ex; i != sx-1; i--) {
                    //System.out.println("i= " + i);
                    System.out.println("KAKA 1");
                    labels[i][sy].setStyle("-fx-background-color: grey");
                }
            }
            if (ex < sx) {
                size = sx - ex+1;
                System.out.println("Size: "+size);
                shippaddo = GOETTLICHESSPIELDERVERNICHTUNGMITbot.addShip(ex,ey,true,size,0);
                System.out.println(shippaddo);
                if(!shippaddo) {
                    illegalesSchiff();
                    return;
                }
                for (int i = sx; i != ex-1; i--) {
                    System.out.println("KAKA 2");
                    //System.out.println("i= "+i);
                    labels[i][sy].setStyle("-fx-background-color: grey");
                }
            }
            sx=-1;
            sy=-1;
            ex=-1;
            ey=-1;
            return;
        }
        //System.out.println("Ungültiges Schiff");
        illegalesSchiff();
    }

    private void illegalesSchiff() {
        System.err.println("Ungültiges Schiff");
        labels[sx][sy].setStyle("-fx-background-color: #03fcf4");
        labels[ex][ey].setStyle("-fx-background-color: #03fcf4");
        sx=-1;
        sy=-1;
        ex=-1;
        ey=-1;
    }


    //ActionHandler für Label 1 (Grid 1) gedrückt
    private void labelclick(int a, int b) {
        System.out.println("x= "+a+" y= "+b);

        // sx,sy,ex,ey
        if(!spielstatus) {
            if (sx == -1 && sy == -1) {
                sx = a;
                sy = b;
                labels[a][b].setStyle("-fx-background-color: white");
                return;
            }
            if (sx != -1 && sy != -1) {
                ex = a;
                ey = b;
                labels[a][b].setStyle("-fx-background-color: white");
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
    public void gameStart(ActionEvent event) {
        spielstatus = true;
        System.out.println("Spielstatus: "+spielstatus);
        GameTopLabel1.setText("Du schießt jetzt hier:");
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.starteSpiel(1);
        if(!ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.shipSizesToAdd(Bot.getShipSizes(GOETTLICHESSPIELDERVERNICHTUNGMITbot.schiffe))) {
            System.err.println("Bot Schiffe fehler");
            return;
        }
        placebutton.setVisible(false);
        int spieler = GOETTLICHESSPIELDERVERNICHTUNGMITbot.getAbschussSpieler();
        //System.out.println("Spieler: "+spieler);
        GameTopLabel.setText("Spieler: "+spieler);
        if (spieler == 0) {
            GameTopLabel.setText("Bot schießt");
            Botschiesst();
            GameTopLabel.setText("Du schießt");
        }




    }

    public void BacktoMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);

        window.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void printfeld(ActionEvent event) {
        logic.logicOUTput.printFeld(GOETTLICHESSPIELDERVERNICHTUNGMITbot.getFeld(),true);
    }
}
