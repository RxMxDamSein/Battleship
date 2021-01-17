package logic.save;

import GUI.Client;
import logic.Bot;
import logic.Spiel;

import java.io.IOException;
import java.io.Serializable;

public class SAFE_SOME implements Serializable {
    private static final long serialVersionUID = 1337L;
    public Bot[] bots;
    public Spiel[] spiele;
    public Object[] objects;
    public int game;
    public String id;

    public SAFE_SOME(Bot[] b, Spiel[] s, int g, String id, String DateiName) {
        this.id = id;
        STD_SAVE(b, s, g, DateiName);
    }


    /*
    switch game
    1 -> 2SP
    2 -> 1SP gegen CPU
    3 -> BVB
    4 -> PlayerHost
    5 -> PlayerBotHost
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
     * Damit kann man doch dann gefühlt alles speichern :D
     *
     * @param b  so viele Bots du willst
     * @param s  so viele Spiele du willst
     * @param g  ein int wert um zu wissen was für ein Spiel typ
     * @param id Dateiname sozusagen
     */
    public SAFE_SOME(Bot[] b, Spiel[] s, int g, String id) {
        STD_SAVE(b, s, g, id);
    }

    /**
     * @param id Dateiname sozusagen
     * @return null on failure, sonst success
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
