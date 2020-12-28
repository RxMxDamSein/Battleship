package logic;

import sun.security.provider.ConfigFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RemoteGameClient {
    public Spiel dasSpiel;

    /**
     * kreiert ein Spielobjekt mit Groeße 10x10 kann verändert werden!
     */
    public RemoteGameClient(){
        dasSpiel=new Spiel(10,10,true);
    }

    /**
     * Setzt die Spielgröße
     * und legt diese damit fest, sollte man also immer aufrufen. Außer man verwendet das Spiel Objekt selbst und init ed dann auch selbst
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

    /**
     * fügt die Schiffe dem Clienten hinzu mittels der Konsole mit der untigen Hilfsfunktion
     * @param schiffe {5,4,4,3,3,3,2,2,2,2} int Array mit Schiffslängen
     * @return "done" on success null on failure
     */
    public static String ships(int[] schiffe, Spiel dasSpiel){
        for(int i=0;i<schiffe.length;i++){
            if (schiffe[i]<1 || !shipsAddConsole(schiffe[i],dasSpiel))//this should be done with console in future!
                return null;
        }
        return "done";
    }

    /**
     * Hilfsfunktion um über die Konsole beim Klienten ein Schiff aufs Spielbrett zu setzen
     * @param len Schiffslänge
     * @return true on success false on failure
     */
    public static boolean shipsAddConsole(int len,Spiel dasSpiel){
        BufferedReader inRead=new BufferedReader(new InputStreamReader(System.in));
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

    /**
     * Wenn der Host ready sagt ist dies zu tun
     * @return "ready" on Erfolg null on Failure
     */
    public String ready(){
        if(dasSpiel.starteSpiel())
            return "ready";
        else
            return null;
    }

    /**
     * Wenn der Host schießt
     * @param x
     * @param y
     * @return Was der Host getroffen hat 0W, 1S, 2V
     */
    public String shot(int x, int y){
        if(!dasSpiel.shoot(x,y,0,0,false))
            return null;
        switch (dasSpiel.getFeld()[0][x][y]) {
            default:
                return null;
            case 0://Wasser
            case 3://Wasser bereits getroffen auch einfach Wasser zurück
                return "0";
            case 1://Schiff
                if(dasSpiel.istVersenkt())
                    return "2";
                else
                    return "1";
            case 2://bereits getroffen einfach sagen er hat getroffen xD
                return "1";
        }
    }

    /**
     * Wenn du schießt kannst du das verwenden um deinem Spielbrett denn Schuss hinzuzufügen
     * @param x
     * @param y
     * @param wert 0 Wasser, 1 Treffer, 2 Treffer versenkt
     * @return
     */
    public boolean addYourShootToGame(int x,int y,int wert){
        boolean success=false;
        switch (wert){
            default:break;
            case 0://Wasser
                success=dasSpiel.shoot(x,y,1,3,false);
                break;
            case 1://Treffer
                success=dasSpiel.shoot(x,y,1,1,false);
                break;
            case 2://Treffer versenkt
                success=dasSpiel.shoot(x,y,1,1,true);
                break;
        }
        return success;
    }

    /**
     * ToDo
     * @param id
     * @return
     */
    public String save(String id){
        //Game can not save yet to be done
        return "done";
    }

    /**
     * ToDo
     * @param id
     * @return
     */
    public String load(String id){
        //Game can not load yet to be done
        return "done";
    }
}
