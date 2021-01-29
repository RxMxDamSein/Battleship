package logic.save;

import logic.Bot;
import logic.Spiel;
import java.io.IOException;
import java.io.Serializable;

/**
 * Eine allgemeine Speicherklasse
 */
public class SAFE_SOME implements Serializable {
    /** Serialization Nummer */
    private static final long serialVersionUID = 1337L;
    /** ein mögliches Array an Bots */
    public Bot[] bots;
    /** ein mögliches Array an Spielen */
    public Spiel[] spiele;
    /** ein mögliches Array an Objekten */
    public Object[] objects;
    /** gibt den Spieltyp an. 1-2SP, 2-Spieler VS Bot, 3-Bot VS Bot, 4-SpielerOnline, 5-BotOnline*/
    public int game;
    /** Speicher ID */
    public String id;
    /** Falls der Bot zuletzt ein Schiff gefunden hat */
    public Boolean Slayship;
    /** eine X Stelle des gefundenen Schiffes */
    public int slayX;
    /** eine Y Stelle des gefundenen Schiffes*/
    public int slayY;
    /** Ein Array in dem sich die Schiffsgrößen der noch nicht versenkten Gegnerschiffe gemerkt werden */
    public Integer[] enemyShips;
    /** Merkt sich wie lang das längste übrige Gegnerschiff ist */
    public Integer longestShip;
    /** Merkt sich wie lang das kürzeste übrige Gegnerschiff ist */
    public Integer smallestShip;

    /**
     * Speichert ein SAFE_SOME Objekt
     * @param b Bots
     * @param s Spiele
     * @param g Spieltyp
     * @param id ID
     * @param DateiName Dateiname
     */
    public SAFE_SOME(Bot[] b, Spiel[] s, int g, String id, String DateiName) {
        this.id = id;
        STD_SAVE(b, s, g, DateiName);
    }

    /**
     * Speichert ein SAFE_SOME Objekt
     * @param spiels Spiele
     * @param i Spieltyp
     * @param s Dateiname/ID
     * @param slayship slayship boolean, ob gerade ein Schiff gefunden worden ist.
     * @param slayX X Position des gefundenen Schiffs
     * @param slayY Y Position des gefundenen Schiffs
     * @param enemyShips Array mit den verbleibenden Gegnerschiffen
     * @param smallestShip kleinstes übriges Gegnerschiff
     * @param longestShip größtes übriges Gegnerschiff
     */
    public SAFE_SOME(Spiel[] spiels, int i, String s, boolean slayship, int slayX, int slayY, Integer[] enemyShips, Integer smallestShip, Integer longestShip) {
        this.Slayship=slayship;
        this.slayX=slayX;
        this.slayY=slayY;
        this.enemyShips=enemyShips;
        this.smallestShip=smallestShip;
        this.longestShip=longestShip;
        STD_SAVE(null,spiels,i,s);
    }

    /**
     * Speichert ein SAFE_SOME Objekt
     * @param spiels Spiele
     * @param i Spieltyp
     * @param hash ID
     * @param name Dateiname
     * @param slayship slayship boolean, ob gerade ein Schiff gefunden worden ist.
     * @param slayX X Position des gefundenen Schiffs
     * @param slayY Y Position des gefundenen Schiffs
     * @param enemyShips Array mit den verbleibenden Gegnerschiffen
     * @param smallestShip kleinstes übriges Gegnerschiff
     * @param longestShip größtes übriges Gegnerschiff
     */
    public SAFE_SOME(Spiel[] spiels, int i, String hash, String name, boolean slayship, int slayX, int slayY, Integer[] enemyShips, Integer smallestShip, Integer longestShip) {
        this.Slayship=slayship;
        this.slayX=slayX;
        this.slayY=slayY;
        this.id=hash;
        this.enemyShips=enemyShips;
        this.smallestShip=smallestShip;
        this.longestShip=longestShip;
        STD_SAVE(null,spiels,i,name);
    }


    /**
     * Speichert ein SAFE_SOME Objekt
     * @param b Bots
     * @param s Spiele
     * @param g Spieltyp
     * @param id ID
     */
    private void STD_SAVE(Bot[] b, Spiel[] s, int g, String id) {
        bots = b;
        spiele = s;
        game = g;
        try {
            ResourceManager.save(this, id);
        } catch (IOException e) {
            System.err.println("SAVE ERROR!");
            e.printStackTrace();
        }
    }

    /**
     * Speichert ein SAFE_SOME Objekt
     * @param b  so viele Bots du willst
     * @param s  so viele Spiele du willst
     * @param g  ein int wert um den Spieltyp zu kennen
     * @param id ID
     */
    public SAFE_SOME(Bot[] b, Spiel[] s, int g, String id) {
        STD_SAVE(b, s, g, id);
    }

    /**
     * @param id Dateiname sozusagen
     * @return SAFE_SOME-Objekt bei Erfolg, sonst null
     */
    public static SAFE_SOME load(String id) {
        try {
            return (SAFE_SOME) ResourceManager.load(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
