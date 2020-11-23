package logic;

public class Bot_lvl_2 extends Bot{


    public Bot_lvl_2(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        return false;
    }

    @Override
    public int abschiesen(int x, int y) {
        return 0;
    }

    @Override
    public int[] getSchuss() {
        return new int[0];
    }

    @Override
    public void setSchussFeld(int x, int y, int wert, boolean versenkt) {

    }
}
