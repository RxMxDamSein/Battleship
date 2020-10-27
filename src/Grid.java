import javafx.geometry.HPos;
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




public class Grid {
    private Stage window;
    private Scene sceneOld;
    private Spiel dasSpiel;
    private GridPane gridPlayer1;
    private GridPane gridPlayer2;
    private int[][][] feld;
    private Label[][][] labels;

    public Grid(Stage window, int x, int y, Scene sceneOld)  {
        this.window=window;
        this.sceneOld=sceneOld;
        this.gridPlayer1=new GridPane();
        this.gridPlayer2=new GridPane();
        window.setTitle("GRID!");
        this.dasSpiel=new Spiel(x,y);
        if(!dasSpiel.init())
            sceneZutuck();
        this.feld=dasSpiel.getFeld();
        this.labels=new Label[feld.length][feld[0].length][feld[0][0].length];
        initPlayerGrids();

        Button buttonZuruck=new Button("Zurück");
        buttonZuruck.setOnAction(e->sceneZutuck());

        HBox hBox=new HBox(10);
        hBox.getChildren().addAll(this.gridPlayer1,this.gridPlayer2);
        VBox vBox=new VBox(5);
        vBox.getChildren().addAll(hBox,buttonZuruck);

        Scene sceneGrid=new Scene(vBox);
        window.setScene(sceneGrid);
    }
    private void sceneZutuck(){
        window.setScene(sceneOld);
        window.setTitle("ENTER GRID SIZE");
    }

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
    private void labelClick(int s,int x,int y){

        System.out.println("Clicked Label "+s+": "+x+" | "+y );
        if(!dasSpiel.isStarted()){              //Schiff Hinzufügen!
            if(selected){                     //altes Feld zurücksetzen
                labels[s_old][x_old][y_old].setTextFill(paint_old);
                if(s_old==s && x_old==x ){       //vertical ship!
                    if(dasSpiel.addShip(x_old,(y_old<y)?y_old:y,false,(y_old-y<1)?y-y_old+1:y_old-y+1,s)){
                        selectDone=true;
                    }else {
                        selected=selectDone=false;
                    }

                }else if(s_old==s && y_old==y){ //horizontales Schiff
                    if(dasSpiel.addShip((x_old<x)?x_old:x,y_old,true,(x_old-x<1)?x-x_old+1:x_old-x+1,s)){
                        selectDone=true;
                    }else {
                        selected=selectDone=false;
                    }
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
                logicOUTput.printFeld(feld,true);
                selected=selectDone=false;
            }
        }
    }

}