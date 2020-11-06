package logic;


import java.util.ArrayList;
import java.util.Random;

public abstract class Bot {
    public Spiel dasSpiel;
    protected int x,y;
    protected Random rdm;


    public boolean isFinOver() {
        return dasSpiel.isOver();
    }

    public Bot(int x, int y) {
        this.x=x;
        this.y=y;
        dasSpiel=new Spiel(x,y,true);
        dasSpiel.init();
        dasSpiel.setVerbose(false);
        rdm=new Random();
    };

    public abstract boolean shipSizesToAdd(int[] s);
    public abstract int abschiesen(int x, int y);
    public abstract  int[] getSchuss();
    public Spiel getDasSpiel(){
        return dasSpiel;
    }

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

    public static int[] getShipSizes(ArrayList<Schiff>s){
        int[] sizes=new int[s.size()];
        for(int i=0;i<sizes.length;i++){
            sizes[i]=s.get(i).schifflaenge;
        }
        return sizes;
    }
}
