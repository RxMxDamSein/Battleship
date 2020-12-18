package logic;


import logic.save.ResourceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Spiel implements Serializable {
    private static final long serialVersionUID=1337L;
    private int x=20,y=20;
    // 0 frei, 1 Schiff, 2 Treffer, 3 Wasser
    private int[][][] feld;
    private boolean init=false;
    private boolean started=false;
    private boolean fin=false;
    private boolean versenkt=false;
    public ArrayList<Schiff> schiffe;
    private Random random=new Random();
    private int abschussSpieler =-1;
    private boolean verbose=true;
    //if remote -> both sides are known; !remote -> only your side is known! and you are player 0(1)
    private boolean remote;
    /**
     * make Spiel object
     */
    public Spiel(){
        schiffe=new ArrayList<Schiff>();
    }

    public void setAbschussSpieler(int abschussSpieler) {
        this.abschussSpieler = abschussSpieler;
    }

    /**
     * Lädt das Spiel anhand der ID
     * @param id die Id des zu laden dem Objekts
     * @return null Failure, sonst Erfolg
     */
    public static Spiel load(String id){
        try {
            return (Spiel)ResourceManager.load(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.err.println("load error");
        return null;
    }

    public boolean getVerbose() {
        return verbose;
    }

    /**
     * Verbose soll setzen ob prints gemacht werden oder nicht!
     * @param verbose
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * make Spiel object
     * remote is set to false
     * @param x horizontal size
     * @param y vertical size
     */
    public Spiel(int x , int y){
        this();
        this.x=x;
        this.y=y;
        this.remote=false;
    }

    /**
     * make Spiel object
     * @param x horizontal size
     * @param y vertical size
     * @param remote set if both player sides are known or not
     */
    public Spiel(int x,int y,boolean remote){
        this(x,y);
        this.remote=remote;
    }

    /**
     * @return gibt zurück ob beim letzten erfolgreichen Schuss ein Schiff versunken wurde
     * true -> versenkt, false -> nicht versenkt
     */
    public boolean istVersenkt(){
        return versenkt;
    }
    /**
     * @return true Spiel ist vorbei, false noch nicht!
     */
    public boolean isOver(){
        return fin;
    }
    /**
     * Initialisiert das Spielbrett!
     * Nur 1 mal möglich!
     * @return boolean Erfolgreich oder nicht Erfolgreich
     */
    public boolean init(){
        if(init){
            if(verbose)
                System.err.println("Das Spiel wurde bereits Initialisiert erstelle lieber ein NEUES!");
            return false;
        }
        if(x<1||y<1){
            if(verbose)
                System.err.println("Feld eingabe zu klein x: "+x+"y: "+y+" sollen mindestens 1 sein!");
            return false;
        }
        feld=new int[2][x][y];
        init=true;
        return true;
    }

    /**
     * This Function just prints the winner.
     * Works only when called right after checkGameOver()==true
     */
    private void gameOver(){
        logicOUTput.printFeld(feld,true);
        System.out.println("Der Sieger der Partie ist Spieler "+((abschussSpieler==0)?"2(1)":"1(0)"));
    }
    /**
     * checks if a winner is decided
     * @return true if one player has won!
     */
    private boolean checkGameOver(){
        if(!started){
            if(verbose)
                System.err.println("Game has not started yet!");
            return false;
        }
        boolean spieler1TOT=true;
        boolean spieler2TOT=true;
        for(int i=0;i<schiffe.size();i++){
            Schiff s=schiffe.get(i);
            if(spieler1TOT && s.schifflebt==true && s.spieler==0)
                spieler1TOT=false;
            if(spieler2TOT && s.schifflebt==true && s.spieler==1)
                spieler2TOT=false;
            if(!spieler1TOT && !spieler2TOT)
                break;
        }
        if(remote){//you are player 0(1)
            //System.out.println("spieler 1 "+spieler1TOT);
            if (spieler1TOT){
                //System.out.println("Spieler 1 Ausgelöscht!");
                fin=true;
                /*To Do! tell enemy you lost!
                can also be done in GameOVer function or with fin Attribute
                 */
                return true;
            }
        }else if(spieler1TOT || spieler2TOT){
            fin=true;
            return true;
        }

        return false;
    }

    /**
     * Possibility for remote host to say he lost
     * @return true if success
     */
    public boolean setGameOver(){
        if(!remote){
            if(verbose)
                System.err.println("You can only set GameOver while playing remote!");
            return false;
        }else if(abschussSpieler==0){
            if(verbose)
                System.err.println("Only remote can set GameOver!");
            return false;
        }else if(!started){
            if(verbose)
                System.err.println("Game has not started yet!");
            return false;
        }
        fin=true;
        gameOver();
        return true;
    }
    /**
     * @return gibt den Spieler der jetzt abgeschossen wird zurück
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
            if(verbose)
                System.err.println("Das Spiel ist bereits im Gange!");
            return false;
        }
        started=true;
        if(abschussSpieler <0){
            abschussSpieler =random.nextInt(2);
            //System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
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
        //System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
        return true;
    }
    /**
     * Schießt auf ein Feld(x|y) auf dem Spielbrett des angegebenen Spielers
     * setzt zudem denn nächsten zu beschiesenden Spieler
     * Nur verwendbar wenn remote==false
     * @param x X Koordinate des Schusses
     * @param y Y Koordinate des Schusses
     * @param spieler der abzuschiesende Spieler
     * @return true -> es wurde geschossen, false -> es wurde nicht geschossen FEHLER!
     */
    public boolean shoot(int x, int y,int spieler){
        if(remote){
            if(verbose)
                System.err.println("use other shoot function while playing remote!");
            return false;
        }
        return abstractShoot(x,y,spieler,0);
    }

    /**
     * Schießt auf ein Feld(x|y) auf dem Spielbrett des angegebenen Spielers
     * setzt zudem denn nächsten zu beschiesenden Spieler
     * Nur verwendbar wenn remote==true
     * @param x X Koordinate des Schusses
     * @param y Y Koordinate des Schusses
     * @param spieler der abzuschiesende Spieler
     * @param p_hit Value des getroffenen Feldes auf dem Remotefeld 0 frei 1 Schiff 2 getroffen 3 Wasser| wenn man auf Spieler 0 schießt wird diese Eingabe ignoriert!
     * @param p_versenkt true wenn bei Remote ein Schiff versenkt wurde, false wenn bei Remote kein Schiff versenkt wurde
     * @return true -> es wurde geschossen, false -> es wurde nicht geschossen FEHLER!
     */
    public boolean shoot(int x, int y,int spieler,int p_hit,boolean p_versenkt){
        if(!remote){
            if(verbose)
                System.err.println("use other shoot function while playing local!");
            return false;
        }
        boolean ret=abstractShoot(x,y,spieler,p_hit);
        if(ret && spieler==1)
            versenkt=p_versenkt;
        return  ret;
    }

    /**
     * Hilfsfunktion für die ShootFunktionen
     * @param x
     * @param y
     * @param spieler
     * @param p_hit
     * @return
     */
    private boolean abstractShoot(int x, int y,int spieler,int p_hit){
        if(!started){
            if(verbose)
                System.err.println("Game has not started yet!");
            return false;
        }
        if(fin){
            if(verbose)
                System.err.println("Game is Over!");
            return false;
        }
        if(spieler!=abschussSpieler && p_hit!=31){
            if(verbose)
                System.err.println("Wrong Player to shoot!");
            return false;
        }

        if(x>=this.x || y >=this.y || x<0 || y<0){
            if(verbose)
                System.err.println("Shoot out of boundaries!");
            return false;
        }
        if(remote && spieler==1 && feld[spieler][x][y]==0)
            feld[spieler][x][y]=p_hit;
        switch (feld[spieler][x][y]){
            default:
                if(verbose)
                    System.err.println("undefined feld state");
                return false;
            case 2: case 3:
                if(verbose)
                    System.err.println("Selected Field was already shot s "+spieler+" xy "+x+" "+y);
                return false;
            case 1://Treffer
                feld[spieler][x][y]=2;
                //find ship and update its getroffen Attribute
                if(!remote || (remote&&spieler==0)){
                    versenkt=false;
                    Schiff s=findSchiffFromPos(x,y,spieler);
                    if(!Schiff.setGetroffenWposAship(x,y,s)){
                        if(verbose)
                            System.err.println("Es wurde ein Treffer erzielt, aber es sollte dort kein Schiff sein!");
                    }
                    if(!s.schifflebt){//Schiff ist versenkt!
                        versenkt=true;
                    }
                }
                break;
            case 0://Wasser oder ehr noch frei
                versenkt=false;
                feld[spieler][x][y]=3;
                if(spieler>0){
                    abschussSpieler=0;
                    //System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
                } else{
                    abschussSpieler=1;
                    //System.out.println("abzuschiesender Spieler ist "+abschussSpieler);
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
     * ermöglicht ein Schiff Objekt zu erhalten anhand einer Postion und dem Spieler
     * @param x X Koordinate eines Teiles des Schiffes
     * @param y Y Koordinate eines Teiles des Schiffes
     * @param spieler dem das Schiffzugehöriger Spieler
     * @return das Schiffobjekt wenn erfolgreich ansonsten null
     */
    private Schiff findSchiffFromPos(int x, int y,int spieler){
        int horizontal=2;  //-1 undefined, 0 horizontal, 1 vertical, 2 shipsize=1(both)
        if((x>0 && feld[spieler][x-1][y]==1 )||(x>0 && feld[spieler][x-1][y]==2 )|| (x<this.x-1 && feld[spieler][x+1][y]==1 )|| (x<this.x-1 && feld[spieler][x+1][y]==2 )){
            horizontal=0;
        }else if((y>0 && feld[spieler][x][y-1]==1 )||(y>0 && feld[spieler][x][y-1]==2 )|| (y<this.y-1 && feld[spieler][x][y+1]==1 )|| (y<this.y-1 && feld[spieler][x][y+1]==2 )){
            horizontal=1;
        }
        Schiff s=null;
        switch (horizontal){
            default://both(2) shipsize should be 1
                s=Schiff.getSchiffFromOrigin(x,y,spieler,schiffe);
                break;
            case 0://horizontal
                int xz=x;
                while (xz>=0 && (feld[spieler][xz][y]==1 || feld[spieler][xz][y]==2)){
                    xz--;
                }
                xz++;//xOPos des Schiffes
                s=Schiff.getSchiffFromOrigin(xz,y,spieler,schiffe);
                break;
            case 1://vertical
                int yz=y;
                while (yz>=0 && (feld[spieler][x][yz]==1 || feld[spieler][x][yz]==2)){
                    yz--;
                }
                yz++;//yOPos des Schiffes
                s=Schiff.getSchiffFromOrigin(x,yz,spieler,schiffe);
                break;
        }
        if(s==null){
            if(verbose)
                System.err.println("Ship was not found error!");
        }

        return s;
    }

    /**
     * fügt ein Schiff dem Feld hinzu!
     */
    public void feldAddSchiff(Schiff s){
        for(int i=0;i<s.schifflaenge;i++){
            if(s.horizontal){
                feld[s.spieler][s.xOPos+i][s.yOPos]=(s.getroffen[i]==false)?1:2;
            }else{
                feld[s.spieler][s.xOPos][s.yOPos+i]=(s.getroffen[i]==false)?1:2;
            }
        }
    }

    public void feldDELSchiff(Schiff s){
        for(int i=0;i<s.schifflaenge;i++){
            if(s.horizontal){
                feld[s.spieler][s.xOPos+i][s.yOPos]=0;
            }else{
                feld[s.spieler][s.xOPos][s.yOPos+i]=0;
            }
        }
    }
    /**
     * Soll überprüfen ob man das Schiff in das Spiel einfügen darf
     * @param s das zu überprüfende Schiff
     * @return true ist in Ordnung, false illegal!
     */
    public boolean checkLegalSchiff(Schiff s){
        if(s.xOPos<0 || s.xOPos>=x || s.yOPos<0 || s.yOPos>=y || (s.horizontal && s.xOPos+s.schifflaenge>x) || (!s.horizontal && s.yOPos+s.schifflaenge>y))
            return false;
        /*if(s.horizontal){
            for(int i=s.xOPos-1;i<=s.xOPos+s.schifflaenge;i++){
                for(int j=s.yOPos-1;j)
            }
        }*/
        int z=(s.horizontal)?s.xOPos:s.yOPos;
        for(int i=(z-1<0)?0:z-1;i<z+s.schifflaenge+1;i++){
            if(s.horizontal){
                if(i>= this.x)
                    continue;
                for(int y=s.yOPos-1;y<=s.yOPos+1;y++){
                    if(y<0 || y>=this.y)
                        continue;
                    if(feld[s.spieler][i][y]!=0)
                        return false;
                }
            }else {
                if(i>= this.y)
                    continue;
                for(int x=s.xOPos-1;x<=s.xOPos+1;x++){
                    if(x<0 || x>=this.x)
                        continue;
                    if(feld[s.spieler][x][i]!=0)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Ermöglicht ein Schiff dem Spiel hinzuzufügen.
     * Nach Spielbeginn ist das nicht mehr erlaubt!
     * @param x X Ankerpunkt (Startpunkt)
     * @param y Y Ankerpunkt (Startpunkt)
     * @param horizontal true -> das Schiff geht nach rechts weg, false -> das Schiff geht nach unten weg
     * @param len die Länge des Schiffes
     * @param spieler 0 oder 1 (in der Regel) besagt den Spieler dem das Schiff nacher gehört
     * @return true erfolgreich hinzugefügt, false illegal!
     */
    public boolean addShip(int x,int y,boolean horizontal,int len,int spieler){
        if(started){
            if(verbose)
                System.err.println("Game has already started!");
            return false;
        }
        if(remote && spieler==1){
            if(verbose)
                System.err.println("You can not set Enemy ships (Remote)");
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
            if(verbose)
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
     * If false you can add ships if true you shall shoot
     * @return true -> game started false -> game has not started
     */
    public boolean isStarted() {
        return started;
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

    /**
     * speichert das Spiel ab um später wieder geladen zu werden
     * @return true erfolg, false failure
     */
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
}
