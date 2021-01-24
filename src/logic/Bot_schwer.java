package logic;
import java.util.Arrays;

/**
 * Dies ist die schwierige Bot Schwierigkeitsstufe
 */
public class Bot_schwer extends Bot {


    /**
     * erstellt einen schweren Bot
     * @param x Spielbreite
     * @param y Spielhöhe
     */
    public Bot_schwer(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        initEnemyships(s);
        //System.out.println("longest "+longestShip+" smallest "+smallestShip);
        return addShipsRDMly(s, dasSpiel, rdm, x, y);
    }


    @Override
    public void setSchussFeld(int x, int y, int wert, boolean versenkt) {
        super.setSchussFeld(x, y, wert, versenkt);
        if (versenkt) {
            int len = shipLenDestroyed(x, y, 1);
            boolean erfolg = false;
            for (int i = 0; i < enemyShips.length; i++) {
                if (enemyShips[i] == len) {
                    erfolg = true;
                    enemyShips[i] = -1;
                    if(len==longestShip){
                        longestShip = -1;
                        for (int j = 0; j < enemyShips.length; j++) {
                            if (enemyShips[j] > 0 && longestShip < enemyShips[j]) {    //noch eine richtige Größe
                                //System.err.println("NEW longeShip "+enemyShips[j]);;
                                longestShip = enemyShips[j];
                            }
                        }
                    }
                    if(len==smallestShip && longestShip>0){
                        //System.out.println("new small!");
                        smallestShip=longestShip+1;
                        for (int j = 0; j < enemyShips.length; j++) {
                            if (enemyShips[j] > 0 && smallestShip > enemyShips[j]) {    //noch eine richtige Größe
                                //System.err.println("NEW smallShip "+enemyShips[j]);;
                                smallestShip = enemyShips[j];
                            }
                        }
                    }


                    break;
                }

            }
            if (!erfolg) {
                System.err.println("There should not be a ship that size left");
            }
        }
    }


    @Override
    public int[] getSchuss() {
        if (this.slayship)
            return getSlayShoot();
        else {
            int langquer = rdm.nextInt(2);
            int y,x,lx,ly,z;
            z=y=x=lx=ly=0;
            for (int was = 0; was < 2; was++) {
                if (langquer == 0) {
                    langquer = 1;
                    y = rdm.nextInt(this.y);
                    for (int n = 0; n < this.y; n++) {
                        lx = 0;
                        for (x = 0; x < this.x; x++) {
                            //System.out.println("(x|y) "+x+"|"+y);
                            if (dasSpiel.getFeld()[1][x][y] != 0) {
                                lx = x + 1;
                            }else{
                                if (x - lx +1>= longestShip){
                                    z=rdm.nextInt((x-lx)+1)+lx;
                                    while(z-lx+1<smallestShip&& z<=x)
                                        z++;
                                    return new int[]{z, y};
                                }

                            }
                        }
                        x--;
                        if (x - lx +1>= longestShip){
                            z=rdm.nextInt((x-lx)+1)+lx;
                            while(z-lx+1<smallestShip&& z<=x)
                                z++;
                            return new int[]{z, y};
                        }
                        y += 1;
                        if (y >= this.y)
                            y = 0;
                    }
                } else {
                    langquer = 0;
                    x = rdm.nextInt(this.x);
                    for (int n = 0; n < this.x; n++) {
                        ly = 0;
                        for (y = 0; y < this.y; y++) {
                            //System.out.println("(x|y) "+x+"|"+y);
                            if (dasSpiel.getFeld()[1][x][y] != 0) {
                                ly = y + 1;
                            }else{
                                if (y - ly +1>= longestShip){
                                    z=rdm.nextInt((y-ly)+1)+ly;
                                    while(z-ly+1<smallestShip&& z<=y)
                                        z++;
                                    return new int[]{x, z};
                                }
                                //
                            }
                        }
                        y--;
                        if (y - ly +1>= longestShip){
                            z=rdm.nextInt((y-ly)+1)+ly;
                            while(z-ly+1<smallestShip&& z<=y)
                                z++;
                            return new int[]{x, z};
                        }
                        x += 1;
                        if (x >= this.x)
                            x = 0;
                    }
                }
            }


            System.err.println("Bot should have found something! " + longestShip);
            return rdmSchuss(this.dasSpiel, this.rdm, this.x, this.y);
        }
    }
}
