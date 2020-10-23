package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class logicOUTput {

    /**
     * You can play the game in Cosole both players are humans!
     */
    public static void console2SpielerSpiel()  {
        try {
            BufferedReader inRead = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Feld Breite(x): ");
            String in = inRead.readLine();
            int x = Integer.parseInt(in);
            System.out.print("Feld Länge(y): ");
            in = inRead.readLine();
            int y = Integer.parseInt(in);
            //System.out.println(x + " " + y);
            Spiel dasSpiel=new Spiel(x,y);
            dasSpiel.init();
            printFeld(dasSpiel.getFeld(),true);
            int size=3;
            int count=1;
            boolean shipAdded=false;
            while(size>0){
                System.out.println("Add ships cancel/stop with shipsize 0");
                System.out.print("Ship "+count+" size: ");
                in=inRead.readLine();
                size=Integer.parseInt(in);
                if(size<1)
                    break;
                int horizontal=0;
                shipAdded=false;
                while (!shipAdded){
                    System.out.print("Ship Player 1(0) x: ");
                    in= inRead.readLine();
                    x=Integer.parseInt(in);
                    System.out.print("Ship Player 1(0) y: ");
                    in= inRead.readLine();
                    y=Integer.parseInt(in);
                    System.out.print("Ship Player 1(0) horizontal(1 yes 0 no): ");
                    in= inRead.readLine();
                    horizontal=Integer.parseInt(in);
                    shipAdded=dasSpiel.addShip(x,y,(horizontal>0)?true:false,size,0);
                }
                shipAdded=false;
                while (!shipAdded){
                    System.out.print("Ship Player 2(1) x: ");
                    in= inRead.readLine();
                    x=Integer.parseInt(in);
                    System.out.print("Ship Player 2(1) y: ");
                    in= inRead.readLine();
                    y=Integer.parseInt(in);
                    System.out.print("Ship Player 2(1) horizontal(1 yes 0 no): ");
                    in= inRead.readLine();
                    horizontal=Integer.parseInt(in);
                    shipAdded=dasSpiel.addShip(x,y,(horizontal>0)?true:false,size,1);
                }

                count++;
                printFeld(dasSpiel.getFeld(),true);
            }
            printFeld(dasSpiel.getFeld(),true);
            dasSpiel.starteSpiel();
            while (true){
                int spieler=dasSpiel.getAbschussSpieler();
                System.out.println("Spieler "+(spieler+1)+" wird nun abgeschossen!");
                System.out.print("X Koordinate: ");
                in=inRead.readLine();
                x=Integer.parseInt(in);
                System.out.print("Y Koordinate: ");
                in=inRead.readLine();
                y=Integer.parseInt(in);
                dasSpiel.shoot(x,y,spieler);
                printFeld(dasSpiel.getFeld(),true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOEXCEPTION!");
        }
    }
    /**
     * gibt das Spielbrett aus
     * @param f ein aus der Klasse Spiel einzufügendes Feld -> getFeld()
     */
    public static void printFeld(int[][][] f){
        System.out.print("Spieler1: ");
        for(int i=0;i<f[0].length*2-7;i++)
            System.out.print(" ");
        System.out.print("   Spieler2:\n");
        System.out.print("   ");
        char buchstabe=65;
        for(int x=0;x<f[0].length;x++){
            if(x<10)
                System.out.print(x+" ");
            else{
                buchstabe=65;
                buchstabe+=x-10;
                System.out.print(buchstabe+" ");
            }
        }
        System.out.print("   ");
        for(int x=0;x<f[0].length;x++){
            if(x<10)
                System.out.print(x+" ");
            else{
                buchstabe=65;
                buchstabe+=x-10;
                System.out.print(buchstabe+" ");
            }
        }
        System.out.println("\n");
        for(int y=0;y<f[0][0].length;y++){
            buchstabe=97;
            buchstabe+=y;
            System.out.print(buchstabe+"  ");
            for(int s=0;s<f.length;s++){

                for(int x=0;x<f[0].length;x++){
                    System.out.print(f[s][x][y]+" ");
                }
                if(s<f.length-1)
                    System.out.print(" | ");
            }
            System.out.println("");
        }

    }

    /**
     * gibt das Spielbrett aus
     * @param f ein aus der Klasse Spiel einzufügendes Feld -> getFeld()
     * @param hint true -> gibt Legende mit aus
     */
    public static void printFeld(int[][][] f,boolean hint){
        printFeld(f);
        if(hint)
            System.out.println("0 -> frei, 1 -> Schiff, 2 -> Treffer, 3 -> Wasser");
    }
}
