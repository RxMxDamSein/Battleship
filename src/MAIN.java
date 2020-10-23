
import logic.*;

public class MAIN  {
    public static void main(String[] args) {
        System.out.println("THIS IS GAME LOGIC!");
        Spiel dasSpiel=new Spiel(10,2);
        dasSpiel.init();
        dasSpiel.addShip(0,0,false,2,0);
        dasSpiel.addShip(3,0,false,2,1);
        dasSpiel.starteSpiel();
        dasSpiel.shoot(3,0,1);
        dasSpiel.shoot(3,0,1);
        dasSpiel.shoot(2,0,1);
        dasSpiel.shoot(1,0,1);
        logicOUTput.printFeld(dasSpiel.getFeld(),true);
    }

}
