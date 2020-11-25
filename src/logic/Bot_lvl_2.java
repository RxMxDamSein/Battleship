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
            System.out.println("SLAYSHIP!");
            int zx=slayX;
            int zy=slayY;
            boolean horizontal=false;
            boolean vertical=false;
            if(zx>0 && dasSpiel.getFeld()[1][zx-1][zy]==2 || zx<(x-1) && dasSpiel.getFeld()[1][zx+1][zy]==2)
                horizontal=true;
            if(zy>0 && dasSpiel.getFeld()[1][zx][zy-1]==2 || zy<(y-1) && dasSpiel.getFeld()[1][zx][zy+1]==2)
                vertical=true;
            int schritt=1;
            if(horizontal ){
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
                while(zx+schritt>=0){
                    if(dasSpiel.getFeld()[1][zx+schritt][zy]==0){
                        return new int[]{zx+schritt,zy};
                    }else if(dasSpiel.getFeld()[1][zx+schritt][zy]==2){
                        schritt--;
                        continue;
                    }else {
                        break;
                    }
                }
            }else if (vertical){
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
                while(zy+schritt>=0){
                    //System.out.println(zx+" "+zy);
                    if(dasSpiel.getFeld()[1][zx][zy+schritt]==0){
                        return new int[]{zx,zy+schritt};
                    }else if(dasSpiel.getFeld()[1][zx][zy+schritt]==2){
                        schritt--;
                        continue;
                    }else {
                        break;
                    }
                }
            }else {//check a postion up down left right rdmly
                while(true) {
                    switch (rdm.nextInt(4)) {
                        case 0://up
                            if (zy - 1 >= 0 && dasSpiel.getFeld()[1][zx][zy - 1] == 0)
                                return new int[]{zx, zy - 1};
                            break;
                        case 1://down
                            if (zy + 1 < y && dasSpiel.getFeld()[1][zx][zy + 1] == 0)
                                return new int[]{zx, zy + 1};
                            break;
                        case 2://left
                            if (zx - 1 >= 0 && dasSpiel.getFeld()[1][zx - 1][zy] == 0)
                                return new int[]{zx - 1, zy};
                            break;
                        case 3://right
                            if (zx + 1 < x && dasSpiel.getFeld()[1][zx + 1][zy] == 0)
                                return new int[]{zx + 1, zy};
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
