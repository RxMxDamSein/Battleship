package logic;


import java.util.ArrayList;

public class Spiel {
    private int x=20,y=20;
    // 0 Wasser, 1 Schiff, 2 Treffer
    private int[][][] feld;
    private boolean init=false;
    private ArrayList<Schiff> schiffe;
    /**
     * make Spiel object
     */
    public Spiel(){
        System.out.println("NEUES SPIEL!");
    }

    /**
     * make Spiel object
     * @param x horizontal size
     * @param y vertical size
     */
    public Spiel(int x , int y){
        this();
        this.x=x;
        this.y=y;
    }

    /**
     * Initialisiert das Spielbrett!
     * @return boolean Erfolgreich oder nicht Erfolgreich
     */
    public boolean init(){
        if(x<1||y<1){
            System.err.println("Feld eingabe zu klein x: "+x+"y: "+y+" sollen mindestens 1 sein!");
            return false;
        }
        feld=new int[2][x][y];
        init=true;
        return true;
    }

    /**
     * Soll überprüfen ob man das Schiff in das Spiel einfügen darf
     * @param s das zu überprüfende Schiff
     * @return true ist in Ordnung, false illegal!
     */
    private boolean checkLegalSchiff(Schiff s){
        for(int i=0;i<s.schifflaenge+2;i++){
            for (int j=0;j<3;j++){
                if (s.horizontal){

                }
            }
        }
        return true;
    }

    /**
     * Ermöglicht ein Schiff dem Spiel hinzuzufügen
     * @param x X Ankerpunkt
     * @param y Y Ankerpunkt
     * @param horizontal true -> das Schiff geht nach rechts weg, false -> das Schiff geht nach unten weg
     * @param len die Länge des Schiffes
     * @param spieler 0 oder 1 (in der Regel) besagt den Spieler dem das Schiff nacher gehört
     * @return true erfolgreich hinzugefügt, false illegal!
     */
    public boolean addShip(int x,int y,boolean horizontal,int len,int spieler){
        Schiff s=new Schiff();
        s.xOPos=x;
        s.yOPos=y;
        s.horizontal=horizontal;
        s.schifflaenge=len;
        s.spieler=spieler;
        Schiff.initSchiff(s);
        if (checkLegalSchiff(s)){
            schiffe.add(s);
            return true;
        }else{
            System.err.println("Illegales Schiff!");
            return false;
        }
    }

    /**
     * Set specific field size later
     * Do this before init!
     * @param x horizontal size
     * @param y vertical size
     */
    public void setSize(int x,int y){
        if (!init){
            this.x=x;
            this.y=y;
        }
    }

    /**
     * @return Breite des Spielfeldes
     */
    public int getSizeX(){
        return x;
    }

    /**
     * @return gibt das Spielfeld zurück
     */
    public int[][][] getFeld() {
        return feld;
    }

    /**
     * @return Höhe des Spielfeldes
     */
    public int getSizeY(){
        return y;
    }
}
