package logic;


import java.util.ArrayList;
import java.util.Random;

public class Spiel {
    private int x=20,y=20;
    // 0 frei, 1 Schiff, 2 Treffer, 3 Wasser
    private int[][][] feld;
    private boolean init=false;
    private boolean started=false;
    private ArrayList<Schiff> schiffe;
    private Random random=new Random();
    private int abschussSpieler =-1;
    /**
     * make Spiel object
     */
    public Spiel(){
        schiffe=new ArrayList<Schiff>();
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
     * Nur 1 mal möglich!
     * @return boolean Erfolgreich oder nicht Erfolgreich
     */
    public boolean init(){
        if(init){
            System.err.println("Das Spiel wurde bereits Initialisiert erstelle lieber ein NEUES!");
            return false;
        }
        if(x<1||y<1){
            System.err.println("Feld eingabe zu klein x: "+x+"y: "+y+" sollen mindestens 1 sein!");
            return false;
        }
        feld=new int[2][x][y];
        init=true;
        return true;
    }


    private void gameOver(){
        logicOUTput.printFeld(feld,true);
        System.out.println("Der Sieger der Partie ist: "+((abschussSpieler==0)?"1":"0"));
    }
    /**
     * checks if a winner is decided
     * @return true if one player has one!
     */
    private boolean checkGameOver(){
        return false;
    }
    /**
     * @return gibt den Spieler der jetzt schießen darf zurück
     */
    public int getAbschussSpieler(){
        return abschussSpieler;
    }

    /**
     * setzt das Spiel in spiel Zustand.
     * Das heißt ab jetzt kann geschossen werden.
     * Dafür können keine Schiffe mehr hinzugefügt werden!
     * @return true -> das Spiel wurde gestartet, false -> FEHLER!
     */
    public boolean starteSpiel(){
        if(started){
            System.err.println("Das Spiel ist bereits im Gange!");
            return false;
        }
        started=true;
        if(abschussSpieler <0){
            abschussSpieler =random.nextInt(2);
            System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
        }
        if(checkGameOver()){
            gameOver();
        }
        return true;
    }

    /**
     * setzt das Spiel in spiel Zustand.
     * Das heißt ab jetzt kann geschossen werden.
     * Dafür können keine Schiffe mehr hinzugefügt werden!
     * @param spieler setzt den Spieler der als erstes angeschossen wird 0 oder 1
     * @return true -> das Spiel wurde gestartet, false -> FEHLER!
     */
    public boolean starteSpiel(int spieler){
        if (!starteSpiel())
            return false;
        abschussSpieler =spieler;
        System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
        return true;
    }
    /**
     * Schießt auf ein Feld(x|y) auf dem Spielbrett des angegebenen Spielers
     * setzt zudem denn nächsten zu beschiesenden Spieler
     * @param x X Koordinate des Schusses
     * @param y Y Koordinate des Schusses
     * @param spieler der abzuschiesende Spieler
     * @return true -> es wurde geschossen, false -> es wurde nicht geschossen FEHLER!
     */
    public boolean shoot(int x, int y,int spieler){
        if(!started){
            System.err.println("Game has not started yet!");
            return false;
        }
        if(spieler!=abschussSpieler){
            System.err.println("Wrong Player to shoot!");
            return false;
        }
        if(x>=this.x || y >=this.y || x<0 || y<0){
            System.err.println("Shoot out of boundaries!");
            return false;
        }
        switch (feld[spieler][x][y]){
            default:
                System.err.println("undefined feld state");
                return false;
            case 2: case 3:
                System.err.println("Selected Field was already shot");
                return false;
            case 1://Treffer
                feld[spieler][x][y]=2;
                break;
            case 0://Wasser
                feld[spieler][x][y]=3;
                if(spieler>0){
                    abschussSpieler=0;
                    System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
                } else{
                    abschussSpieler=1;
                    System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
                }
                break;
        }
        //System.out.println("Player "+spieler+"/"+abschussSpieler+" was shot at ("+x+"|"+y+")");
        if(checkGameOver()){
            gameOver();
        }
        return true;
    }

    /**
     * fügt ein Schiff dem Feld hinzu!
     */
    private void feldAddSchiff(Schiff s){
        for(int i=0;i<s.schifflaenge;i++){
            if(s.horizontal){
                feld[s.spieler][s.xOPos+i][s.yOPos]=(s.getroffen[i]==false)?1:2;
            }else{
                feld[s.spieler][s.xOPos][s.yOPos+i]=(s.getroffen[i]==false)?1:2;
            }
        }
    }
    /**
     * Soll überprüfen ob man das Schiff in das Spiel einfügen darf
     * @param s das zu überprüfende Schiff
     * @return true ist in Ordnung, false illegal!
     */
    private boolean checkLegalSchiff(Schiff s){
        if(s.xOPos<0 || s.xOPos>=x || s.yOPos<0 || s.yOPos>=y || (s.horizontal && s.xOPos+s.schifflaenge>x) || (!s.horizontal && s.yOPos+s.schifflaenge>y))
            return false;
        for(int i=0;i<s.schifflaenge+2;i++){
            for (int j=0;j<3;j++){
                if (s.horizontal){
                    if(s.xOPos-1+j>=0 && s.xOPos-1+j<x){
                        if(feld[s.spieler][s.xOPos-1+j][s.yOPos]!=0)
                            return false;
                    }
                }else{
                    if(s.yOPos-1+j>=0 && s.yOPos-1+j<y){
                        if(feld[s.spieler][s.xOPos][s.yOPos-1+j]!=0)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Ermöglicht ein Schiff dem Spiel hinzuzufügen.
     * Nach Spielbeginn ist das nicht mehr erlaubt!
     * @param x X Ankerpunkt
     * @param y Y Ankerpunkt
     * @param horizontal true -> das Schiff geht nach rechts weg, false -> das Schiff geht nach unten weg
     * @param len die Länge des Schiffes
     * @param spieler 0 oder 1 (in der Regel) besagt den Spieler dem das Schiff nacher gehört
     * @return true erfolgreich hinzugefügt, false illegal!
     */
    public boolean addShip(int x,int y,boolean horizontal,int len,int spieler){
        if(started){
            System.err.println("Game has already started!");
            return false;
        }
        Schiff s=new Schiff();
        s.xOPos=x;
        s.yOPos=y;
        s.horizontal=horizontal;
        s.schifflaenge=len;
        s.spieler=spieler;
        Schiff.initSchiff(s);
        if (checkLegalSchiff(s)){
            schiffe.add(s);
            feldAddSchiff(s);
            return true;
        }else{
            System.err.println("Illegales Schiff!");
            return false;
        }
    }

    /**
     * Set specific field size later
     * Do this before init! (also noch viel früher als Spielbeginn)
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
