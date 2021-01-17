package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Schiff implements Serializable {
    private static final long serialVersionUID = 1337L;
    public int spieler;
    public int schifflaenge;
    public boolean schifflebt;
    public int xOPos;
    public int yOPos;
    public boolean horizontal;
    public boolean[] getroffen;

    /**
     * setzt das Schiff auf lebend und intialisiert das Schiff als ungetroffen anhand dessen Länge
     *
     * @param s das zu initialisierende Schiff
     */
    public static void initSchiff(Schiff s) {
        s.schifflebt = true;
        s.getroffen = new boolean[s.schifflaenge];
    }

    /**
     * gibt das übereinstimmende Schiffsobjekt aus einer Schiffsliste zurück.
     * Wenn kein passendes gefunden wird NULL!
     *
     * @param x       xOPos des zu suchenden Schiffes
     * @param y       yOPos des zu suchenden Schiffes
     * @param spieler spieler des zu suchenden Schiffes
     * @param s       die Liste der Schiffe aus dennen gesucht wird
     * @return das gefundene Schiff oder null falls kein passendes Schiff gefunden wurde!
     */
    public static Schiff getSchiffFromOrigin(int x, int y, int spieler, ArrayList<Schiff> s) {
        for (int i = 0; i < s.size(); i++) {
            Schiff sz = s.get(i);
            if (sz.spieler == spieler && sz.xOPos == x && sz.yOPos == y)
                return sz;
        }
        return null;
    }

    /**
     * setzt getroffen Array eines Schiffobjekts
     *
     * @param x X Koordinate des Treffers auf dem Spielfeld
     * @param y Y Koordinate des Treffers auf dem Spielfeld
     * @param s das getroffene Schiff
     * @return true bei erfolg, false wenn misslungen
     */
    public static boolean setGetroffenWposAship(int x, int y, Schiff s) {
        try {
            if (s.horizontal) {
                s.getroffen[x - s.xOPos] = true;
            } else {
                s.getroffen[y - s.yOPos] = true;
            }
            int countLebt = 0;
            for (int i = 0; i < s.getroffen.length; i++)
                if (s.getroffen[i] == true)
                    countLebt++;
            if (countLebt == s.getroffen.length)
                s.schifflebt = false;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Illegal POS!");
            return false;
        }
        return true;
    }

    public static void killShipfields(Schiff s, int[][][] f) {
        for (int i = 0; i < s.schifflaenge; i++) {
            if (s.horizontal) {
                f[s.spieler][s.xOPos + i][s.yOPos] = 4;
            } else {
                f[s.spieler][s.xOPos][s.yOPos + i] = 4;
            }
        }
    }
}
