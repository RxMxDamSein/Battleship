import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import logic.Spiel;
import logic.logicOUTput;


/**
 * Klasse die das 2Spieler spielen Schiffeversenken Spiel erlaubt mit spicken!
 */
public class Grid {
    private Stage window;
    private Scene sceneOld;
    private Spiel dasSpiel;
    private GridPane gridPlayer1;
    private GridPane gridPlayer2;
    private int[][][] feld;
    private Label[][][] labels;
    private Label[] lToShoot;

    /**
     * Konstruktor initialisiert alles
     * @param window Das Fenster indem das Spiel angezeigt wird
     * @param x Spielbrettbreite
     * @param y Spielbretthöhe
     * @param sceneOld In diese Scene kann mittels des Buttons "Zurück" gesprungen werden
     */
    public Grid(Stage window, int x, int y, Scene sceneOld)  {
        this.window=window;
        this.sceneOld=sceneOld;
        this.gridPlayer1=new GridPane();
        this.gridPlayer2=new GridPane();
        this.lToShoot=new Label[2];
        lToShoot[0]=new Label("1");
        lToShoot[1]=new Label("2");
        GridPane.setConstraints(lToShoot[0],0,y,x+1,y,HPos.CENTER,VPos.CENTER);
        this.gridPlayer1.getChildren().add(lToShoot[0]);
        GridPane.setConstraints(lToShoot[1],0,y,x+1,y,HPos.CENTER,VPos.CENTER);
        this.gridPlayer2.getChildren().add(lToShoot[1]);
        window.setTitle("GRID!");
        this.dasSpiel=new Spiel(x,y);
        if(!dasSpiel.init())
            sceneZutuck();
        this.feld=dasSpiel.getFeld();
        this.labels=new Label[feld.length][feld[0].length][feld[0][0].length];
        initPlayerGrids();

        Button buttonZuruck=new Button("Zurück");
        buttonZuruck.setOnAction(e->sceneZutuck());
        Button buttonStart=new Button("Start Shooting!");
        buttonStart.setOnAction(e->buttonSpielStart());


        HBox hBox=new HBox(10);
        hBox.getChildren().addAll(this.gridPlayer1,this.gridPlayer2);
        HBox hBox2=new HBox(10);
        hBox2.setPadding(new Insets(5,5,5,5));
        hBox2.getChildren().addAll(buttonStart);
        VBox vBox=new VBox(5);
        vBox.getChildren().addAll(hBox,hBox2,buttonZuruck);
        vBox.setPadding(new Insets(5,5,5,5));
        Scene sceneGrid=new Scene(vBox);
        window.setScene(sceneGrid);
    }

    /**
     * Schreibt in die lToShoot Labels jeweils ob man verloren oder gewonnen hat.
     * Diese Funkton gilt es unmittelbar beom Sieg aufzurufen
     */
    private void gameOver(){
        lToShoot[dasSpiel.getAbschussSpieler()].setText("Verlierer!");
        lToShoot[(dasSpiel.getAbschussSpieler()==0)?1:0].setText("Sieger!");
    }

    /**
     * Zeigt bei den lToShoot Labels an welcher Spieler abzuschiesen ist
     */
    private void setLabelAbschuss(){
        int s=dasSpiel.getAbschussSpieler();
        lToShoot[s].setText("Schieß hier!");
        lToShoot[(s==1)?0:1].setText("Spieler "+(((s==1)?0:1)+1));
    }

    /**
     * Geht vom Schiff hinzufügen Modus in Spiel(Shoot) Modus über
     */
    private void buttonSpielStart(){
        if(dasSpiel.starteSpiel()){
            setLabelAbschuss();
            if(dasSpiel.isOver())
                gameOver();
        }
    }

    /**
     * geht in die im Konstruktor gegebene Scene zurück.
     * -> das Spiel wird verworfen
     */
    private void sceneZutuck(){
        window.setScene(sceneOld);
        window.setTitle("ENTER GRID SIZE");
    }

    /**
     * Initialisiert das Spielfeld
     */
    private void initPlayerGrids(){
        GridPane[] gridPanes=new GridPane[2];
        gridPanes[0]=gridPlayer1;
        gridPanes[1]=gridPlayer2;
        for(int s=0;s<2;s++){
            for (int i=0;i<feld[s].length;i++){
                for(int j=0;j<feld[s][i].length;j++){
                    labels[s][i][j]=new Label(" "+String.valueOf(feld[s][i][j])+" ");
                    final int sz=s,iz=i,jz=j;
                    labels[s][i][j].setOnMouseClicked(e->labelClick(sz,iz,jz));
                    GridPane.setConstraints(labels[s][i][j],i,j,1,1,HPos.CENTER,VPos.CENTER);
                    gridPanes[s].getChildren().add(labels[s][i][j]);
                }
            }
        }
    }

    /**
     * schreibt die Werte aus Spiel.feld in das Spielfeld
     */
    private void updatePlayerGrids(){
        GridPane[] gridPanes=new GridPane[2];
        gridPanes[0]=gridPlayer1;
        gridPanes[1]=gridPlayer2;
        for(int s=0;s<2;s++){
            for (int i=0;i<feld[s].length;i++){
                for(int j=0;j<feld[s][i].length;j++){
                    labels[s][i][j].setText(" "+String.valueOf(feld[s][i][j])+" ");
                }
            }
        }
    }

    private int s_old=-1,x_old=-1,y_old=-1;
    private Paint paint_old=null;
    boolean selected=false;
    boolean selectDone=false;

    /**
     * Funktion wenn man auf ein Spielfeld klickt!
     * Ermöglicht das hinzufügen von Schiffen.
     * Und das schießen.
     * @param s Spieler
     * @param x X Koordinate
     * @param y Y Koordinate
     */
    private void labelClick(int s,int x,int y){
        System.out.println("Clicked Label "+s+": "+x+" | "+y );
        if(dasSpiel.isStarted() && !dasSpiel.isOver()){
            boolean shot=dasSpiel.shoot(x,y,s);
            if(shot){
                if(dasSpiel.istVersenkt())
                    System.out.println("Treffer Versenkt!");
                setLabelAbschuss();
                updatePlayerGrids();
                if(dasSpiel.isOver())
                    gameOver();
            }

        } else if(!dasSpiel.isStarted()){              //Schiff Hinzufügen!
            if(selected){                     //altes Feld zurücksetzen
                labels[s_old][x_old][y_old].setTextFill(paint_old);
                if(s_old==s && x_old==x ){       //vertical ship!
                    dasSpiel.addShip(x_old,(y_old<y)?y_old:y,false,(y_old-y<1)?y-y_old+1:y_old-y+1,s);
                        selectDone=true;
                }else if(s_old==s && y_old==y){ //horizontales Schiff
                    dasSpiel.addShip((x_old<x)?x_old:x,y_old,true,(x_old-x<1)?x-x_old+1:x_old-x+1,s);
                        selectDone=true;
                }
            }
            if(!selectDone){
                s_old=s;x_old=x;y_old=y;paint_old=labels[s][x][y].getTextFill();
                labels[s][x][y].setTextFill(Color.web("green"));
                selected=true;
            }else {
                s_old=x_old=y_old=-1;
                paint_old=null;
                updatePlayerGrids();
                //logicOUTput.printFeld(feld,true);
                selected=selectDone=false;
            }
        }
    }

}