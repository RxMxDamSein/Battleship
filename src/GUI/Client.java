package GUI;

import logic.Bot;
import logic.Spiel;
import logic.netCode.Server;
import logic.save.SAFE_SOME;
import sun.security.provider.ConfigFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Serializable {
    private static final long serialVersionUID=1337L;
    private Socket s;
    private BufferedReader in;
    private OutputStreamWriter out;
    private boolean shooting=false;
    public boolean ERROR=false,change=false;
    public Spiel dasSpiel;
    public int[] ships;//if ships is added make it -1
    public int status=0;
    // 0 = keine verbindung
    // 1 = Verbindung und Spielfeld
    // 2 = Schiffsgrößen erhalten






    public Client(String IP, Integer PORT) {
        Runnable runnable = ()->{
            try {
                s = new Socket(IP, PORT);
                System.out.println("Connected!");
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                String z =receiveSocket();
                if (z.contains("load")) {
                    SAFE_SOME safe_some=SAFE_SOME.load(z.split(" ")[1]);
                    Client c=(Client) safe_some.objects[0];
                    shooting=c.shooting;
                    ERROR=c.ERROR;
                    change=c.change;
                    dasSpiel=c.dasSpiel;
                    ships=c.ships;
                    status=c.status;
                    sendSocket("done");
                    if(!receiveSocket().contains("ready")){
                        CutConnection();
                        return;
                    }
                    sendSocket("ready");
                    if (dasSpiel.getAbschussSpieler() == 0) {
                        sendSocket("next");
                        z = receiveSocket();
                        if (z.contains("shot"))
                            Servershot(z);
                        else
                            sonderNachrichten(z);
                    }

                } else if (z.contains("size")) {
                    dasSpiel = new Spiel(Integer.parseInt(z.split(" ")[1]),Integer.parseInt(z.split(" ")[2]),true);
                    dasSpiel.init();
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

    public void save(String hash) {
        sendSocket("save "+hash);
        if (!receiveSocket().contains("done")) {
            System.err.println("Client hat nicht wahrscheinlich gespeichert");
            CutConnection();
            return;
        }
        CutConnection();
    }

    public void schuss(int x, int y) {
        if (dasSpiel.getAbschussSpieler() != 1 || !dasSpiel.isStarted() || dasSpiel.isOver() || shooting) {
            System.err.println("NIX SCHUSS");
            return;
        }
        shooting = true;
        Runnable runnable = ()->{
            sendSocket("shot "+x+" "+y);
            String z = receiveSocket();
            System.out.println(z);
            if (!z.contains("answer")) {
                CutConnection();
            }
            if (z.contains("1")) {
                dasSpiel.shoot(x,y,1,1,false);
            } else if(z.contains("2")) {
                dasSpiel.shoot(x,y,1,1,true);
            } else if (z.contains("0")) {
                dasSpiel.shoot(x,y,1,0,false);
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
            shooting = false;
            change = true;
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    private void Servershot(String nachricht) {
        int x = Integer.parseInt(nachricht.split(" ")[1]);
        int y = Integer.parseInt(nachricht.split(" ")[2]);
        dasSpiel.shoot(x,y,0,0,false);
        change = true;
        String antwort = "answer ";
        switch (dasSpiel.getFeld()[0][x][y]) {
            case 2:
                if (dasSpiel.istVersenkt()) {
                    antwort+="2";
                } else {
                    antwort+="1";
                }
                break;
            case 3:
                antwort+="0";
                break;
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
            new SAFE_SOME(this);
            sendSocket("done");
            CutConnection();
        } else if (nachricht.contains("next")) {
            System.out.println("Sie sind an der Reihe!");
        } else {
            CutConnection();
        }

    }

    public boolean senships() {
        for (int i: ships)
        {
            if(i>-1)
                return false;
        }
        Runnable runnable=() ->{
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
