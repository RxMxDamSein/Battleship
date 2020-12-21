package logic.netCode;

import java.io.*;
import java.net.Socket;
import logic.Spiel;
import java.lang.*;

public class Client
{
    public Spiel dasSpiel;

    public void main (String [] args) throws IOException{
        System.out.println("Connecting to a server ...");
        Socket s = new Socket("127.0.0.1", 420);
        System.out.println("Connected!");
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Writer out = new OutputStreamWriter(s.getOutputStream());
        BufferedReader tIn = new BufferedReader(new InputStreamReader(System.in));

        String antwort="";
        String nachricht;

        while(true)
        {
            nachricht = in.readLine();
            System.out.println("Von Server: " + nachricht);

            if(nachricht.equals("exit"))
                break;

            if(nachricht.contains("size"))
            {
                String sizeVariablen = nachricht.substring(5);
                String sizeX = "";
                String sizeY = "";
                int l = 0;
                l =nachricht.length();
                switch(l)
                {
                    case 7: sizeX = sizeVariablen.substring(0, 1);
                        sizeY = sizeVariablen.substring(2);
                        break;
                    case 8: sizeX = sizeVariablen.substring(0, 2);
                        sizeY = sizeVariablen.substring(3, 4);
                        break;
                }

                antwort = size(sizeX, sizeY);
            }

            if(nachricht.contains("ships"))
            {
                String shipsAnz = nachricht.substring(6);

                antwort = ships(shipsAnz);
            }

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
                antwort = shot(kordX, kordY);
            }

            if(nachricht.equals("answer 0") || nachricht.equals("answer 1") || nachricht.equals("answer 2"))
            {
                switch(nachricht)
                {
                    case "answer 0": antwort = "next";
                        break;
                    case "answer 1": antwort = tIn.readLine();
                        break;
                    case "answer 2": antwort = tIn.readLine();
                        break;
                }
            }

            System.out.print("Zu Server: " + antwort);
            out.write(String.format("%s%n", antwort));
            out.flush();
        }

    }

    public String shot(String sx, String sy)
    {
        int x = Integer.parseInt(sx);
        int y = Integer.parseInt(sy);
        dasSpiel.shoot(x,y,0);
        return "";
    }

    public String size(String sx, String sy)
    {
        int x = Integer.parseInt(sx);
        int y = Integer.parseInt(sy);
        dasSpiel.setSize(x, y);
        return "next";
    }

    public String ships(String shipsString)
    {

        return "done";
    }

}
