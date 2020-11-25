package logic;


import logic.save.ResourceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public abstract class Bot implements Serializable {
    private static final long serialVersionUID=1337L;
    public Spiel dasSpiel;
    protected int x,y;
    protected Random rdm;
    protected boolean slayship=false; //true if you hit ship and not sunk
    protected int slayX,slayY;

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
        //dasSpiel.setVerbose(false);
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
    public int abschiesen(int x, int y){
        int p_hit=dasSpiel.getFeld()[0][x][y];
        boolean succ=dasSpiel.shoot(x,y,0,31,false);
        if(!succ)
            return -1;
        else if(dasSpiel.istVersenkt()) {
            //System.out.println("Der Bot hat verloren!");
            //fin=dasSpiel.isOver();
            return 4;
        }
        else
            return p_hit;
    }

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
    public static boolean resetFeld(int[][][] f,int spieler){
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
    public void setSchussFeld(int x,int y,int wert,boolean versenkt){
        int[][][] f=dasSpiel.getFeld();
        f[1][x][y]=wert;
        if(versenkt){//make water around ship
            slayship=false;
            Bot.waterAround(x,y,f,this.x,this.y);
            System.out.println("after versenkt!");
            logicOUTput.printFeld(f,true);
        }else {
            if(wert==2){
                slayship=true;
                slayX=x;
                slayY=y;
            }
        }
    }

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

    public static boolean addShipsRDMly(int[] s,Spiel dasSpiel,Random rdm,int width,int height){
        boolean v=dasSpiel.getVerbose();
        dasSpiel.setVerbose(false);
        boolean added=false;
        int count=0;
        while (!added && count<300){
            count++;
            dasSpiel.schiffe=new ArrayList<Schiff>();
            //added=true;
            resetFeld(dasSpiel.getFeld(),0);
            for(int i=0;i<s.length;i++){
                int zx=0,zy=0;
                int count2=0;
                do{
                    count2++;
                    zx=rdm.nextInt(width);
                    zy=rdm.nextInt(height);
                    added=dasSpiel.addShip(zx,zy, rdm.nextInt(2) == 0,s[i],0);
                }while (added==false && count2<60);
                if(!added)
                    break;
            }
        }
        logicOUTput.printFeld(dasSpiel.getFeld(),true);
        System.out.println(added);
        if(added)
            dasSpiel.starteSpiel();
        dasSpiel.setVerbose(v);
        return added;
    }
    protected static int[] rdmSchuss(Spiel dasSpiel, Random rdm,int width,int height){
        int zx=0,zy=0,count=0;
        do{
            count++;
            zx=rdm.nextInt(width);
            zy=rdm.nextInt(height);
        }while (dasSpiel.getFeld()[1][zx][zy]!=0 /*&& count<x*y*2*/);
        System.out.println(dasSpiel.getFeld()[1][zx][zy]+" getSchuss "+zx+" "+zy);
        return new int[]{zx, zy};
    }

    public static void waterAround(int x, int y, int[][][] f,int width,int height){
        boolean up,down,left,right;
        up=down=left=right=false;
        if(y+1<height && f[1][x][y+1]==2){
            down=true;
        }
        if(y-1>=0 && f[1][x][y-1]==2){
            up=true;
        }
        if(x+1<width && f[1][x+1][y]==2){
            right=true;
        }
        if(x-1>=0 && f[1][x-1][y]==2){
            left=true;
        }
        int i;
        if(up){
            for(i=0;y-i>=0&&f[1][x][y-i]==2;i++){
                if(x+1<width)
                    f[1][x+1][y-i]=3;
                if(x-1>=0)
                    f[1][x-1][y-i]=3;
            }
            if(y-i>=0){
                if(x+1<width)
                    f[1][x+1][y-i]=3;
                if(x-1>=0)
                    f[1][x-1][y-i]=3;
                f[1][x][y-i]=3;
            }
            if(!down){
                if(y+1<height){
                    if(x+1<width)
                        f[1][x+1][y+1]=3;
                    if(x-1>=0)
                        f[1][x-1][y+1]=3;
                    f[1][x][y+1]=3;
                }
            }
        }
        if(down){
            for(i=0;y+i<height&&f[1][x][y+i]==2;i++){
                if(x+1<width)
                    f[1][x+1][y+i]=3;
                if(x-1>=0)
                    f[1][x-1][y+i]=3;
            }
            if(y+i<height){
                if(x+1<width)
                    f[1][x+1][y+i]=3;
                if(x-1>=0)
                    f[1][x-1][y+i]=3;
                f[1][x][y+i]=3;
            }
            if(!up){
                if(y-1>=0){
                    if(x+1<width)
                        f[1][x+1][y-1]=3;
                    if(x-1>=0)
                        f[1][x-1][y-1]=3;
                    f[1][x][y-1]=3;
                }
            }
        }
        if(left){
            for(i=0;x-i>=0&&f[1][x-i][y]==2;i++){
                if(y+1<height)
                    f[1][x-i][y+1]=3;
                if(y-1>=0)
                    f[1][x-i][y-1]=3;
            }
            if(x-i>=0){
                if(y+1<height)
                    f[1][x-i][y+1]=3;
                if(y-1>=0)
                    f[1][x-i][y-1]=3;
                f[1][x-i][y]=3;
            }
            if(!right){
                if(x+1<width){
                    if(y+1<height)
                        f[1][x+1][y+1]=3;
                    if(y-1>=0)
                        f[1][x+1][y-1]=3;
                    f[1][x+1][y]=3;
                }
            }
        }
        if(right){
            for(i=0;x+i<width&&f[1][x+i][y]==2;i++){
                if(y+1<height)
                    f[1][x+i][y+1]=3;
                if(y-1>=0)
                    f[1][x+i][y-1]=3;
            }
            if(x+i<width){
                if(y+1<height)
                    f[1][x+i][y+1]=3;
                if(y-1>=0)
                    f[1][x+i][y-1]=3;
                f[1][x+i][y]=3;
            }
            if(!left){
                if(x-1>=0){
                    if(y+1<height)
                        f[1][x-1][y+1]=3;
                    if(y-1>=0)
                        f[1][x-1][y-1]=3;
                    f[1][x-1][y]=3;
                }
            }
        }
        if(!up && !down && !right && !left){
            if(x>0 && y>0)
                f[1][x-1][y-1]=3;
            if( y>0)
                f[1][x][y-1]=3;
            if(x+1<width && y>0)
                f[1][x+1][y-1]=3;
            if(x>0)
                f[1][x-1][y]=3;
            if(x+1<width )
                f[1][x+1][y]=3;
            if(x>0 && y+1<height)
                f[1][x-1][y+1]=3;
            if( y+1<height)
                f[1][x][y+1]=3;
            if(x+1<width && y+1<height)
                f[1][x+1][y+1]=3;
        }
    }
}


