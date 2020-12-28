package logic.netCode;

import logic.Spiel;
import java.lang.*;
import java.net.*;
import java.io.*;
import logic.logicOUTput;


public class Server
{
public Spiel dasSpiel;


    public void main(String[] args) throws IOException{
        dasSpiel=new Spiel(10,10,true);
        ServerSocket ss = new ServerSocket(420);
        System.out.println("Waiting for client ...");
        Socket s = ss.accept();
        System.out.println("Connection established");

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Writer out = new OutputStreamWriter(s.getOutputStream());
        BufferedReader tIn = new BufferedReader(new InputStreamReader(System.in));

        String antwort="";
        String nachricht="";
        int sCount = 0;

        int lx,ly;
        lx=ly=-1;
        while(true)
        {
            if(!antwort.contains("ready") && !antwort.contains("shot")&& !antwort.contains("next")&& !antwort.contains("answer")){
                if(nachricht.contains("next") && antwort.contains("answer 0")){

                }else{
                    antwort = tIn.readLine();
                }
            }


            if(antwort.contains("size") || antwort.equals("exit") || antwort.contains("ships") ){
                if(antwort.equals("exit"))
                    break;
                else if(antwort.contains("size"))
                {
                    String sizeVariablen = antwort.substring(5);
                    String sizeX = "";
                    String sizeY = "";
                    int l = 0;
                    l =antwort.length();
                    if(l<=8) {
                        sizeX = sizeVariablen.substring(0, 1);
                        sizeY = sizeVariablen.substring(2);

                    }else{
                        sizeX = sizeVariablen.substring(0, 2);
                        sizeY = sizeVariablen.substring(3);
                    }
                    Methods.size(sizeX, sizeY,dasSpiel);
                }
                else if(antwort.contains("ships"))
                {
                    String shipsAnzML = antwort.substring(6);
                    String[] shipsAnz = shipsAnzML.split(" ");

                    Methods.ships(shipsAnz,dasSpiel);
                    System.out.print("Zu Client: " + antwort + "\n");
                    out.write(String.format("%s%n", antwort));
                    out.flush();

                    nachricht = in.readLine();
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
                    lx=Integer.parseInt(tIn.readLine());
                    ly=Integer.parseInt(tIn.readLine());
                    antwort="shot "+lx+" "+ly;
                }
                else if(nachricht.contains("answer 0")){
                    dasSpiel.shoot(lx,ly,1,0,false);
                    logicOUTput.printFeld(dasSpiel.getFeld(),true);
                    antwort="next";
                }
            }




            System.out.print("Zu Client: " + antwort + "\n");
            out.write(String.format("%s%n", antwort));
            out.flush();

            nachricht = in.readLine();
            System.out.println("Von Client: " + nachricht);
            if(nachricht==null)
                break;
            sCount++;
        }

        s.close();
        System.out.println("Connection closed! "+sCount);
    }





}