package logic;

import java.util.ArrayList;

public class RDM_Bot extends  Bot{

    public static void main(String[] args) {
        RDM_Bot rdm_bot=new RDM_Bot(5,5);
        int[] s={1,1,1,1,1,1,1,2};
        rdm_bot.shipSizesToAdd(s);
    }

    public RDM_Bot(int x,int y){
        super(x,y);

    }
    @Override
    public boolean shipSizesToAdd(int[] s) {
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
                    zx=rdm.nextInt(this.x);
                    zy=rdm.nextInt(this.y);
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
        return added;
    }

    @Override
    public int abschiesen(int x, int y) {
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

    @Override
    public int[] getSchuss() {
        int zx=0,zy=0,count=0;
        do{
            count++;
            zx=rdm.nextInt(x);
            zy=rdm.nextInt(y);
        }while (dasSpiel.getFeld()[1][zx][zy]!=0 /*&& count<x*y*2*/);
        System.out.println(dasSpiel.getFeld()[1][zx][zy]+" getSchuss "+zx+" "+zy);
        return new int[]{zx, zy};
    }

    public void setSchussFeld(int x,int y,int wert,boolean versenkt){
        int[][][] f=dasSpiel.getFeld();
        f[1][x][y]=wert;
        if(versenkt){//make water around ship
            int zx=x,zy=y;
            boolean horizontal=false;
            if(zx>0 && f[1][zx-1][zy]==2 || zx<(x-1) && f[1][zx+1][zy]==2)
                horizontal=true;
            int schritt;
            if(horizontal){
                schritt=0;
                while( x+schritt>=0 && x+schritt<this.x &&f[1][x+schritt][y]==2){
                    if(y+1<this.y)
                        f[1][x+schritt][y+1]=3;
                    if(y-1>=0)
                        f[1][x+schritt][y-1]=3;
                    schritt++;
                }
                if(x+schritt<this.x){
                    f[1][x+schritt][y]=3;
                    if(y+1<this.y)
                        f[1][x+schritt][y+1]=3;
                    if(y-1>=0)
                        f[1][x+schritt][y-1]=3;
                }

                schritt=-1;
                while( x+schritt>=0 && x+schritt<this.x &&f[1][x+schritt][y]==2){
                    if(y+1<this.y)
                        f[1][x+schritt][y+1]=3;
                    if(y-1>=0)
                        f[1][x+schritt][y-1]=3;
                    schritt--;
                }
                if(x+schritt>=0){
                    f[1][x+schritt][y]=3;
                    if(y+1<this.y)
                        f[1][x+schritt][y+1]=3;
                    if(y-1>=0)
                        f[1][x+schritt][y-1]=3;
                }
            }else{
                schritt=0;
                while( y+schritt>=0 && y+schritt<this.y &&f[1][x][y+schritt]==2){
                    if(x+1<this.x)
                        f[1][x+1][y+schritt]=3;
                    if(x-1>=0)
                        f[1][x-1][y+schritt]=3;
                    schritt++;
                }
                if(y+schritt<this.y){
                    f[1][x][y+schritt]=3;
                    if(x+1<this.x)
                        f[1][x+1][y+schritt]=3;
                    if(x-1>=0)
                        f[1][x-1][y+schritt]=3;
                }
                schritt=-1;
                while( y+schritt>=0 && y+schritt<this.y &&f[1][x][y+schritt]==2){
                    if(x+1<this.x)
                        f[1][x+1][y+schritt]=3;
                    if(x-1>=0)
                        f[1][x-1][y+schritt]=3;
                    schritt--;
                }
                if(y+schritt>=0){
                    f[1][x][y+schritt]=3;
                    if(x+1<this.x)
                        f[1][x+1][y+schritt]=3;
                    if(x-1>=0)
                        f[1][x-1][y+schritt]=3;
                }
            }
            System.out.println("after versenkt!");
            logicOUTput.printFeld(f,true);
        }
    }
}
