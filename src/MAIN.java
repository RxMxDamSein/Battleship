
import logic.*;

public class MAIN  {
    public static void main(String[] args) {
        System.out.println("THIS IS GAME LOGIC!");
        Spiel dasSpiel=new Spiel(10,2);
        dasSpiel.init();
        logicOUTput.printFeld(dasSpiel.getFeld());
    }

}
