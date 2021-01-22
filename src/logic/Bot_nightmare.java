package logic;
import java.io.Serializable;

/**
 * Dies ist ein schummelnder Bot
 */
public class Bot_nightmare extends Bot implements Serializable {
    /** Das Spielobjekt des Gegners zum schummeln */
    public Spiel cheat;

    /**
     * erstellt einen Nightmare Bot
     * @param x Spielfeldbreite
     * @param y Spielfeldhöhe
     * @param c Gegnerspielobjekt
     */
    public Bot_nightmare(int x, int y, Spiel c) {
        super(x, y);
        cheat = c;
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        return Bot.addShipsRDMly(s, this.dasSpiel, this.rdm, this.x, this.y);
    }

    /** er merkt sich wo er zuletzt hingeschossen hat um die Suche nach Schiffen zu beschleunigen */
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
