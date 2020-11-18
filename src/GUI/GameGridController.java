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
    private boolean spielstatus=false;
    private GridPane GameGrid;
    private Label[ ][ ] labels;
    private Label[][]labels2;
    private Integer x,y;
    private int labelclicked [][][];
    private int dex,dey,drx,dry;

    public GameGridController() {}
    public void setInteger(Integer a,Integer b) {
        x=a;
        y=b;
        Gridinit();
        Spielinit();
    }

    private void Spielinit() {
        Spiel GOETTLICHESSPIELDERVERNICHTUNGMITbot = new Spiel(x,y,true);
        RDM_Bot ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new RDM_Bot(x,y);
        GOETTLICHESSPIELDERVERNICHTUNGMITbot.init();
    }

    //Grid und Labels initialisieren
    public void Gridinit() {

        labels2 = new Label[x][y];

        GameGrid = new GridPane();
        labels = new Label[x][y];
        labelclicked = new int[x][y][2];
        for(int a=x-1;a >= 0;a--) {
            //System.out.println("for 2");
            for(int b=y-1;b >= 0;b--) {
/*
                //Targeto Labels
                labels2 [a][b]= new Label();
                labels2[a][b].setMinSize(50,50);
                labels2[a][b].setText("KAKAKAKKA");

 */
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



                GridPane.setConstraints(labels[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);
               /*
                GridPane.setConstraints(labels2[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid.getChildren().add(labels2[a][b]);

                */
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



    //ActionHandler für Label gedrückt
    private void labelclick(int a, int b) {
        System.out.println("x= "+a+" y= "+b);


        /*
        //Schiff setzen
        if (!spielstatus){
            labels[a][b].setStyle("-fx-background-color: grey");
        }
        //feuern
        if (spielstatus) {
            labels[a][b].setStyle("-fx-background-color: red");
        }

        */
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
