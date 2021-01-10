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
import logic.*;
import logic.save.SAFE_SOME;

import java.io.*;
import java.net.Socket;


/**
 * Klasse die das 2Spieler spielen Schiffeversenken Spiel erlaubt mit spicken!
 */
public class Grid_NET_Client_B {
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
    private Timeline nachrichtChecker;
    private int speed=10;
    private Socket s;
    private BufferedReader in;
    private Writer out;
    private Button buttonStart;
    private Bot derBot;
    private Button buttonSave;





    private void botschuss(){
        int[] xy=derBot.getSchuss();
        lx=xy[0];
        ly=xy[1];
        sentReceiveTRun("shot "+lx+" "+ly);
    }
    /**
     * Konstruktor initialisiert alles
     * @param window Das Fenster indem das Spiel angezeigt wird
     * @param sceneOld In diese Scene kann mittels des Buttons "Zurück" gesprungen werden
     */
    public Grid_NET_Client_B(Stage window, Scene sceneOld, String IP, int PORT) throws IOException {
        s = new Socket(IP, PORT);
        System.out.println("Connected!");
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new OutputStreamWriter(s.getOutputStream());
        nachricht=receiveSocket();
        if(nachricht.contains("load")){
            SAFE_SOME safe_some=SAFE_SOME.load(nachricht.split(" ")[1]);
            if(safe_some.game!=5){
                System.err.println("you loaded not a NETBot game!");
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
            derBot=safe_some.bots[0];
            dasSpiel=safe_some.spiele[0];
            init(window,dasSpiel.getSizeX(),dasSpiel.getSizeY(),sceneOld);
        }else{
            int x=Integer.parseInt(nachricht.split(" ")[1]);
            int y=Integer.parseInt(nachricht.split(" ")[2]);
            derBot= new Bot_lvl_2(x,y);
            init(window,x,y,sceneOld);
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
                        if (nachricht.contains("answer 2")){


                            versenkt = true;
                        }
                        dasSpiel.shoot(lx, ly, 1, 1, versenkt);
                        derBot.setSchussFeld(lx,ly,2,versenkt);
                        setLabelAbschuss();
                        updatePlayerGrids();
                        if(dasSpiel.isOver()){
                            gameOver();
                            return;
                        }
                        botschuss();
                    }else if(nachricht.contains("next")){
                        botschuss();
                    }
                }else if(nachricht.contains("answer 0")){
                    dasSpiel.shoot(lx,ly,1,0,false);
                    derBot.setSchussFeld(lx,ly,3,false);
                    setLabelAbschuss();
                    updatePlayerGrids();
                    if(dasSpiel.isOver())
                        gameOver();
                    if(srT.isAlive())
                        System.err.println("WTF warum gibts denn srT?!");
                    sentReceiveTRun("next");
                }else if(nachricht.contains("shot")){
                    int x_ = Integer.parseInt(nachricht.split(" ")[1]);
                    int y_ = Integer.parseInt(nachricht.split(" ")[2]);
                    dasSpiel.shoot(x_,y_,0,0,false);
                    setLabelAbschuss();
                    updatePlayerGrids();
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
                    if(srT!=null && srT.isAlive())
                        System.err.println("WTF warum gibts denn srT?!");
                    sentReceiveTRun(z);
                    if(dasSpiel.isOver())
                        gameOver();
                }
                else if(nachricht.contains("ships")){
                    String[] z=nachricht.split(" ");
                    int[] s=new int[z.length-1];
                    for(int i=0;i<z.length-1;i++){
                        s[i]=Integer.parseInt(z[i+1]);
                    }
                    if(derBot.shipSizesToAdd(s)){
                        derBot.dasSpiel.setAbschussSpieler(0);
                        sentReceiveTRun("done");
                        updatePlayerGrids();
                    }else {
                        System.err.println("Es ist nicht möglich die Schiffe hinzuzufügen!");
                    }


                } else if(nachricht.contains("ready")){

                    setLabelAbschuss();
                    if(dasSpiel.isOver())
                        gameOver();
                    sentReceiveTRun("ready");
                    buttonStart.setText("start shooting");

                } else if(nachricht.contains("save")){
                    String saveID=""+nachricht.split(" ")[1];

                    SAFE_SOME safe_some=new SAFE_SOME(new Bot[]{derBot},new Spiel[]{dasSpiel},5,saveID);
                    sendSocket("done");
                }else if(nachricht.contains("done")){//Server hat gespeichert eigentlich jetzt schließen

                }

            }
        }));
        nachrichtChecker.setCycleCount(Animation.INDEFINITE);
        nachrichtChecker.play();
    }

    private void init(Stage window, int x, int y, Scene sceneOld){
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
        this.dasSpiel=derBot.dasSpiel;
        //this.dasSpiel.init();
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
            SAFE_SOME safe_some=new SAFE_SOME(new Bot[]{derBot},new Spiel[]{dasSpiel},5,saveID);
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
                in.close();
                out.close();
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
            in.close();
            out.close();
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
                            if(dasSpiel.istVersenkt()){
                                labels[s][i][j].setTextFill(Color.web("darkred"));
                            }else {
                                labels[s][i][j].setTextFill(Color.web("red"));
                            }
                            break;
                        case 3:
                            labels[s][i][j].setTextFill(Color.web("blue"));
                            break;
                        case 4:
                            labels[s][i][j].setTextFill(Color.web("darkred"));
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
        /*if(dasSpiel.isStarted() && !dasSpiel.isOver() && s==1 && dasSpiel.getAbschussSpieler()==1 && !shooting){
            shooting=true;
            logicOUTput.printFeld(dasSpiel.getFeld(),true);
            System.out.println("x&y to shoot: ");
            lx=x;
            ly=y;
            sentReceiveTRun("shot "+lx+" "+ly);

        } */
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
        String nachricht="";
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