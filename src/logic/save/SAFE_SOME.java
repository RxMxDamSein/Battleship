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
     * @return SAFRE_SOME-Objekt bei Erfolg, sonst null
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
