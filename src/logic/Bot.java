package logic;


import logic.save.ResourceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public abstract class Bot implements Serializable {
    private static final long serialVersionUID = 1337L;
    public Spiel dasSpiel;
    protected int x, y;
    public Random rdm;
    public boolean slayship = false; //true if you hit ship and not sunk
    public int slayX, slayY;

    int[] getSlayShoot() {

        //System.out.println("SLAYSHIP!");
        int zx = slayX;
        int zy = slayY;
        boolean horizontal = false;
        boolean vertical = false;
        if (zx > 0 && dasSpiel.getFeld()[1][zx - 1][zy] == 4 || zx < (x - 1) && dasSpiel.getFeld()[1][zx + 1][zy] == 4 || zx > 0 && dasSpiel.getFeld()[1][zx - 1][zy] == 2 || zx < (x - 1) && dasSpiel.getFeld()[1][zx + 1][zy] == 2)
            horizontal = true;
        if (zy > 0 && dasSpiel.getFeld()[1][zx][zy - 1] == 4 || zy > 0 && dasSpiel.getFeld()[1][zx][zy - 1] == 2 || zy < (y - 1) && dasSpiel.getFeld()[1][zx][zy + 1] == 4 || zy < (y - 1) && dasSpiel.getFeld()[1][zx][zy + 1] == 2)
            vertical = true;
        int schritt = 1;
        if (horizontal) {
            while (zx + schritt < x) {
                if (dasSpiel.getFeld()[1][zx + schritt][zy] == 0) {
                    return new int[]{zx + schritt, zy};
                } else if (dasSpiel.getFeld()[1][zx + schritt][zy] == 4 || dasSpiel.getFeld()[1][zx + schritt][zy] == 2) {
                    schritt++;
                    continue;
                } else {
                    break;
                }
            }
            schritt = -1;
            while (zx + schritt >= 0) {
                if (dasSpiel.getFeld()[1][zx + schritt][zy] == 0) {
                    return new int[]{zx + schritt, zy};
                } else if (dasSpiel.getFeld()[1][zx + schritt][zy] == 4 || dasSpiel.getFeld()[1][zx + schritt][zy] == 2) {
                    schritt--;
                    continue;
                } else {
                    break;
                }
            }
        } else if (vertical) {
            while (zy + schritt < y) {
                if (dasSpiel.getFeld()[1][zx][zy + schritt] == 0) {
                    return new int[]{zx, zy + schritt};
                } else if (dasSpiel.getFeld()[1][zx][zy + schritt] == 4 || dasSpiel.getFeld()[1][zx][zy + schritt] == 2) {
                    schritt++;
                    continue;
                } else {
                    break;
                }
            }
            schritt = -1;
            while (zy + schritt >= 0) {
                //System.out.println(zx+" "+zy);
                if (dasSpiel.getFeld()[1][zx][zy + schritt] == 0) {
                    return new int[]{zx, zy + schritt};
                } else if (dasSpiel.getFeld()[1][zx][zy + schritt] == 4 || dasSpiel.getFeld()[1][zx][zy + schritt] == 2) {
                    schritt--;
                    continue;
                } else {
                    break;
                }
            }
        } else {//check a postion up down left right rdmly
            while (true) {
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

        if (dasSpiel.getVerbose())
            System.err.println("there should be another ship piece close!");
        return rdmSchuss(dasSpiel, rdm, x, y);

    }

    /**
     * gibt zurück ob der Bot verloren hat!
     * genau wie is Over Function von Spiel
     *
     * @return true Bot hat verloren false Bott lebt noch
     */
    public boolean isFinOver() {
        return dasSpiel.isOver();
    }

    /**
     * Bott Intialisierung des Spielfelds
     * x und y werte müssen >0 sein!
     *
     * @param x Spielbreite
     * @param y Spielhöhe
     */
    public Bot(int x, int y) {
        this.x = x;
        this.y = y;
        dasSpiel = new Spiel(x, y, true);
        dasSpiel.init();
        //dasSpiel.setVerbose(false);
        rdm = new Random();
    }

    ;

    /**
     * Funktion die Schiffe dem Bott hinzufügt
     * Bitte Längen in absteigender Reihenfolge sonst geht Bot_schwer nicht!
     *
     * @param s {5,4,3,2,1} legt beim Bott 5 Schiffe an mit den Längen(5,4,3,2,1)
     * @return true wenn erfolgreich false wenn fehler
     */
    public abstract boolean shipSizesToAdd(int[] s);

    /**
     * schießt beim Bot ein Feld ab
     *
     * @param x
     * @param y
     * @return erfolg
     */
    public int abschiesen(int x, int y) {
        int p_hit = dasSpiel.getFeld()[0][x][y];
        boolean succ = dasSpiel.shoot(x, y, 0, 31, false);
        if (!succ)
            return -1;
        else if (dasSpiel.istVersenkt()) {
            //System.out.println("Der Bot hat verloren!");
            //fin=dasSpiel.isOver();
            return 4;
        } else
            return p_hit;
    }

    /**
     * berechnet die nächste Stelle an die der Bot schießt!
     *
     * @return {x,y}
     */
    public abstract int[] getSchuss();

    /**
     * Falls man das Spielobjekt des Bots gebrauchen kann #ToterCode
     *
     * @return Spiel
     */
    public Spiel getDasSpiel() {
        return dasSpiel;
    }

    /**
     * Hilfsfunktion zum zurücksetzten eines Spielbretts
     * z.Bsp. falls man die Schiffe schlecht gesetzt hat und neu anfagen will
     *
     * @param f       feld
     * @param spieler wessens feld soll gelöscht werden
     * @return erfolg
     */
    public static boolean resetFeld(int[][][] f, int spieler) {
        if (spieler < 0 || spieler > 1)
            return false;
        //System.out.println("Feld prereset!");
        //logicOUTput.printFeld(f);
        for (int i = 0; i < f[spieler].length; i++) {
            for (int j = 0; j < f[spieler][i].length; j++) {
                f[spieler][i][j] = 0;
            }
        }
        //System.out.println("Feld reset!");
        //logicOUTput.printFeld(f);
        return true;
    }

    /**
     * Hilfsfunktion um aus der Schiffsliste die Laengen zu extrahieren
     *
     * @param s Arraylist<Schiff>
     * @return {1,2,3,4,5} // alles Schiffslängen in einem int array
     */
    public static int[] getShipSizes(ArrayList<Schiff> s) {
        int[] sizes = new int[s.size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = s.get(i).schifflaenge;
        }
        java.util.Arrays.sort(sizes);
        return sizes;
    }

    /**
     * Setzt das angegebene Feld auf der Spielseite des Gegners vom Bot ausgesehen
     * dient dazu, dass der Bot weiß was er getroffen hat und ob er wetwas versenkt hat!
     *
     * @param x        XKoordinate
     * @param y        YKoordinate
     * @param wert     dieser wert wird eingesetzt feld[1][x][y]=wert
     * @param versenkt true das hier liegende Schiff wurde versenkt -> daneben muss Wasser sein für KI
     */
    public void setSchussFeld(int x, int y, int wert, boolean versenkt) {
        int[][][] f = dasSpiel.getFeld();
        f[1][x][y] = wert;
        if (wert == 2) {
            dasSpiel.setAbschussSpieler(1);
        } else {
            dasSpiel.setAbschussSpieler(0);
        }
        if (versenkt) {//make water around ship
            slayship = false;
            Bot.waterAround(x, y, f, this.x, this.y);
            System.out.println("after versenkt!");
            logicOUTput.printFeld(f, true);
        } else {
            if (wert == 2 || wert == 4) {
                slayship = true;
                slayX = x;
                slayY = y;
            }
        }
    }

    public boolean saveGame(String id) {
        try {
            ResourceManager.save(this, id);
        } catch (IOException e) {
            System.err.println("SAVE ERROR!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bot load(String id) {
        try {
            return (Bot) ResourceManager.load(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.err.println("load error");
        return null;
    }


    public static boolean addbiggestship(int[] s, boolean[] sa, Spiel dasSpiel, int x, int y, boolean h, int j) {
        int i;
        for (i = j; i >= 0; i--) {
            if (sa[i])
                continue;
            if (dasSpiel.addShip(x, y, h, s[i], 0)) {
                sa[i] = true;
                break;
            }

        }
        if (i == j)
            return true;
        return false;
    }

    public static boolean addShipsRDMly(int[] s, Spiel dasSpiel, Random rdm, int width, int height) {
        boolean v = dasSpiel.getVerbose();
        dasSpiel.setVerbose(false);
        boolean added = false;
        int count = 0;
        while (!added && count < 60) {
            count++;
            dasSpiel.schiffe = new ArrayList<Schiff>();
            //added=true;
            resetFeld(dasSpiel.getFeld(), 0);
            for (int i = 0; i < s.length; i++) {
                int zx = 0, zy = 0;
                int count2 = 0;
                do {
                    count2++;
                    zx = rdm.nextInt(width);
                    zy = rdm.nextInt(height);
                    added = dasSpiel.addShip(zx, zy, rdm.nextInt(2) == 0, s[i], 0);
                } while (added == false && count2 < 120);
                if (!added)
                    break;
            }
        }
        if (!added) {
            resetFeld(dasSpiel.getFeld(), 0);
            dasSpiel.schiffe = new ArrayList<>();
            int x = 0, y = 0;
            boolean h = (width > height) ? true : false;
            boolean step = true;
            boolean sa[] = new boolean[s.length];
            boolean fill = false;
            for (int i = s.length - 1; i >= 0; i--) {
                if (sa[i])
                    continue;
                //step=dasSpiel.addShip(x,y, (fill)?!h:h,s[i],0);
                //logicOUTput.printFeld(dasSpiel.getFeld(),true);
                step = addbiggestship(s, sa, dasSpiel, x, y, (fill) ? !h : h, i);
                if (h) {
                    if (fill) {
                        y--;
                    } else {
                        x += s[i] + 1;
                        if (x >= width) {
                            x = 0;
                            y += 2;
                        }
                        if (y >= height) {
                            y = height - 1;
                            x = width - 1;
                            fill = true;
                        }
                    }
                } else {
                    if (fill) {
                        x--;
                    } else {
                        y += s[i] + 1;
                        if (y >= height) {
                            y = 0;
                            x += 2;
                        }
                        if (x >= width) {
                            x = width - 1;
                            y = height - 1;
                            fill = true;
                        }
                    }
                }
                if (!step)
                    i++;
                else
                    sa[i] = true;
            }
            for (int i = 0; i < dasSpiel.schiffe.size(); i++) {
                Schiff ss = dasSpiel.schiffe.get(i);
                dasSpiel.feldDELSchiff(ss);
                int xo = ss.xOPos;
                int yo = ss.yOPos;
                boolean ho = ss.horizontal;
                count = 0;
                boolean ok;
                do {
                    ss.xOPos = rdm.nextInt(width);
                    ss.yOPos = rdm.nextInt(height);
                    ss.horizontal = rdm.nextBoolean();
                    count++;
                    ok = dasSpiel.checkLegalSchiff(ss);
                } while (!ok && count < 20);
                if (!ok) {
                    ss.xOPos = xo;
                    ss.yOPos = yo;
                    ss.horizontal = ho;
                }
                dasSpiel.feldAddSchiff(ss);
            }
        }


        logicOUTput.printFeld(dasSpiel.getFeld(), true);

        dasSpiel.starteSpiel();
        dasSpiel.setVerbose(v);
        return true;
    }

    protected static int[] rdmSchuss(Spiel dasSpiel, Random rdm, int width, int height) {
        int zx = 0, zy = 0, count = 0;
        do {
            count++;
            zx = rdm.nextInt(width);
            zy = rdm.nextInt(height);
        } while (dasSpiel.getFeld()[1][zx][zy] != 0 /*&& count<x*y*2*/);
        if (dasSpiel.getVerbose())
            System.out.println(dasSpiel.getFeld()[1][zx][zy] + " getSchuss " + zx + " " + zy);
        return new int[]{zx, zy};
    }

    public static void waterAround(int s, int x, int y, int[][][] f, int width, int height) {
        boolean up, down, left, right;
        up = down = left = right = false;
        if (y + 1 < height && (f[s][x][y + 1] == 2 || f[s][x][y + 1] == 4)) {
            down = true;
        }
        if (y - 1 >= 0 && (f[s][x][y - 1] == 2 || f[s][x][y - 1] == 4)) {
            up = true;
        }
        if (x + 1 < width && (f[s][x + 1][y] == 2 || f[s][x + 1][y] == 4)) {
            right = true;
        }
        if (x - 1 >= 0 && (f[s][x - 1][y] == 2 || f[s][x - 1][y] == 4)) {
            left = true;
        }
        int i;
        if (up) {
            for (i = 0; y - i >= 0 && (f[s][x][y - i] == 2 || f[s][x][y - i] == 4); i++) {
                f[s][x][y - i] = 4;
                if (x + 1 < width)
                    f[s][x + 1][y - i] = 3;
                if (x - 1 >= 0)
                    f[s][x - 1][y - i] = 3;
            }
            if (y - i >= 0) {
                if (x + 1 < width)
                    f[s][x + 1][y - i] = 3;
                if (x - 1 >= 0)
                    f[s][x - 1][y - i] = 3;
                f[s][x][y - i] = 3;
            }
            if (!down) {
                if (y + 1 < height) {
                    if (x + 1 < width)
                        f[s][x + 1][y + 1] = 3;
                    if (x - 1 >= 0)
                        f[s][x - 1][y + 1] = 3;
                    f[s][x][y + 1] = 3;
                }
            }
        }
        if (down) {
            for (i = 0; y + i < height && (f[s][x][y + i] == 2 || f[s][x][y + i] == 4); i++) {
                f[s][x][y + i] = 4;
                if (x + 1 < width)
                    f[s][x + 1][y + i] = 3;
                if (x - 1 >= 0)
                    f[s][x - 1][y + i] = 3;
            }
            if (y + i < height) {
                if (x + 1 < width)
                    f[s][x + 1][y + i] = 3;
                if (x - 1 >= 0)
                    f[s][x - 1][y + i] = 3;
                f[s][x][y + i] = 3;
            }
            if (!up) {
                if (y - 1 >= 0) {
                    if (x + 1 < width)
                        f[s][x + 1][y - 1] = 3;
                    if (x - 1 >= 0)
                        f[s][x - 1][y - 1] = 3;
                    f[s][x][y - 1] = 3;
                }
            }
        }
        if (left) {
            for (i = 0; x - i >= 0 && (f[s][x - i][y] == 2 || f[s][x - i][y] == 4); i++) {
                f[s][x - i][y] = 4;
                if (y + 1 < height)
                    f[s][x - i][y + 1] = 3;
                if (y - 1 >= 0)
                    f[s][x - i][y - 1] = 3;
            }
            if (x - i >= 0) {
                if (y + 1 < height)
                    f[s][x - i][y + 1] = 3;
                if (y - 1 >= 0)
                    f[s][x - i][y - 1] = 3;
                f[s][x - i][y] = 3;
            }
            if (!right) {
                if (x + 1 < width) {
                    if (y + 1 < height)
                        f[s][x + 1][y + 1] = 3;
                    if (y - 1 >= 0)
                        f[s][x + 1][y - 1] = 3;
                    f[s][x + 1][y] = 3;
                }
            }
        }
        if (right) {
            for (i = 0; x + i < width && (f[s][x + i][y] == 2 || f[s][x + i][y] == 4); i++) {
                f[s][x + i][y] = 4;
                if (y + 1 < height)
                    f[s][x + i][y + 1] = 3;
                if (y - 1 >= 0)
                    f[s][x + i][y - 1] = 3;
            }
            if (x + i < width) {
                if (y + 1 < height)
                    f[s][x + i][y + 1] = 3;
                if (y - 1 >= 0)
                    f[s][x + i][y - 1] = 3;
                f[s][x + i][y] = 3;
            }
            if (!left) {
                if (x - 1 >= 0) {
                    if (y + 1 < height)
                        f[s][x - 1][y + 1] = 3;
                    if (y - 1 >= 0)
                        f[s][x - 1][y - 1] = 3;
                    f[s][x - 1][y] = 3;
                }
            }
        }
        if (!up && !down && !right && !left) {
            f[s][x][y] = 4;
            if (x > 0 && y > 0)
                f[s][x - 1][y - 1] = 3;
            if (y > 0)
                f[s][x][y - 1] = 3;
            if (x + 1 < width && y > 0)
                f[s][x + 1][y - 1] = 3;
            if (x > 0)
                f[s][x - 1][y] = 3;
            if (x + 1 < width)
                f[s][x + 1][y] = 3;
            if (x > 0 && y + 1 < height)
                f[s][x - 1][y + 1] = 3;
            if (y + 1 < height)
                f[s][x][y + 1] = 3;
            if (x + 1 < width && y + 1 < height)
                f[s][x + 1][y + 1] = 3;
        }
    }

    public static void waterAround(int x, int y, int[][][] f, int width, int height) {
        waterAround(1, x, y, f, width, height);
    }

    public static int[] calcships(int x, int y) {
        int res = x * y;
        //Change von Dennis von >= zu ==
        if ((res == 100) && (x >= 5 || y >= 5)) {
            return new int[]{5, 4, 4, 3, 3, 3, 2, 2, 2, 2};
        } else if (res == 1)
            return new int[]{1};
        else if (res <= 4)
            return new int[]{2};
        int max = (x > y) ? x : y;
        if (max > 5) {
            max = 5;
            if (max < 5)
                max = 5;
        }

        ArrayList<Integer> s = new ArrayList<Integer>();
        int used = 0;
        Random rdm = new Random();
        while (used < res * 0.7) {
            int next = rdm.nextInt(max - 1) + 2;
            s.add(next);
            used += (next + 2) * 3;
        }

        int[] ret = new int[s.size()];
        for (int i = 0; i < s.size(); i++) {
            ret[i] = s.get(i);
        }
        return ret;
    }

    public static void main(String[] args) {
        int[] t = calcships(7, 5);
        for (int i = 0; i < t.length; i++)
            System.out.println(t[i]);
    }

    protected int shipLenDestroyed(int x, int y, int s) {
        int size = 0;
        boolean up, down, left, right;
        up = down = left = right = false;
        if (y + 1 < this.y && dasSpiel.getFeld()[s][x][y + 1] == 4) {
            down = true;
        }
        if (y - 1 >= 0 && dasSpiel.getFeld()[s][x][y - 1] == 4) {
            up = true;
        }
        if (x + 1 < this.x && dasSpiel.getFeld()[s][x + 1][y] == 4) {
            right = true;
        }
        if (x - 1 >= 0 && dasSpiel.getFeld()[s][x - 1][y] == 4) {
            left = true;
        }
        int i;
        if (up) {
            for (i = 0; y - i >= 0 && dasSpiel.getFeld()[s][x][y - i] == 4; i++) {
                size++;
            }
        }
        if (down) {
            for (i = 0; y + i < this.y && dasSpiel.getFeld()[s][x][y + i] == 4; i++) {
                size++;
            }

        }
        if (left) {
            for (i = 0; x - i >= 0 && dasSpiel.getFeld()[s][x - i][y] == 4; i++) {
                size++;
            }

        }
        if (right) {
            for (i = 0; x + i < this.x && dasSpiel.getFeld()[s][x + i][y] == 4; i++) {
                size++;
            }
        }
        if (!up && !down && !left && !right)
            size = 1;

        return size;
    }
}


