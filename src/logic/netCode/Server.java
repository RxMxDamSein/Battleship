package logic.netCode;

import java.net.*;
import java.io.*;


public class Server
{

    public static void main (String [] args) throws IOException{
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
        int versenkt = 0;

        while(true)
        {
            if(sCount == 0)
            antwort = tIn.readLine();

            if(nachricht.equals("exit"))
                break;

            if(nachricht.contains("shot"))
            {
                String shotKordinaten = nachricht.substring(5);
                System.out.println(shotKordinaten);
                String kordX = shotKordinaten.substring(0, 1);
                String kordY = shotKordinaten.substring(2);

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
                    case "answer 2": versenkt++;
                        System.out.println(versenkt);
                        antwort = tIn.readLine();
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

            if(nachricht.equals("ready"))
            {
                ready();
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


    public static String shot(String x, String y)
    {
        System.out.println("Shot funktion wurde ausgeführt!");
        System.out.println("Shot auf " + x + " " + y);
        if(x.equals("4") && y.equals("4"))
            return "answer 1";
        if(x.equals("3") && y.equals("1"))
            return "answer 1";
        if(x.equals("5") && y.equals("3"))
            return "answer 2";
        else
            return "answer 0";
    }

    public static void ready()
    {
        System.out.println("Ready funktion wurde ausgeführt!");
    }

}