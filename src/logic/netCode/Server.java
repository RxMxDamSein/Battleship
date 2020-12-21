package logic.netCode;

import logic.Spiel;
import java.lang.*;
import java.net.*;
import java.io.*;


public class Server
{
public Spiel dasSpiel;


    public void main(String[] args) throws IOException{
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

        while(true)
        {
            if(sCount == 0)
            antwort = tIn.readLine();

            if(nachricht.equals("exit"))
                break;

            if(nachricht.contains("shot"))
            {
                String shotKoordinaten = "";
                String kordX = "";
                String kordY = "";
                int tr = 0;
                int l = 0;
                l = nachricht.length();
                switch (l)
                {
                    case 8: shotKoordinaten = nachricht.substring(5);
                            kordX = shotKoordinaten.substring(0, 1);
                            kordY = shotKoordinaten.substring(2);
                        break;
                    case 9: shotKoordinaten = nachricht.substring(5);
                            tr = shotKoordinaten.indexOf(" ");
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
                    case 10: shotKoordinaten = nachricht.substring(5);
                             kordX = shotKoordinaten.substring(0, 2);
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

            if(nachricht.equals("next"))
            {
                antwort = tIn.readLine();
            }

            if(nachricht.equals("done"))
            {
                antwort = tIn.readLine();
            }

            System.out.print("Zu Client: " + antwort + "\n");
            out.write(String.format("%s%n", antwort));
            out.flush();

            nachricht = in.readLine();
            System.out.println("Von Client: " + nachricht);

            sCount++;
        }

        s.close();
        System.out.println("Connection closed!");
    }


    public String shot(String sx, String sy)
    {
        int x = Integer.parseInt(sx);
        int y = Integer.parseInt(sy);
        dasSpiel.shoot(x,y,0);
        return "";
    }

}