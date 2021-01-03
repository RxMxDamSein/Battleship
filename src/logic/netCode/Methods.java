package logic.netCode;

import logic.RemoteGameClient;
import logic.Spiel;
import logic.logicOUTput;

public class Methods {
    public static String ships(String[] s, Spiel dasSpiel)
    {
        for(int i=0;i<s.length;i++){
            RemoteGameClient.shipsAddConsole(Integer.parseInt(s[i]),dasSpiel);
        }
        logicOUTput.printFeld(dasSpiel.getFeld(),true);
        return "done";
    }

    public static String size(String sx, String sy,Spiel dasSpiel)
    {
        int x = Integer.parseInt(sx);
        int y = Integer.parseInt(sy);
        dasSpiel.setSize(x, y);
        dasSpiel.init();
        return "next";
    }

    public static String shot(String sx, String sy,Spiel dasSpiel)
    {
        int x = Integer.parseInt(sx);
        int y = Integer.parseInt(sy);
        dasSpiel.shoot(x,y,0,0,false);
        logicOUTput.printFeld(dasSpiel.getFeld(),true);
        switch (dasSpiel.getFeld()[0][x][y]){
            case 2:
                if(dasSpiel.istVersenkt()){
                    return "answer 2";
                }else{
                    return "answer 1";
                }
            default:
                return "answer 0";
        }
    }
}
