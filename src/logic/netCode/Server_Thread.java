package logic.netCode;

import logic.Spiel;
import logic.logicOUTput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server_Thread extends Thread {
    public Spiel dasSpiel;
    public BufferedReader in;
    public Writer out;
    public Socket s;
    private int port;
    private int x, y;
    public boolean wait;
    public ServerSocket ss;

    @Override
    public void run() {
        super.run();
        ss = null;
        try {
            ss = new ServerSocket(port);
            System.out.println("Waiting for client ...");
            s = ss.accept();
            System.out.println("Connection established");

        } catch (IOException e) {
            System.err.println("Can not create Socket!");
            e.printStackTrace();
        }
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());
        } catch (IOException e) {
            System.err.println("can not create IO Stream!");
        }
        lx = ly = -1;

        sendSocket("size " + x + " " + y);
        if (!receiveSocket().contains("next")) {
            System.err.println("Klient ist falsch!");
        }
    }
        /*sendSocket("size "+x+" "+y);
        nachricht=receiveSocket();
        if(!nachricht.contains("next")){
            System.err.println("Klient antwortet wir!");
        }

        while(true)
        {
            if(!antwort.contains("ready") && !antwort.contains("shot")&& !antwort.contains("next")&& !antwort.contains("answer")){
                if(nachricht.contains("next") && antwort.contains("answer 0")){

                }else{//size exit ships
                    System.out.println("now waiting");
                    wait=true;
                    while (wait);
                    System.out.println("fin wait");

                }
            }


            if(antwort.contains("size") || antwort.equals("exit") || antwort.contains("ships") ){
                if(antwort.equals("exit"))
                    break;
                else if(antwort.contains("ships"))
                {
                    String shipsAnzML = antwort.substring(6);
                    String[] shipsAnz = shipsAnzML.split(" ");

                    Methods.ships(shipsAnz,dasSpiel);
                    System.out.print("Zu Client: " + antwort + "\n");
                    //out.write(String.format("%s%n", antwort));
                    //out.flush();

                    //nachricht = in.readLine();
                    System.out.println("Von Client: " + nachricht);
                    if(!nachricht.contains("done"))
                        break;
                    dasSpiel.starteSpiel(1);
                    antwort="ready";
                }

            }else{
                if(nachricht.contains("shot"))
                {
                    String shotKoordinaten = nachricht.substring(5);
                    String kordX = "";
                    String kordY = "";
                    int tr = 0;
                    int l = 0;
                    l = nachricht.length();
                    switch (l)
                    {
                        case 7: kordX = shotKoordinaten.substring(0, 1);
                            kordY = shotKoordinaten.substring(2);
                            break;
                        case 8: tr = shotKoordinaten.indexOf(" ");
                            switch(tr)
                            {
                                case 1: kordX = shotKoordinaten.substring(0, 1);
                                    kordY = shotKoordinaten.substring(2, 3);
                                    break;
                                case 2: kordX = shotKoordinaten.substring(0, 2);
                                    kordY = shotKoordinaten.substring(3);
                                    break;
                                case -1: //Falsche Eingabe
                                    break;
                            }
                            break;
                        case 9: kordX = shotKoordinaten.substring(0, 2);
                            kordY = shotKoordinaten.substring(3, 4);
                            break;
                    }
                    antwort = Methods.shot(kordX, kordY,dasSpiel);
                }

                if(nachricht.contains("next")||nachricht.contains("ready") || nachricht.contains("answer 1") || nachricht.contains("answer 2")){
                    if(nachricht.contains("answer 1") || nachricht.contains("answer 2")){
                        boolean versenkt=false;
                        if(nachricht.contains("answer 2"))
                            versenkt=true;
                        dasSpiel.shoot(lx,ly,1,1,versenkt);
                        if(dasSpiel.isOver())
                            break;
                    }
                    logicOUTput.printFeld(dasSpiel.getFeld(),true);
                    System.out.println("x&y to shoot: ");
                    //lx=Integer.parseInt(tIn.readLine());
                    //ly=Integer.parseInt(tIn.readLine());
                    synchronized (this){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    antwort="shot "+lx+" "+ly;
                }
                else if(nachricht.contains("answer 0")){
                    dasSpiel.shoot(lx,ly,1,0,false);
                    logicOUTput.printFeld(dasSpiel.getFeld(),true);
                    antwort="next";
                }
            }




            sendSocket(antwort);

            nachricht=receiveSocket();
            if(nachricht==null)
                break;
            sCount++;
        }
        try{
            s.close();
        }catch (IOException e){
            System.err.println("Can not close Socket!");
            e.printStackTrace();
        }

        System.out.println("Connection closed! "+sCount);
    }

         */

    private String antwort = "";
    private String nachricht = "";
    private int sCount = 0;
    private int lx, ly;


    public Server_Thread(int x, int y, int port) {
        dasSpiel = new Spiel(x, y, true);
        dasSpiel.init();
        this.x = x;
        this.y = y;
        this.port = port;
    }


    public void sendSocket(String antwort) {
        System.out.print("Zu Client: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        } catch (IOException e) {
            System.err.println("Can not send Antwort to Client!");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("can not sent anything if it does not exist!");
            e.printStackTrace();
        }
    }

    public String receiveSocket() {
        try {
            nachricht = in.readLine();
            System.out.println("Von Client: " + nachricht);
        } catch (IOException e) {
            System.err.println("Can not receive Nachricht from Client!");
            e.printStackTrace();
        }
        return nachricht;
    }


}