package logic;

public class Bot_lvl_2 extends Bot{


    public Bot_lvl_2(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        return addShipsRDMly(s,dasSpiel,rdm,x,y);
    }


    @Override
    public int[] getSchuss() {
        if(slayship){
            int zx=slayX;
            int zy=slayY;
            boolean horizontal=false;
            if(zx>0 && dasSpiel.getFeld()[1][zx-1][zy]==2 || zx<(x-1) && dasSpiel.getFeld()[1][zx+1][zy]==2)
                horizontal=true;
            int schritt=1;
            if(horizontal){
                while(zx+schritt<x){
                    if(dasSpiel.getFeld()[1][zx+schritt][zy]==0){
                        return new int[]{zx+schritt,zy};
                    }else if(dasSpiel.getFeld()[1][zx+schritt][zy]==2){
                        schritt++;
                        continue;
                    }else {
                        break;
                    }
                }
                schritt=-1;
                while(zx-schritt>=0){
                    if(dasSpiel.getFeld()[1][zx+schritt][zy]==0){
                        return new int[]{zx+schritt,zy};
                    }else if(dasSpiel.getFeld()[1][zx+schritt][zy]==2){
                        schritt--;
                        continue;
                    }else {
                        break;
                    }
                }
            }else {
                while(zy+schritt<y){
                    if(dasSpiel.getFeld()[1][zx][zy+schritt]==0){
                        return new int[]{zx,zy+schritt};
                    }else if(dasSpiel.getFeld()[1][zx][zy+schritt]==2){
                        schritt++;
                        continue;
                    }else {
                        break;
                    }
                }
                schritt=-1;
                while(zx-schritt>=0){
                    if(dasSpiel.getFeld()[1][zx][zy+schritt]==0){
                        return new int[]{zx,zy+schritt};
                    }else if(dasSpiel.getFeld()[1][zx][zy+schritt]==2){
                        schritt--;
                        continue;
                    }else {
                        break;
                    }
                }
            }
            if(dasSpiel.getVerbose())
                System.err.println("there should be another ship piece close!");
        }
        return rdmSchuss(dasSpiel,rdm,x,y);
    }


}
