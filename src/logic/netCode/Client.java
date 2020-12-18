package logic.netCode;

import java.io.*;
import java.net.Socket;

public class Client
{


    public static void main (String [] args) throws IOException{
        System.out.println("Connecting to a server ...");
        Socket s = new Socket("127.0.0.1", 420);
        System.out.println("Connected!");
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Writer out = new OutputStreamWriter(s.getOutputStream());
        BufferedReader tIn = new BufferedReader(new InputStreamReader(System.in));

        String antwort="";
        String nachricht;
        int versenkt = 0;

        while(true)
        {
            nachricht = in.readLine();
            System.out.println("Von Server: " + nachricht);

            if(nachricht.equals("exit"))
                break;

            if(nachricht.contains("size"))
            {
                String sizeVariablen = nachricht.substring(5);
                String sizeX = sizeVariablen.substring(0, 1);
                String sizeY = sizeVariablen.substring(2);

                antwort = size(sizeX, sizeY);
            }

            if(nachricht.contains("ships"))
            {
                String shipsAnz = nachricht.substring(6);

                antwort = ships(shipsAnz);
            }

            if(nachricht.contains("ready"))
            {
                ready();
                antwort = tIn.readLine();
            }

            if(nachricht.contains("shot"))
            {
                String shotKordinaten = nachricht.substring(5);
                System.out.println(shotKordinaten);
                String kordX = shotKordinaten.substring(0, 1);
                System.out.println("X-Koordinate: " + kordX);
                String kordY = shotKordinaten.substring(2);
                System.out.println("Y-Koordinate: " + kordY);

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
                        System.out.println("Schiffe versenkt: " + versenkt);
                        antwort = tIn.readLine();
                        break;
                }
            }

            System.out.print("Zu Server: " + antwort + "\n");
            out.write(String.format("%s%n", antwort));
            out.flush();
        }

    }

    public static String shot(String x, String y)
    {
        System.out.println("Shot funktion wurde ausgeführt!");
        //System.out.println("Shot auf " + x + " " + y);
        if(x.equals("1") && y.equals("2"))
            return "answer 1";
        if(x.equals("1") && y.equals("3"))
            return "answer 2";
        if(x.equals("3") && y.equals("3"))
            return "answer 1";
        else
            return "answer 0";
    }

    public static String size(String x, String y)
    {
        System.out.println("Size funktion wurde ausgeführt!");
        System.out.println("Feldgröße ist nun " + x + " * " + y);
        return "next";
    }

    public static String ships(String shipsString)
    {
        char[] shipsArray = shipsString.toCharArray();
        int count = 0;
        for (char c : shipsArray) {
            if (c != ' ')
                count++;
        }

        //System.out.println(count);

        for(int k = 0;k < count;k++)
        {
            System.out.println("Schiff Nr." + k + " hat die Größe " + shipsArray[k * 2]);
        }

        System.out.println("Ships funktion wurde ausgeführt!");
        return "done";
    }

    public static void ready()
    {
        System.out.println("Ready funktion wurde ausgeführt!");
    }

}
