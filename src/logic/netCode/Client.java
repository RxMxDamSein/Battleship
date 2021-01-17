package logic.netCode;

import java.io.*;
import java.net.Socket;

import logic.RemoteGameClient;
import logic.Spiel;
import logic.logicOUTput;

import java.lang.*;

public class Client {
    public Spiel dasSpiel;

    public void main(String[] args) throws IOException {
        dasSpiel = new Spiel(10, 10, true);
        System.out.println("Connecting to a server ...");
        Socket s = new Socket("127.0.0.1", 420);
        System.out.println("Connected!");
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Writer out = new OutputStreamWriter(s.getOutputStream());
        BufferedReader tIn = new BufferedReader(new InputStreamReader(System.in));

        String antwort = "";
        String nachricht;

        int lx, ly;
        lx = ly = -1;
        while (true) {
            nachricht = in.readLine();
            System.out.println("Von Server: " + nachricht);
            if (nachricht == null)
                break;

            if (nachricht.equals("exit"))
                break;

            if (nachricht.contains("size")) {
                String sizeVariablen = nachricht.substring(5);
                String sizeX = "";
                String sizeY = "";
                int l = 0;
                l = nachricht.length();
                if (l <= 8) {
                    sizeX = sizeVariablen.substring(0, 1);
                    sizeY = sizeVariablen.substring(2);

                } else {
                    sizeX = sizeVariablen.substring(0, 2);
                    sizeY = sizeVariablen.substring(3);
                }
                System.out.println("x: " + sizeX + "Y: " + sizeY);
                antwort = Methods.size(sizeX, sizeY, dasSpiel);
            }

            if (nachricht.contains("ships")) {
                String shipsAnzML = nachricht.substring(6);
                String[] shipsSizes = shipsAnzML.split(" ");
                //int l = shipsAnz.length();
                //char[] shipsArray = new char[l];
                //shipsArray = shipsAnz.toCharArray();

                antwort = Methods.ships(shipsSizes, dasSpiel);
            }

            if (nachricht.contains("shot")) {
                String shotKoordinaten = nachricht.substring(5);
                String kordX = "";
                String kordY = "";
                int tr = 0;
                int l = 0;
                l = nachricht.length();
                switch (l) {
                    case 7:
                        kordX = shotKoordinaten.substring(0, 1);
                        kordY = shotKoordinaten.substring(2);
                        break;
                    case 8:
                        tr = shotKoordinaten.indexOf(" ");
                        switch (tr) {
                            case 1:
                                kordX = shotKoordinaten.substring(0, 1);
                                kordY = shotKoordinaten.substring(2, 3);
                                break;
                            case 2:
                                kordX = shotKoordinaten.substring(0, 2);
                                kordY = shotKoordinaten.substring(3);
                                break;
                            case -1: //Falsche Eingabe
                                break;
                        }
                        break;
                    case 9:
                        kordX = shotKoordinaten.substring(0, 2);
                        kordY = shotKoordinaten.substring(3, 4);
                        break;
                }
                antwort = Methods.shot(kordX, kordY, dasSpiel);
            }

            if (nachricht.contains("ready")) {
                dasSpiel.starteSpiel(0);
                antwort = "ready";
            } else if (nachricht.contains("next") || nachricht.contains("answer 1") || nachricht.contains("answer 2")) {
                if (nachricht.contains("answer 1") || nachricht.contains("answer 2")) {
                    boolean versenkt = false;
                    if (nachricht.contains("answer 2"))
                        versenkt = true;
                    dasSpiel.shoot(lx, ly, 1, 1, versenkt);
                    if (dasSpiel.isOver())
                        break;
                }
                logicOUTput.printFeld(dasSpiel.getFeld(), true);
                System.out.println("x&y to shoot: ");
                lx = Integer.parseInt(tIn.readLine());
                ly = Integer.parseInt(tIn.readLine());
                antwort = "shot " + lx + " " + ly;
            } else if (nachricht.contains("answer 0")) {
                dasSpiel.shoot(lx, ly, 1, 0, false);
                logicOUTput.printFeld(dasSpiel.getFeld(), true);
                antwort = "next";
            }


            System.out.print("Zu Server: " + antwort);
            out.write(String.format("%s%n", antwort));
            out.flush();
        }
        s.close();
        System.out.println("Connection closed!");
    }


}
