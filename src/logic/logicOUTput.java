package logic;

public class logicOUTput {
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
        for(int x=0;x<f[0].length;x++)
            System.out.print(x+" ");
        System.out.print("   ");
        for(int x=0;x<f[0].length;x++)
            System.out.print(x+" ");
        System.out.println("\n");
        for(int y=0;y<f[0][0].length;y++){
            char buchstabe=97;
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
