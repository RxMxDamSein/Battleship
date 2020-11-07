package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RemoteGame {
    public Spiel dasSpiel;
    private BufferedReader inRead;
    public RemoteGame(){
        inRead = new BufferedReader(new InputStreamReader(System.in));
        dasSpiel=new Spiel(10,10,true);
    }

    /**
     * Setzt die Spielgröße
     * @param x
     * @param y
     * @return null wenn Fehler "next" bei Erfolg
     */
    public String size(int x,int y){
        dasSpiel.setSize(x,y);
        if(!dasSpiel.init())
            return null;
        return "next";
    }

    public String ships(int[] schiffe){
        for(int i=0;i<schiffe.length;i++){
            if (schiffe[i]<1 || !shipsAddConsole(schiffe[i]))//this should be done with console in future!
                return null;
        }
        return "done";
    }

    private boolean shipsAddConsole(int len){
        int x,y,horizontal;
        try {
            System.out.print("Füge ein Schiff der Länge "+len+" hinzu!");
            System.out.print("X Origin: ");
            String in=inRead.readLine();
            x = Integer.parseInt(in);
            System.out.print("Y Origin: ");
            in = inRead.readLine();
            y = Integer.parseInt(in);
            System.out.print("horizontales Schiff?(1 yes 0 no): ");
            in = inRead.readLine();
            horizontal=Integer.parseInt(in);
        } catch (IOException e) {
            return false;
        }catch (NumberFormatException e){
            return false;
        }
        return dasSpiel.addShip(x,y,(horizontal > 0) ? true : false,len,0);
    }

    public String ready(){
        if(dasSpiel.starteSpiel())
            return "ready";
        else
            return null;
    }

    public String shot(int x, int y){
        return null;
    }
}
