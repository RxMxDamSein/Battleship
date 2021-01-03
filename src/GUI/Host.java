package GUI;

import logic.Spiel;
import logic.netCode.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {

    private ServerSocket ss;
    private Socket s;
    private int port,Feldg;
    private BufferedReader in;
    private OutputStreamWriter out;
    private boolean ERROR=false,Spielstartet=false;
    public boolean Connected=false,change=false;
    public String nachricht="";
    private Spiel dasSpiel;

    public Host (int p,int g,Spiel dasSpiel) {
        port=p;
        Feldg=g;
        this.dasSpiel = dasSpiel;
    }

    public void init() {
        Runnable Runnable = ()->{
            ss = null;
            try {
                ss = new ServerSocket(port);
                System.out.println("Waiting for client ...");
                s = ss.accept();
                System.out.println("Connection established");
                Connected=true;
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                sendSocket("size "+Feldg+" "+Feldg);
                String z = receiveSocket();
                if (!z.contains("next")) {
                    CutConnection();
                }

            } catch (IOException e) {
                System.err.println("Can not create Socket!");
                e.printStackTrace();
            }

        };
        Thread t = new Thread(Runnable);
        t.start();


    }

    public  void sendSocket(String antwort){
        System.out.print("Zu Client: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        }catch (IOException e){
            System.err.println("Can not send Antwort to Client!");
            e.printStackTrace();
        }catch (NullPointerException e){
            System.err.println("can not sent anything if it does not exist!");
            e.printStackTrace();
        }
    }

    public String receiveSocket(){
        String nachricht="ERROR";
        try {
            nachricht = in.readLine();
            System.out.println("Von Client: " + nachricht);
        }catch (IOException e){
            System.err.println("Can not receive Nachricht from Client!");
            e.printStackTrace();
        }
        return nachricht;
    }

    public void senships(int[] shipSizes) {
        Runnable Runnable = ()->{
            String antwort="ships";
            for(int i=shipSizes.length-1;i>=0;i--){

                antwort+=" "+shipSizes[i];
            }
            sendSocket(antwort);
            String z = receiveSocket();
            if (!z.contains("done")) {
                CutConnection();
            }
            sendSocket("ready");
            z = receiveSocket();
            if (!z.contains("ready")) {
                CutConnection();
            }
            Spielstartet = true;
        };
        Thread t = new Thread(Runnable);
        t.start();
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
    private boolean shooting=false;

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
                sendSocket("next");
                nachricht = receiveSocket();
                if (nachricht.contains("shot"))
                    Clientshot(nachricht);
                else
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

    private void Clientshot(String nachricht) {
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
                CutConnection();
                return;
        }
        sendSocket(antwort);
        String z = receiveSocket();
        if (z.contains("shot")) {
            Clientshot(z);
        }
        sonderNachrichten(z);
    }
    private void sonderNachrichten (String nachricht) {

    }
}
