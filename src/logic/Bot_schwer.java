package logic;

public class Bot_schwer extends Bot{

    private int[] enemyShips;
    private int longestShip;
    /**
     * Bott Intialisierung des Spielfelds
     * x und y werte müssen >0 sein!
     *
     * @param x Spielbreite
     * @param y Spielhöhe
     */
    public Bot_schwer(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shipSizesToAdd(int[] s) {
        enemyShips=s;
        longestShip=s[0];
        return addShipsRDMly(s,dasSpiel,rdm,x,y);
    }


    @Override
    public void setSchussFeld(int x, int y, int wert, boolean versenkt) {
        super.setSchussFeld(x, y, wert, versenkt);
        if(versenkt){
            int len=shipLenDestroyed(x,y,1);
            boolean erfolg=false;
            for(int i=0;i<enemyShips.length;i++){
                if(enemyShips[i]==len){
                    erfolg=true;
                    enemyShips[i]=-1;
                    longestShip=-1;
                    for(int j=i+1;j<enemyShips.length;j++){
                        if(enemyShips[j]>0 && longestShip<enemyShips[j]){    //noch eine richtige Größe
                            //System.err.println("NEW longeShip "+enemyShips[j]);;
                            longestShip=enemyShips[j];
                        }
                    }


                    break;
                }
                if(!erfolg){
                    System.err.println("There should not be a ship that size left");
                }
            }
        }
    }

    @Override
    public int[] getSchuss() {
        if(this.slayship)
            return getSlayShoot();
        else{
            int y=rdm.nextInt(this.y);
            for(int n=0;n<this.y;n++) {
                int x, lx = 0;
                for (x = 0; x < this.x; x++) {
                    //System.out.println("(x|y) "+x+"|"+y);
                    if (dasSpiel.getFeld()[1][x][y] != 0) {
                        if (x - lx >= longestShip)
                            return new int[]{rdm.nextInt(x), y};
                        lx = x + 1;
                    }
                }
                if (x - lx >= longestShip)
                    return new int[]{rdm.nextInt(x), y};
                y+=1;
                if(y>=this.y)
                    y=0;
            }
            int x=rdm.nextInt(this.x);
            for(int n=0;n<this.x;n++) {
                int ly = 0;
                for (y = 0; y < this.y; y++) {
                    //System.out.println("(x|y) "+x+"|"+y);
                    if (dasSpiel.getFeld()[1][x][y] != 0) {
                        if (y - ly >= longestShip)
                            return new int[]{x,rdm.nextInt(y)};
                        ly = y + 1;
                    }
                }
                if (y - ly >= longestShip)
                    return new int[]{x,rdm.nextInt(y)};
                x+=1;
                if(x>=this.x)
                    x=0;
            }

        }
        System.err.println("Bot should have found something! "+longestShip);
        return rdmSchuss(this.dasSpiel,this.rdm,this.x,this.y);
    }
}
