package GUI;

import logic.Bot;
import logic.Spiel;
import logic.save.ResourceManager;

import java.io.IOException;
import java.io.Serializable;

public class SaveGame implements Serializable {
    private static final long serialVersionUID = 1337L;
    public Spiel g;
    public Bot b, b1, b2;

    public SaveGame(Spiel goettlichesspieldervernichtungmiTbot, Bot romansfabelhafteRbotDERNOCHVERBUGGTIST, String name) {
        g = goettlichesspieldervernichtungmiTbot;
        b = romansfabelhafteRbotDERNOCHVERBUGGTIST;
        try {
            ResourceManager.save(this, name);
        } catch (IOException e) {
            System.err.println("SAVE FEHLER!!");
        }

    }

    public SaveGame(Bot bot1, Bot bot2, String name) {

        b1 = bot1;
        b2 = bot2;
        try {
            ResourceManager.save(this, name);
        } catch (IOException e) {
            System.err.println("SAVE FEHLER!!");
        }

    }

    public static SaveGame load(String Id) {
        try {
            return (SaveGame) ResourceManager.load(Id);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("LOAAD FEHLER!!!!");
        }
        return null;
    }

}
