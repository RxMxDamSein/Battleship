import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.Bot;
import logic.Bot_lvl_2;
import logic.Spiel;
import logic.save.SAFE_SOME;

public class BvB_GUI {
    private Stage window;
    private Scene sceneOld;
    private Scene sceneGrid;
    private Bot b1;
    private Bot b2;
    private GridPane gridPlayer1;
    private GridPane gridPlayer2;
    private int[][][] feld;
    private Label[][][] labels;
    private Label[] lToShoot;
    private Label Lversenkt;
    private boolean started=false;
    private Timeline oneSecondsWonder;
    private int speed=1000;


    public BvB_GUI(Stage window, int x, int y, Scene sceneOld){
        init(window,x,y,sceneOld);

        int[] add=Bot.calcships(x,y);
        b1=new Bot_lvl_2(x,y);
        b1.shipSizesToAdd(add);
        b2=new Bot_lvl_2(x,y);
        b2.shipSizesToAdd(add);
        feld=b1.dasSpiel.getFeld();
        initPlayerGrids();
        updatePlayerGrids();
        window.setScene(sceneGrid);

    }

    /**
     * ToDo should load game later
     * @param window
     * @param sceneOld
     * @param id
     */
    public BvB_GUI(Stage window,Scene sceneOld,String id){
        SAFE_SOME ss=SAFE_SOME.load(id);
        if(ss==null || ss.game!=3 || ss.bots==null || ss.bots.length<2){//error
            window.setTitle("load error");
            return;
        }
        b1=ss.bots[0];
        b2=ss.bots[1];
        init(window,b1.dasSpiel.getSizeX(),b1.dasSpiel.getSizeY(),sceneOld);
        feld=b1.dasSpiel.getFeld();
        initPlayerGrids();
        updatePlayerGrids();
        window.setScene(sceneGrid);
    }

    private void init(Stage window, int x, int y, Scene sceneOld){
        this.window=window;
        this.sceneOld=sceneOld;
        this.gridPlayer1=new GridPane();
        this.gridPlayer2=new GridPane();
        this.lToShoot=new Label[2];
        lToShoot[0]=new Label("1");
        lToShoot[1]=new Label("2");
        GridPane.setConstraints(lToShoot[0],0,y,x+1,y, HPos.CENTER, VPos.CENTER);
        this.gridPlayer1.getChildren().add(lToShoot[0]);
        GridPane.setConstraints(lToShoot[1],0,y,x+1,y,HPos.CENTER,VPos.CENTER);
        this.gridPlayer2.getChildren().add(lToShoot[1]);
        window.setTitle("BvB!");

        this.labels=new Label[2][x][y];


        Button buttonZuruck=new Button("Zurück");
        buttonZuruck.setOnAction(e->sceneZutuck());
        Button buttonStart=new Button("Start Shooting!");
        buttonStart.setOnAction(e->buttonSpielStart());
        Button buttonSave=new Button("SAVE");
        buttonSave.setOnAction(e->buttonSave());

        Lversenkt=new Label("o");


        HBox hBox=new HBox(10);
        hBox.getChildren().addAll(this.gridPlayer1,Lversenkt,this.gridPlayer2);
        HBox hBox2=new HBox(10);
        hBox2.setPadding(new Insets(5,5,5,5));
        hBox2.getChildren().addAll(buttonStart);
        HBox hBox3=new HBox(5);
        hBox3.getChildren().addAll(buttonZuruck,buttonSave);
        VBox vBox=new VBox(5);
        vBox.getChildren().addAll(hBox,hBox2,hBox3);
        vBox.setPadding(new Insets(5,5,5,5));
        sceneGrid=new Scene(vBox);
    }

