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
        return new int[0];
    }


}
