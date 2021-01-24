package logic;

/**
 * Ein Bot als Spieler der einfach zufällig irgendwo hinschießt.
 */
public class RDM_Bot extends Bot {
    /**
     * erstellt ein RDM_Bot
     * @param x Spielfeldbreite
     * @param y Spielfeldhöhe
     */
    public RDM_Bot(int x, int y) {
        super(x, y);

    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        initEnemyships(s);
        return addShipsRDMly(s, dasSpiel, rdm, x, y);
    }


    @Override
    public int[] getSchuss() {
        return rdmSchuss(dasSpiel, rdm, x, y);
    }


}
