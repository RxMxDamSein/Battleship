package logic;


import logic.save.ResourceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public abstract class Bot implements Serializable {
    public Spiel dasSpiel;
    protected int x,y;
    protected Random rdm;

    /**
     * gibt zurück ob der Bot verloren hat!
     * genau wie is Over Function von Spiel
     * @return true Bot hat verloren false Bott lebt noch
     */
    public boolean isFinOver() {
        return dasSpiel.isOver();
    }

    /**
     * Bott Intialisierung des Spielfelds
     * x und y werte müssen >0 sein!
     * @param x Spielbreite
     * @param y Spielhöhe
     */
    public Bot(int x, int y) {
        this.x=x;
        this.y=y;
        dasSpiel=new Spiel(x,y,true);
        dasSpiel.init();
        dasSpiel.setVerbose(false);
        rdm=new Random();
    };

    /**
     * Funktion die Schiffe dem Bott hinzufügt
     * @param s {1,2,3,4,5} legt beim Bott 5 Schiffe an mit den Längen(1,2,3,4,5)
     * @return true wenn erfolgreich false wenn fehler
     */
    public abstract boolean shipSizesToAdd(int[] s);

    /**
     * schießt beim Bot ein Feld ab
     * @param x
     * @param y
     * @return erfolg
     */
    public abstract int abschiesen(int x, int y);

    /**
     * berechnet die nächste Stelle an die der Bot schießt!
     * @return {x,y}
     */
    public abstract  int[] getSchuss();

    /**
     * Falls man das Spielobjekt des Bots gebrauchen kann #ToterCode
     * @return Spiel
     */
    public Spiel getDasSpiel(){
        return dasSpiel;
    }

    /**
     * Hilfsfunktion zum zurücksetzten eines Spielbretts
     * z.Bsp. falls man die Schiffe schlecht gesetzt hat und neu anfagen will
     * @param f feld
     * @param spieler wessens feld soll gelöscht werden
     * @return erfolg
     */
    public boolean resetFeld(int[][][] f,int spieler){
        if(spieler<0 || spieler>1)
            return false;
        //System.out.println("Feld prereset!");
        //logicOUTput.printFeld(f);
        for(int i=0;i<f[spieler].length;i++){
            for(int j=0;j<f[spieler][i].length;j++){
                f[spieler][i][j]=0;
            }
        }
        //System.out.println("Feld reset!");
        //logicOUTput.printFeld(f);
        return true;
    }

    /**
     * Hilfsfunktion um aus der Schiffsliste die Laengen zu extrahieren
     * @param s Arraylist<Schiff>
     * @return {1,2,3,4,5} // alles Schiffslängen in einem int array
     */
    public static int[] getShipSizes(ArrayList<Schiff>s){
        int[] sizes=new int[s.size()];
        for(int i=0;i<sizes.length;i++){
            sizes[i]=s.get(i).schifflaenge;
        }
        return sizes;
    }

    /**
     * Setzt das angegebene Feld auf der Spielseite des Gegners vom Bot ausgesehen
     * dient dazu, dass der Bot weiß was er getroffen hat und ob er wetwas versenkt hat!
     * @param x XKoordinate
     * @param y YKoordinate
     * @param wert dieser wert wird eingesetzt feld[1][x][y]=wert
     * @param versenkt true das hier liegende Schiff wurde versenkt -> daneben muss Wasser sein für KI
     */
    public abstract void setSchussFeld(int x,int y,int wert,boolean versenkt);

    public boolean saveGame(String id){
        try {
            ResourceManager.save(this,id);
        } catch (IOException e) {
            System.err.println("SAVE ERROR!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bot load(String id){
        try {
            return (Bot)ResourceManager.load(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.err.println("load error");
        return null;
    }

    public static void waterAround(int x, int y, int[][][] f,int width,int height){
        int zx=x,zy=y;
        boolean horizontal=false;
        if(zx>0 && f[1][zx-1][zy]==2 || zx<(x-1) && f[1][zx+1][zy]==2)
            horizontal=true;
        int schritt;
        if(horizontal){
            schritt=0;
            while( x+schritt>=0 && x+schritt<width &&f[1][x+schritt][y]==2){
                if(y+1<height)
                    f[1][x+schritt][y+1]=3;
                if(y-1>=0)
                    f[1][x+schritt][y-1]=3;
                schritt++;
            }
            if(x+schritt<width){
                f[1][x+schritt][y]=3;
                if(y+1<height)
                    f[1][x+schritt][y+1]=3;
                if(y-1>=0)
                    f[1][x+schritt][y-1]=3;
            }

            schritt=-1;
            while( x+schritt>=0 && x+schritt<width &&f[1][x+schritt][y]==2){
                if(y+1<height)
                    f[1][x+schritt][y+1]=3;
                if(y-1>=0)
                    f[1][x+schritt][y-1]=3;
                schritt--;
            }
            if(x+schritt>=0){
                f[1][x+schritt][y]=3;
                if(y+1<height)
                    f[1][x+schritt][y+1]=3;
                if(y-1>=0)
                    f[1][x+schritt][y-1]=3;
            }
        }else{
            schritt=0;
            while( y+schritt>=0 && y+schritt<height &&f[1][x][y+schritt]==2){
                if(x+1<width)
                    f[1][x+1][y+schritt]=3;
                if(x-1>=0)
                    f[1][x-1][y+schritt]=3;
                schritt++;
            }
            if(y+schritt<height){
                f[1][x][y+schritt]=3;
                if(x+1<width)
                    f[1][x+1][y+schritt]=3;
                if(x-1>=0)
                    f[1][x-1][y+schritt]=3;
            }
            schritt=-1;
            while( y+schritt>=0 && y+schritt<height &&f[1][x][y+schritt]==2){
                if(x+1<width)
                    f[1][x+1][y+schritt]=3;
                if(x-1>=0)
                    f[1][x-1][y+schritt]=3;
                schritt--;
            }
            if(y+schritt>=0){
                f[1][x][y+schritt]=3;
                if(x+1<width)
                    f[1][x+1][y+schritt]=3;
                if(x-1>=0)
                    f[1][x-1][y+schritt]=3;
            }
        }
    }
}


