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
import java.io.*;
import java.net.Socket;


/**
 * Klasse die das 2Spieler spielen Schiffeversenken Spiel erlaubt mit spicken!
 */
public class Grid_NET_Client {
    private Stage window;
    private Scene sceneOld;
    private Spiel dasSpiel;
    private GridPane gridPlayer1;
    private GridPane gridPlayer2;
    private int[][][] feld;
    private Label[][][] labels;
    private Label[] lToShoot;
    private String nachricht;
    private int lx,ly;
    private boolean shooting=false;
    private Timeline nachrichtChecker;
    private int speed=250;
    private Socket s;
    private BufferedReader in;
    private Writer out;
    private int[] ships;
    private Button buttonStart;
    private Button buttonSave;




    /**
     * Konstruktor initialisiert alles
     * @param window Das Fenster indem das Spiel angezeigt wird
     * @param sceneOld In diese Scene kann mittels des Buttons "Zurück" gesprungen werden
     */
    public Grid_NET_Client(Stage window, Scene sceneOld, String IP, int PORT) throws IOException {
        s = new Socket(IP, PORT);
        System.out.println("Connected!");
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new OutputStreamWriter(s.getOutputStream());
        nachricht=receiveSocket();
        if(nachricht.contains("load")){
            SAFE_SOME safe_some=SAFE_SOME.load(nachricht.split(" ")[1]);
            if(safe_some.game<4 || safe_some.game>5){
                System.err.println("you loaded not a ClientBot game!");
                return;
            }
            Runnable runnable=() -> {
                sendSocket("done");
                if(!receiveSocket().contains("ready")){
                    System.err.println("Server labert wirr");
                }
                sendSocket("ready");
                //System.out.println("pre recieve: "+nachricht);
                nachricht=receiveSocket();
                //System.out.println("runnable nachricht: "+nachricht);
            };
            Thread t=new Thread(runnable);
            t.start();
            dasSpiel=safe_some.spiele[0];
            init(window,dasSpiel.getSizeX(),dasSpiel.getSizeY(),sceneOld,dasSpiel);
        }else{
            int x=Integer.parseInt(nachricht.split(" ")[1]);
            int y=Integer.parseInt(nachricht.split(" ")[2]);
            dasSpiel=new Spiel(x,y,true);
            dasSpiel.init();
            init(window,x,y,sceneOld,dasSpiel);
            sentReceiveTRun("next");
        }

        nachrichtChecker=new Timeline(new KeyFrame(Duration.millis(speed), e->{
            if(dasSpiel.isOver() /*|| b2.isFinOver()*/){
                nachrichtChecker.stop();
            }else {
                String nachricht=this.nachricht;
                this.nachricht="";
                if(nachricht.contains("next") || nachricht.contains("answer 1") || nachricht.contains("answer 2")) {
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
                    if(srT!=null &&srT.isAlive())
                        System.err.println("WTF warum gibts denn srT?!");
                    sentReceiveTRun(z);
                }
                else if(nachricht.contains("ships")){
                    String[] z=nachricht.split(" ");
                    int[] s=new int[z.length-1];
                    for(int i=0;i<z.length-1;i++){
                        s[i]=Integer.parseInt(z[i+1]);
                    }
                    ships=s;
                } else if(nachricht.contains("ready")){
                    if( dasSpiel.starteSpiel(0) ){

                        setLabelAbschuss();
                        if(dasSpiel.isOver())
                            gameOver();
                        sentReceiveTRun("ready");
                        buttonStart.setText("start shooting");
                    }
                }else if(nachricht.contains("save")){
                    String saveID=""+nachricht.split(" ")[1];

                    SAFE_SOME safe_some=new SAFE_SOME(null,new Spiel[]{dasSpiel},5,saveID);
                    sendSocket("done");
                }

            }
        }));
        nachrichtChecker.setCycleCount(Animation.INDEFINITE);
        nachrichtChecker.play();
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
        buttonStart=new Button("Bestätige Schiffe");
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
            SAFE_SOME safe_some=new SAFE_SOME(null,new Spiel[]{dasSpiel},5,saveID);
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
                //srT.join();
                s.close();
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
        for(int i=0;i<ships.length ;i++)
            if(ships[i]>-1)
                return;

        sentReceiveTRun("done");

    }

    /**
     * geht in die im Konstruktor gegebene Scene zurück.
     * -> das Spiel wird verworfen
     */
    private void sceneZutuck(){
        if(nachrichtChecker!=null)
            nachrichtChecker.stop();
        try {

            s.close();
        } catch (IOException e) {
            System.err.println("Can not close Socket");
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
                    int len=(y_old-y<1)?y-y_old+1:y_old-y+1;
                    int idx=inShips(len);
                    if(idx>-1){
                        if(dasSpiel.addShip(x_old,(y_old<y)?y_old:y,false,(y_old-y<1)?y-y_old+1:y_old-y+1,s)){
                            ships[idx]=-1;
                        }
                    }

                    selectDone=true;
                }else if(s_old==s && y_old==y){ //horizontales Schiff
                    int len=(x_old-x<1)?x-x_old+1:x_old-x+1;
                    int idx=inShips(len);
                    if(idx>-1){
                        if(dasSpiel.addShip((x_old<x)?x_old:x,y_old,true,len,s))
                        {
                            ships[idx]=-1;
                        }
                    }

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
    private int inShips(int len){
        if(ships==null)
            return -1;
        for(int i=0;i<ships.length;i++)
            if(len==ships[i])
                return i;
        return -1;
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
            sendSocket(antwort);
            nachricht=receiveSocket();


        }
    }

    private String receiveSocket(){
        try {
            nachricht = in.readLine();
            System.out.println("Von Server: " + nachricht);
        }catch (IOException e){
            System.err.println("Can not receive Nachricht from Server!");
            e.printStackTrace();
        }
        return nachricht;
    }

    private void sendSocket(String antwort){
        System.out.print("Zu Server: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        }catch (IOException e){
            System.err.println("Can not send Antwort to Server!");
            e.printStackTrace();
        }
    }

}