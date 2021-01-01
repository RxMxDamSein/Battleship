import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import logic.Bot;
import logic.Spiel;
import logic.logicOUTput;
import logic.netCode.Server_Thread;
import logic.save.SAFE_SOME;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


/**
 * Klasse die das 2Spieler spielen Schiffeversenken Spiel erlaubt mit spicken!
 */
public class Grid_NET{
    private Stage window;
    private Scene sceneOld;
    private Spiel dasSpiel;
    private GridPane gridPlayer1;
    private GridPane gridPlayer2;
    private int[][][] feld;
    private Label[][][] labels;
    private Label[] lToShoot;
    private Server_Thread sT;
    private Thread T;
    private String nachricht="";
    private int lx,ly;
    private boolean shooting=true;
    private Timeline nachrichtChecker;
    private int speed=250;
    private Button buttonStart;
    private Button buttonSave;


    public Grid_NET(Stage window, Scene sceneOld,int PORT, String id){
        SAFE_SOME safe_some=SAFE_SOME.load(id);
        if(safe_some.game!=4){
            System.err.println("you loaded not a ServerHost game!");
            return;
        }
        Spiel s=safe_some.spiele[0];
        dasSpiel=s;
        sT=new Server_Thread(s.getSizeX(), s.getSizeY(),PORT);
        Runnable runnable= () -> {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(PORT);
                System.out.println("Waiting for client ...");
                sT.s = ss.accept();
                System.out.println("Connection established");

            } catch (IOException e) {
                System.err.println("Can not create Socket!");
                e.printStackTrace();
                sceneZutuck();
            }
            try {
                sT.in = new BufferedReader(new InputStreamReader(sT.s.getInputStream()));
                sT.out = new OutputStreamWriter(sT.s.getOutputStream());
            } catch (IOException e) {
                System.err.println("can not create IO Stream!");
            }
            lx = ly = -1;
            sT.sendSocket("load "+id);
            if(!sT.receiveSocket().contains("done")){
                System.err.println("Klient spricht wirres Zeug");
            }
            sT.sendSocket("ready");
            nachricht=sT.receiveSocket();
            initNChecker();
        };
        Thread t=new Thread(runnable);
        t.start();
        init(window,s.getSizeX(), s.getSizeY(), sceneOld,s);
        dasSpiel=s;
        updatePlayerGrids();
        if(dasSpiel.isStarted() && !dasSpiel.isOver()) {
            setLabelAbschuss();
        }else if(dasSpiel.isOver()){
            gameOver();
        }
    }
    /**
     * Konstruktor initialisiert alles
     * @param window Das Fenster indem das Spiel angezeigt wird
     * @param x Spielbrettbreite
     * @param y Spielbretthöhe
     * @param sceneOld In diese Scene kann mittels des Buttons "Zurück" gesprungen werden
     */
    public Grid_NET(Stage window, int x, int y, Scene sceneOld,int PORT) throws IOException {
        sT=new Server_Thread(x,y,PORT);
        T=new Thread(sT);
        T.start();
        init(window,x,y,sceneOld,sT.dasSpiel);
    }

    private void init(Stage window, int x, int y, Scene sceneOld,Spiel s){
        lx=ly=-1;
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

        this.dasSpiel=s;

        this.feld=dasSpiel.getFeld();
        this.labels=new Label[feld.length][feld[0].length][feld[0][0].length];
        initPlayerGrids();

        Button buttonZuruck=new Button("Zurück");
        buttonZuruck.setOnAction(e->sceneZutuck());
        buttonStart=new Button("Send Ships");
        buttonStart.setOnAction(e->buttonSpielStart());

        buttonSave=new Button("SAVE");
        buttonSave.setOnAction(e->buttonSave());

        HBox hBox=new HBox(10);
        hBox.getChildren().addAll(this.gridPlayer1,this.gridPlayer2);
        HBox hBox2=new HBox(10);
        hBox2.setPadding(new Insets(5,5,5,5));
        hBox2.getChildren().addAll(buttonStart);
        HBox hBox3=new HBox(5);
        hBox3.getChildren().addAll(buttonZuruck,buttonSave);
        VBox vBox=new VBox(5);
        vBox.getChildren().addAll(hBox,hBox2,hBox3);
        vBox.setPadding(new Insets(5,5,5,5));
        Scene sceneGrid=new Scene(vBox);
        window.setScene(sceneGrid);
    }

    private void buttonSave(){
        if(dasSpiel.getAbschussSpieler()==1){
            String saveID=""+this.hashCode();
            sentReceiveTRun("save "+saveID);
            SAFE_SOME safe_some=new SAFE_SOME(null,new Spiel[]{dasSpiel},4,saveID);
            buttonSave.setText("SAVED!");
        }else {
            buttonSave.setText("SAVE on your turn!");
        }

    }

    /**
     * Schreibt in die lToShoot Labels jeweils ob man verloren oder gewonnen hat.
     * Diese Funkton gilt es unmittelbar beom Sieg aufzurufen
     */
    private void gameOver(){
        lToShoot[dasSpiel.getAbschussSpieler()].setText("Verlierer!");
        lToShoot[(dasSpiel.getAbschussSpieler()==0)?1:0].setText("Sieger!");
        Runnable runnable=()->{
            try {
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                sT.s.close();
            } catch (IOException e) {
                System.err.println("can not close Socket");
                e.printStackTrace();
            }

        };
        Thread z=new Thread(runnable);
        z.start();

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
        if(dasSpiel.isStarted())
            return;

        int[] ships=logic.Bot.getShipSizes(dasSpiel.schiffe);
        String antwort="ships";
        for(int i=ships.length-1;i>=0;i--){

            antwort+=" "+ships[i];
        }

        if(dasSpiel.starteSpiel(1)){

            setLabelAbschuss();
            if(dasSpiel.isOver())
                gameOver();
            //try {
                //T.join();
            sentReceiveTRun(antwort);
            initNChecker();
            //} catch (InterruptedException e) {
            //    System.err.println("Socket won't join!");
            //    e.printStackTrace();
            //}
        }
    }

    private void initNChecker(){
        if(nachrichtChecker==null){
            nachrichtChecker=new Timeline(new KeyFrame(Duration.millis(speed), e->{
                if(dasSpiel.isOver() /*|| b2.isFinOver()*/){
                    nachrichtChecker.stop();
                }else {
                    String nachricht=this.nachricht;
                    this.nachricht="";
                    if(nachricht.contains("next")||nachricht.contains("ready") || nachricht.contains("answer 1") || nachricht.contains("answer 2")) {
                        if (nachricht.contains("answer 1") || nachricht.contains("answer 2")) {
                            boolean versenkt = false;
                            if (nachricht.contains("answer 2"))
                                versenkt = true;
                            dasSpiel.shoot(lx, ly, 1, 1, versenkt);
                            setLabelAbschuss();
                            updatePlayerGrids();
                            if(dasSpiel.isOver())
                                gameOver();
                            shooting=false;
                        }else if (nachricht.contains("ready")){
                            shooting=false;
                            buttonStart.setText("start shooting");
                        }
                    }else if(nachricht.contains("answer 0")){
                        dasSpiel.shoot(lx,ly,1,0,false);
                        setLabelAbschuss();
                        updatePlayerGrids();
                        if(dasSpiel.isOver())
                            gameOver();
                        if(srT.isAlive())
                            System.err.println("WTF warum gibts denn srT?!");
                        sentReceiveTRun("next");
                        shooting=false;
                    }else if(nachricht.contains("shot")){
                        int x_ = Integer.parseInt(nachricht.split(" ")[1]);
                        int y_ = Integer.parseInt(nachricht.split(" ")[2]);
                        dasSpiel.shoot(x_,y_,0,0,false);
                        setLabelAbschuss();
                        updatePlayerGrids();
                        if(dasSpiel.isOver())
                            gameOver();
                        String z="";
                        System.out.println("("+x_+"|"+y_+") "+dasSpiel.getFeld()[0][x_][y_]);
                        switch (dasSpiel.getFeld()[0][x_][y_]){
                            case 2:
                                if(dasSpiel.istVersenkt()){
                                    z="answer 2";
                                }else{
                                    z= "answer 1";
                                }
                                break;
                            default:
                                z= "answer 0";
                                break;
                        }
                        if(srT.isAlive())
                            System.err.println("WTF warum gibts denn srT?!");
                        sentReceiveTRun(z);
                    } else if(nachricht.contains("done")){
                        sentReceiveTRun("ready");
                    }
                    else if(nachricht.contains("save")){
                        String saveID=""+nachricht.split(" ")[1];

                        SAFE_SOME safe_some=new SAFE_SOME(null,new Spiel[]{dasSpiel},4,saveID);
                        sT.sendSocket("done");
                    }
                }
            }));
            nachrichtChecker.setCycleCount(Animation.INDEFINITE);
            nachrichtChecker.play();
        }
    }

    /**
     * geht in die im Konstruktor gegebene Scene zurück.
     * -> das Spiel wird verworfen
     */
    private void sceneZutuck(){
        if(nachrichtChecker!=null)
            nachrichtChecker.stop();



        try {
            if(sT.s!=null)
                sT.s.close();
            if(sT.ss!=null)
                sT.ss.close();
        } catch (IOException e) {
            System.err.println("can not close Socket");
            e.printStackTrace();
        }
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
        if(dasSpiel.isStarted() && !dasSpiel.isOver() && s==1 && dasSpiel.getAbschussSpieler()==1 && !shooting){
            shooting=true;



            logicOUTput.printFeld(dasSpiel.getFeld(),true);
            System.out.println("x&y to shoot: ");
            lx=x;
            ly=y;
            sentReceiveTRun("shot "+lx+" "+ly);

        } else if(!dasSpiel.isStarted() && s==0){              //Schiff Hinzufügen!
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

    private void sentReceiveTRun(String antwort){
        srT=new Thread(new sentReceive(antwort));
        srT.start();
    }

    private Thread srT;
    class sentReceive extends Thread{
        private String antwort;
        public sentReceive(String antwort){
            this.antwort=antwort;
        }

        @Override
        public void run() {
            super.run();
            if(antwort.contains("ships")){
                sT.sendSocket(antwort);
                String z=sT.receiveSocket();
                if(!z.contains("done"))
                    System.err.println("Klient hätte done sein sollen!");
                sT.sendSocket("ready");
                nachricht=sT.receiveSocket();
            }else{
                sT.sendSocket(antwort);
                nachricht=sT.receiveSocket();
            }


        }
    }

}