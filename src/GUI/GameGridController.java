package GUI;

import javafx.event.ActionEvent;
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

        GameGrid = new GridPane();
        labels = new Label[x][y];
        labelclicked = new int[x][y][2];
        for(int a=x-1;a >= 0;a--) {
            System.out.println("for 2");
            for(int b=y-1;b >= 0;b--) {
                labels [a] [b] = new Label();
                labels[a][b].setMinSize(50,50);
                labels[a][b].setStyle("-fx-background-color: blue");
                final int ca=a,cb=b;
               labels[a][b].setOnMouseClicked(e -> labelclick(ca,cb) );
                //labels[a][b].setOnMouseDragEntered(e -> MouseDragEnter(ca,cb));
                //labels[a][b].setOnMouseDragReleased(e ->MouseDragReleased(ca,cb));
                labels[a][b].setOnMousePressed(e -> MouseDragEnter(ca,cb));
                labels[a][b].setOnMouseReleased(e ->MouseDragReleased(ca,cb));

                //labels[a][b].setOnMouseClicked();
                GridPane.setConstraints(labels[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);
            }
        }
        GameGrid.setAlignment(Pos.CENTER);
        GameGrid.setHgap(2);
        GameGrid.setVgap(2);
        StackPane.getChildren().add(GameGrid);
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
