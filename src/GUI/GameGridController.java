package GUI;

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
import logic.RDM_Bot;
import logic.Spiel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameGridController implements Initializable {
    //@FXML private AnchorPane anchoroanegamegrid;
    @FXML private StackPane StackPane;
    @FXML private Button placebutton;
    private boolean spielstatus=false;
    private GridPane GameGrid;
    private Label[ ][ ] labels;
    private Label[][]labels2;
    private Integer x,y;
    private int labelclicked [][][];
    private int dex,dey,drx,dry;
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
        GameGrid = new GridPane();
        labels = new Label[x][y];
        labelclicked = new int[x][y][2];
        for(int a=x-1;a >= 0;a--) {
            //System.out.println("for 2");
            for(int b=y-1;b >= 0;b--) {

                //Game Labels
                labels [a] [b] = new Label();
                labels[a][b].setMinSize(50,50);
                labels[a][b].setStyle("-fx-background-color: blue");
                //final int ca=a,cb=b;
                int ca=a,cb=b;
               labels[a][b].setOnMouseClicked(e -> labelclick(ca,cb) );
                //labels[a][b].setOnMouseDragEntered(e -> MouseDragEnter(ca,cb));
                //labels[a][b].setOnMouseDragReleased(e ->MouseDragReleased(ca,cb));

                //labels[a][b].setOnMousePressed(e -> MouseDragEnter(ca,cb));
                //labels[a][b].setOnMouseReleased(e ->MouseDragReleased(ca,cb));

                //Daragos
                /*
                labels[a][b].setOnDragDetected(e -> DragDetected(ca,cb));
                labels[a][b].setOnDragEntered(e -> DragEntered(ca,cb));
                labels[a][b].setOnDragExited(e -> DragExit(ca,cb));
                labels[a][b].setOnDra
                 */

/*
                //#Geklaute Drag and Drop kake
                labels[a][b].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Event MAUS GEPRESSED x= "+ca+" b= "+cb);
                        event.setDragDetect(true);
                    }
                });
                labels[a][b].setOnMouseReleased(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("");
                        System.out.println("Maus RELASED x= "+ ca +" y= "+ cb);
                        System.out.println("");
                    }
                });
                labels[a][b].setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("DARG DETECTET IN THE x= "+ca+" Y= "+cb);
                        event.setDragDetect(false);
                    }
                });
                labels[a][b].setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Wundervoller Drag dedecktet x= "+ca+" y= "+cb);
                    }
                });
                /////////////////////////////////
                labels[a][b].setOnMouseDragEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Darg entero");
                    }
                });

                labels[a][b].setOnMouseDragOver(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Drago OVERO");
                    }
                });
                labels[a][b].setOnMouseDragReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("MAUS DRAG RELEASED IN x= "+ca+" y= "+cb);
                    }
                });
                labels[a][b].setOnMouseDragExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("MAUS DRAG EXIT");
                    }
                });


                //////////

 */
                GridPane.setConstraints(labels[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);
            }
        }
        GameGrid.setAlignment(Pos.CENTER);
        GameGrid.setHgap(1);
        GameGrid.setVgap(1);
        StackPane.getChildren().add(GameGrid);
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
        boolean shippaddo,richtung;
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
        labels[sx][sy].setStyle("-fx-background-color: blue");
        labels[ex][ey].setStyle("-fx-background-color: blue");
        sx=-1;
        sy=-1;
        ex=-1;
        ey=-1;
    }


    //ActionHandler für Label gedrückt
    private void labelclick(int a, int b) {
        System.out.println("x= "+a+" y= "+b);
        // sx,sy,ex,ey
        if (sx == -1 && sy == -1 ) {
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
    //versetzt Spiel in Feuermodus
    public void gameStart(ActionEvent event) {
        spielstatus = true;
        System.out.println(spielstatus);
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


}
