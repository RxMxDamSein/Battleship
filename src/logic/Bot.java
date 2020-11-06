package logic;


import java.util.ArrayList;
import java.util.Random;

public abstract class Bot {
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
}
