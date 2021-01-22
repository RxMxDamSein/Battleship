package logic;

/**
 * Die mittlere Bot Schwierigkeitsstufe
 */
public class Bot_lvl_2 extends Bot {

    /**
     * kreiert das Botobjekt
     * @param x Spielbrettbreite
     * @param y Spielbretthöhe
     */
    public Bot_lvl_2(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        return addShipsRDMly(s, dasSpiel, rdm, x, y);
    }


    @Override
    public int[] getSchuss() {
        if (slayship)
            return getSlayShoot();
        else
            return rdmSchuss(dasSpiel, rdm, x, y);
    }


}
