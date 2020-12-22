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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BOTgegenBOTGridController  implements Initializable  {
    private static final long serialVersionUID=1337L;
    //@FXML private AnchorPane anchoroanegamegrid;
    @FXML private StackPane StackPane;
    @FXML private StackPane StackPane2;
    //@FXML private Button placebutton;
    @FXML private  Label GameTopLabel;
    @FXML private Label GameTopLabel1;
    private boolean spielstatus=false;
    private GridPane GameGrid;
    private GridPane GameGrid2;
    private Label[ ][ ] labels;
    private Label[ ][ ] labels2;
    private Integer x,bot1,bot2,count=0;
    private int sx=-1,sy=-1,ex=-1,ey=-1;
    private Timeline oneSecondsWonder;
    private int speed=10;
    private int[][][] feld;

    //private Spiel GOETTLICHESSPIELDERVERNICHTUNGMITbot;
    private Bot ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST;
    private Bot WUNDERVOLLERGEGNERBOT;

    public BOTgegenBOTGridController() {}
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setInteger(Integer a, Integer b1, Integer b2) {
        x=a;
        bot1 = b2;
        bot2 = b1;
        Spielinit();
        Gridinit();
        GridUpdater();

    }
    public void GridUpdater() {
        int feld1[][][] = ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld();
        //for (int s=0;s<2;s++){
        for (int a = x - 1; a >= 0; a--) {
            for (int b = x - 1; b >= 0; b--) {
                switch (feld1[0][a][b]) {
                    default:
                        break;
                    case 1:
                        labels2[a][b].setStyle("-fx-background-color: grey");
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
                        labels[a][b].setStyle("-fx-background-color: grey");
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

    public void Spielinit() {
        //GOETTLICHESSPIELDERVERNICHTUNGMITbot = new Spiel(x,x,true);
        switch (bot1) {
            case 1:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new RDM_Bot(x,x);
                break;
            case 2:
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_lvl_2(x,x);
                break;
            case 3:
                //ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_nightmare(x,x,WUNDERVOLLERGEGNERBOT.dasSpiel);
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST = new Bot_nightmare(x,x,null);
                break;
            default:
                System.err.println("Bot Auswahl Fehler!!");

        }
        switch (bot2) {
            case 1:
                WUNDERVOLLERGEGNERBOT = new RDM_Bot(x,x);
                break;
            case 2:
                WUNDERVOLLERGEGNERBOT = new Bot_lvl_2(x,x);
                break;
            case 3:
                WUNDERVOLLERGEGNERBOT = new Bot_nightmare(x,x,ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel);
                break;
            default:
                System.err.println("Bot Auswahl Fehler!!");

        }
        if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST instanceof Bot_nightmare) {
            ((Bot_nightmare) ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST).cheat = WUNDERVOLLERGEGNERBOT.dasSpiel;
        }
        int[] add = Bot.calcships(x,x);
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.shipSizesToAdd(add);
        ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getDasSpiel().setVerbose(false);
        WUNDERVOLLERGEGNERBOT.shipSizesToAdd(add);
        WUNDERVOLLERGEGNERBOT.getDasSpiel().setVerbose(false);
        feld=ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld();
        //GOETTLICHESSPIELDERVERNICHTUNGMITbot.init();
    }
    public double minsizeberechner() {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        System.out.println("Höhe: "+screen.getHeight()+" Weite: "+screen.getWidth());
        //return -((double)x-10)+50;
        //return -(0.75* (double) x-7.5)+50;
        //double zahl = java.lang.Math.exp(-(0.05*x-4.3))+5;
        double zahl = (screen.getHeight()>screen.getWidth())?screen.getHeight():screen.getWidth();
        zahl*= 0.7;
        zahl = (zahl/2)/x;
        System.out.println("Wundervolle Zahl: "+zahl);
        return zahl;
    }

    public void Gridinit() {
        //initialisieren Label und Grid (1)
        GameGrid = new GridPane();
        labels = new Label[x][x];
        //initialisieren Label und Grid (2)
        GameGrid2 = new GridPane();
        labels2 = new Label[x][x];
        //lustige scale sachen
        for(int a=x-1;a >= 0;a--) {
            //System.out.println("for 2");
            for(int b=x-1;b >= 0;b--) {

                //Game Labels (1)
                labels [a] [b] = new Label();
                labels[a][b].setMinSize(minsizeberechner(),minsizeberechner());
                //labels[a][b].setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                labels[a][b].setStyle("-fx-background-color: #03fcf4");

                //lustige bilder zu den Labels
                /*
                Image img = new Image("GUI/Textures/42.jpg");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner());
                view.setPreserveRatio(true);
                labels[a][b].setGraphic(view);

                 */

                GridPane.setConstraints(labels[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
                GameGrid.getChildren().add(labels[a][b]);

                //Game Labels (2)
                labels2 [a] [b] = new Label();
                //labels2[a][b].setMinSize(50,50);
                labels2[a][b].setMinSize(minsizeberechner(),minsizeberechner());
                labels2[a][b].setStyle("-fx-background-color: #03fcf4");

                //Wundervolles Image
                /*
                Image img2 = new Image("GUI/Textures/42.jpg");
                ImageView view2 = new ImageView(img2);
                view2.setFitHeight(minsizeberechner());
                view2.setPreserveRatio(true);
                labels2[a][b].setGraphic(view2);

                 */


                GridPane.setConstraints(labels2[a][b],a,b,1,1,HPos.CENTER,VPos.CENTER);
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

    public void gameStart(ActionEvent event) {
        oneSecondsWonder=new Timeline(new KeyFrame(Duration.millis(speed), e->{
            if(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.isOver() /*|| b2.isFinOver()*/){
                oneSecondsWonder.stop();
                System.out.println("SPIEL ENDE");
            }else {
                shoot();
            }
        }));
        oneSecondsWonder.setCycleCount(Animation.INDEFINITE);
        oneSecondsWonder.play();
    }

    private void shoot() {
        if(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getAbschussSpieler()==0){
            int[] xy=WUNDERVOLLERGEGNERBOT.getSchuss();
            if(!ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.shoot(xy[0],xy[1],0,0,false))
                return;
            WUNDERVOLLERGEGNERBOT.setSchussFeld(xy[0],xy[1],feld[0][xy[0]][xy[1]],ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.istVersenkt());
        }else{
            int[] xy=ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.getSchuss();
            int ret=WUNDERVOLLERGEGNERBOT.abschiesen(xy[0],xy[1]);
            //Lversenkt.setText("o");
            if(ret<0)
                return;
            else if(ret==4){
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.shoot(xy[0],xy[1],1,1,true);
                System.out.println("Treffer Versenkt!");
                //Lversenkt.setText("x");
                //if(derBot.isFinOver())
                //    dasSpiel.setGameOver();
            }
            else
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.shoot(xy[0],xy[1],1,ret,false);
            if(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.istVersenkt()){
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.slayship=false;
                Bot.waterAround(1,xy[0],xy[1],ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld(),ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getSizeX(),ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getSizeY());
            }else if (ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld()[1][xy[0]][xy[1]]==2){
                //System.out.println("B1 SLAY!!!!");
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.slayship=true;
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.slayX=xy[0];
                ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.slayY=xy[1];
            }
        }
        GridUpdater();
    }

    public void Speichern(ActionEvent event) {
        //oneSecondsWonder.stop();
        //SaveData data = new SaveData();
        //ResourceManager.save(this, "1.save");
        // SAVE POP UP Fenster
        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.setPadding(new Insets(10,10,10,10));
        comp.setSpacing(5);
        comp.setStyle("-fx-background-color: DARKCYAN;");
        comp.setAlignment(Pos.CENTER);
        TextField DateiName = new TextField();
        DateiName.setText("Dateiname:");
        Button Save = new Button();
        Save.setPrefSize(100,30);
        Save.setText("Save");
        Save.setOnAction(event1 -> {
            String name = String.valueOf(DateiName.getText());
            System.out.println("Name: "+name);
            new SaveGame(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST,WUNDERVOLLERGEGNERBOT,name);
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

    public void BacktoMenu(ActionEvent event) throws IOException {
        //oneSecondsWonder.stop();
        Parent root = FXMLLoader.load(getClass().getResource("BOTgegenBOTMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();
    }

    public void printfeld(ActionEvent event) {
        logic.logicOUTput.printFeld(ROMANSFABELHAFTERbotDERNOCHVERBUGGTIST.dasSpiel.getFeld(),true);
    }
}
