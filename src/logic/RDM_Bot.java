package logic;

import java.util.ArrayList;

public class RDM_Bot extends  Bot{

    public static void main(String[] args) {//test
        RDM_Bot rdm_bot=new RDM_Bot(5,5);
        int[] s={1,1,1,1,1,1,1,2};
        rdm_bot.shipSizesToAdd(s);
    }

    public RDM_Bot(int x,int y){
        super(x,y);

    }
    @Override
    public boolean shipSizesToAdd(int[] s) {
        return addShipsRDMly(s,dasSpiel,rdm,x,y);
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


}
