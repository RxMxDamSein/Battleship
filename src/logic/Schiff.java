package logic;

public class Schiff {
    public int spieler;
    public int schifflaenge;
    public boolean schifflebt;
    public int xOPos;
    public int yOPos;
    public boolean horizontal;
    public boolean[] getroffen;

    /**
     * setzt das Schiff auf lebend und intialisiert das Schiff als ungetroffen anhand dessen Länge
     * @param s das zu initialisierende Schiff
     */
    public static void initSchiff(Schiff s){
        s.schifflebt=true;
        s.getroffen=new boolean[s.schifflaenge];
    }
}