    /**
     * Geht vom Schiff hinzufügen Modus in Spiel(Shoot) Modus über
     */
    private void buttonSpielStart(){
        if(started)
            return;
        started=true;
        //System.out.println("spielstart");

        setLabelAbschuss();
        if(b1.dasSpiel.isOver())
            gameOver();
        //shoot();
        oneSecondsWonder = new Timeline(
                new KeyFrame(Duration.millis(speed),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                if(!b1.dasSpiel.isOver() && !b2.isFinOver())
                                    shoot();
                                else
                                    oneSecondsWonder.stop();
                            }
                        }));
        oneSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        oneSecondsWonder.play();

    }

    /**
     * lässt den Bott schießen!
     */
    private void shoot(){
        System.out.println("shoot");
        //int s=dasSpiel.getAbschussSpieler();
        //while(s==0 && !dasSpiel.isOver()){

        if(b1.dasSpiel.getAbschussSpieler()==0){
            int[] xy=b2.getSchuss();
            if(!b1.dasSpiel.shoot(xy[0],xy[1],0,0,false))
                return;

            b2.setSchussFeld(xy[0],xy[1],feld[0][xy[0]][xy[1]],b1.dasSpiel.istVersenkt());
        }else{
            int[] xy=b1.getSchuss();
            int ret=b2.abschiesen(xy[0],xy[1]);
            Lversenkt.setText("o");
            if(ret<0)
                return;
            else if(ret==4){
                b1.dasSpiel.shoot(xy[0],xy[1],1,1,true);
                System.out.println("Treffer Versenkt!");
                Lversenkt.setText("x");
                //if(derBot.isFinOver())
                //    dasSpiel.setGameOver();
            }
            else
                b1.dasSpiel.shoot(xy[0],xy[1],1,ret,false);
            if(b1.dasSpiel.istVersenkt()){
                b1.slayship=false;
                Bot.waterAround(1,xy[0],xy[1],b1.dasSpiel.getFeld(),b1.dasSpiel.getSizeX(),b1.dasSpiel.getSizeY());
            }else if (b1.dasSpiel.getFeld()[1][xy[0]][xy[1]]==2){
                //System.out.println("B1 SLAY!!!!");
                b1.slayship=true;
                b1.slayX=xy[0];
                b1.slayY=xy[1];
            }
        }

        setLabelAbschuss();
        updatePlayerGrids();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(b1.dasSpiel.isOver() || b2.isFinOver()){
            if(b1.dasSpiel.getAbschussSpieler()==1)
                b1.dasSpiel.setGameOver();
            gameOver();
        }

    }

    private void gameOver(){
        lToShoot[b1.dasSpiel.getAbschussSpieler()].setText("Verlierer!");
        lToShoot[(b1.dasSpiel.getAbschussSpieler()==0)?1:0].setText("Sieger!");
    }

    private void setLabelAbschuss(){
        int s=b1.dasSpiel.getAbschussSpieler();
        /*if(s==0){
            botSchuss();
            return;
        }*/
        lToShoot[s].setText("Schieß hier!");
        lToShoot[(s==1)?0:1].setText("Spieler "+(((s==1)?0:1)+1));
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
                    switch (feld[s][i][j]){
                        default:
                            labels[s][i][j].setTextFill(Color.web("grey"));
                            break;
                        case 1:
                            labels[s][i][j].setTextFill(Color.web("black"));
                            break;
                        case 2:
                            labels[s][i][j].setTextFill(Color.web("red"));
                            break;
                        case 3:
                            labels[s][i][j].setTextFill(Color.web("blue"));
                            break;

                    }
                }
            }
        }
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
                    labels[s][i][j]=new Label(" 0 ");
                    final int sz=s,iz=i,jz=j;
                    GridPane.setConstraints(labels[s][i][j],i,j,1,1,HPos.CENTER,VPos.CENTER);
                    gridPanes[s].getChildren().add(labels[s][i][j]);
                }
            }
        }
    }

    private void sceneZutuck(){
        if(oneSecondsWonder!=null)
            oneSecondsWonder.stop();
        window.setScene(sceneOld);
        window.setTitle("ENTER GRID SIZE");
    }

    private void buttonSave(){
        if(b1.dasSpiel.isStarted()){
            new SAFE_SOME(new Bot[]{b1,b2},null,3,"bvb");
        }else{
            System.err.println("Es gibt nichts zu speichern!");
        }
    }
}
