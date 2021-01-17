package logic;

import java.io.Serializable;

public class Bot_nightmare extends Bot implements Serializable {
    private static final long serialVersionUID = 1337L;
    public Spiel cheat;

    public Bot_nightmare(int x, int y, Spiel c) {
        super(x, y);
        cheat = c;
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        return Bot.addShipsRDMly(s, this.dasSpiel, this.rdm, this.x, this.y);
    }

    private int lx = 0;

    @Override
    public int[] getSchuss() {
        for (int x = lx; x < this.x; x++) {
            for (int y = 0; y < this.y; y++) {
                if (cheat.getFeld()[0][x][y] == 1) {
                    lx = x;

                    return new int[]{x, y};
                }
            }
        }
        return Bot.rdmSchuss(this.dasSpiel, this.rdm, this.x, this.y);
    }
}
