package GUI;

import logic.*;
import logic.netCode.Server;
import logic.save.SAFE_SOME;
import sun.security.provider.ConfigFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class BotClient {
    private Socket s;
    private BufferedReader in;
    private OutputStreamWriter out;
    public boolean ERROR=false,change=false,loaded=false,pause=false;
    public Spiel dasSpiel;
    public Bot derBot;
    public int[] ships;//if ships is added make it -1
    public int status=0;
    private nuetzlicheMethoden methoden;
    // 0 = keine verbindung
    // 1 = Verbindung und Spielfeld
    // 2 = Schiffsgrößen erhalten






    public BotClient(String IP, Integer PORT,int bot) {
        Runnable runnable = ()->{
            try {
                s = new Socket(IP, PORT);
                System.out.println("Connected!");
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                String z =receiveSocket();
                if (z.contains("load")) {
                    loaded=true;
                    SAFE_SOME safe_some=SAFE_SOME.load(z.split(" ")[1]);
                    dasSpiel=safe_some.spiele[0];
                    int x =dasSpiel.getSizeX();
                    int y =dasSpiel.getSizeY();
                    methoden = new nuetzlicheMethoden(dasSpiel.getSizeX());
                    switch (bot) {
                        case 1:
                            derBot = new RDM_Bot(x,y);
                            break;
                        case 2:
                            derBot = new Bot_lvl_2(x,y);
                            break;
                        case 3:
                            derBot = new Bot_schwer(x,y);
                            break;
                        default:
                            System.err.println("Bot Error!!");
                            ERROR = true;
                            CutConnection();
                            return;
                    }
                    derBot.dasSpiel = dasSpiel;
                    status=2;
                    System.out.println("status "+status+" error "+ERROR);
                    sendSocket("done");
                    if(!receiveSocket().contains("ready")){
                        CutConnection();
                        return;
                    }
                    sendSocket("ready");
                    z = receiveSocket();
                    if (z.contains("shot"))
                        Servershot(z);
                    else
                        sonderNachrichten(z);

                } else if (z.contains("size")) {
                    int x = Integer.parseInt(z.split(" ")[1]),y=Integer.parseInt(z.split(" ")[1]);
                    switch (bot) {
                        case 1:
                            derBot = new RDM_Bot(x,y);
                            break;
                        case 2:
                            derBot = new Bot_lvl_2(x,y);
                            break;
                        case 3:
                            derBot = new Bot_schwer(x,y);
                            break;
                        default:
                            System.err.println("Bot Error!!");
                            ERROR = true;
                            CutConnection();
                            return;
                    }
                    dasSpiel = derBot.dasSpiel;
                    status = 1;
                    sendSocket("next");
                    z = receiveSocket();
                    if (!z.contains("ships")) {
                        System.err.println("Ships von Server erwartet!!");
                        CutConnection();
                        return;
                    }
                    String[] s = z.split(" ");
                    ships = new int[s.length-1];
                    for (int i=1;i<s.length;i++) {
                        ships[i-1] = Integer.parseInt(s[i]);
                    }
                    status = 2;
                    senships();
                } else {
                    CutConnection();
                    return;
                }
            } catch (IOException e) {
                ERROR=true;
                System.err.println("Cannot create Socket!!");
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }

    public  void sendSocket(String antwort){
        System.out.print("Zu Server: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        }catch (IOException e){
            System.err.println("Can not send Antwort to Server!");
            e.printStackTrace();
        }catch (NullPointerException e){
            System.err.println("can not sent anything if it does not exist!");
            e.printStackTrace();
        }
    }


    public boolean inShips(int len){
        if(ships==null)
            return false;
        for(int i: ships){
            if(len==i)
                return true;
        }
        return false;
    }

    public void deleteShip(int len){
        for(int i=0;i<ships.length;i++)
            if(ships[i]==len){
                ships[i]=-1;
                return;
            }
    }
    public String receiveSocket(){
        String nachricht="ERROR";
        try {
            nachricht = in.readLine();
            System.out.println("Von Server: " + nachricht);
        }catch (IOException e){
            System.err.println("Can not receive Nachricht from Server!");
            e.printStackTrace();
        }
        if (nachricht == null) {
            CutConnection();
        }
        return nachricht;
    }

    public void CutConnection() {
        ERROR = true;
        System.out.println("Closing Connection!");
        try {
            s.close();
        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            e.printStackTrace();
        }
    }

    public void save(String hash,String dname) {
        new SAFE_SOME(null,new Spiel[]{dasSpiel},4,hash,dname);
        sendSocket("save "+hash);
        if (!receiveSocket().contains("done")) {
            System.err.println("Client hat nicht wahrscheinlich gespeichert");
            CutConnection();
            return;
        }
        CutConnection();
    }

    public void schuss() {
        if (dasSpiel.getAbschussSpieler() != 1 || !dasSpiel.isStarted() || dasSpiel.isOver() ) {
            System.err.println("NIX SCHUSS");
            return;
        }
        Runnable runnable = ()->{
            while (pause);
            int[] xy= derBot.getSchuss();
            int x = xy[0];
            int y = xy[1];
            sendSocket("shot "+(x+1)+" "+(y+1));
            String z = receiveSocket();
            System.out.println(z);
            if (!z.contains("answer")) {
                CutConnection();
            }
            if (z.contains("1")) {
                dasSpiel.shoot(x,y,1,1,false);
                derBot.setSchussFeld(x,y,2,false);
                change = true;
                schuss();
            } else if(z.contains("2")) {
                dasSpiel.shoot(x,y,1,1,true);
                derBot.setSchussFeld(x,y,2,true);
                change = true;

                //
                if (dasSpiel.isOver()) {
                    methoden.GameEnd(true);
                }
                ////////////
                schuss();
            } else if (z.contains("0")) {
                dasSpiel.shoot(x,y,1,0,false);
                derBot.setSchussFeld(x,y,3,false);
                change = true;
                sendSocket("next");
                String nachricht = receiveSocket();
                if (nachricht.contains("shot")) {
                    Servershot(nachricht);
                } else
                    sonderNachrichten(nachricht);
            } else {
                CutConnection();
            }
            change = true;
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    private void Servershot(String nachricht) {
        int x = Integer.parseInt(nachricht.split(" ")[1])-1;
        int y = Integer.parseInt(nachricht.split(" ")[2])-1;
        dasSpiel.shoot(x,y,0,0,false);
        change = true;
        String antwort = "answer ";
        switch (dasSpiel.getFeld()[0][x][y]) {
            case 2:
                if (dasSpiel.istVersenkt()) {
                    antwort+="2";
                } else {
                    //treffer
                    antwort+="1";
                }
                ////////////
                if (dasSpiel.isOver()) {
                    methoden.GameEnd(false);
                }
                ////////////////
                break;
            case 3:
                //Wasser
                antwort+="0";
                break;
                ///////////////////////
            case 4:
                antwort+="0";
                break;
                ////////////////
            default:
                System.err.println("Spielbrett sollte beschossen sein");
                CutConnection();
                return;
        }
        sendSocket(antwort);
        String z = receiveSocket();
        if (z.contains("shot")) {
            Servershot(z);
            return;
        }
        sonderNachrichten(z);
    }

    private void sonderNachrichten (String nachricht) {
        if (nachricht.contains("save")) {
            new SAFE_SOME(null,new Spiel[]{dasSpiel},4,nachricht.split(" ")[1]);
            sendSocket("done");
            CutConnection();
        } else if (nachricht.contains("next")) {
            schuss();
        } else {
            CutConnection();
        }

    }

    public boolean senships() {
        Runnable runnable=() ->{
            if (!derBot.shipSizesToAdd(ships)) {
                CutConnection();
                System.err.println("Bot shipsizestoadd fehler");
                return;
            }
            dasSpiel.setAbschussSpieler(0);
            sendSocket("done");
            if(!receiveSocket().contains("ready")){
                System.err.println("Server sollte eigentlich Spiel starten!");
                CutConnection();
                return;
            }
            dasSpiel.starteSpiel(0);
            sendSocket("ready");
            String z=receiveSocket();
            if(z.contains("shot")){
                Servershot(z);
            }else {
                sonderNachrichten(z);
            }

        };
        runnable.run();
        return true;
    }
}
