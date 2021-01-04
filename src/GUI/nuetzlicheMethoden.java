package GUI;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

import javax.swing.text.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public  class nuetzlicheMethoden {
    private int texture=0;
    private ImageView textureWasser,textureSchiff,textureSchiffTreffer,textureWasserTreffer,textureversenkt,textureauswahlWasser;


    public nuetzlicheMethoden(int x){


        //TODO DENNIS MACH SACHEN (NUR IMAGE SPEICHERN)
        texture = EinstellungenController.skin;
        Image img = new Image("GUI/Textures/WundervollesWasser.png");
        textureWasser = new ImageView(img);
        textureWasser.setFitHeight(minsizeberechner(x));
        textureWasser.setPreserveRatio(true);

        img = new Image("GUI/Textures/Schiff.png");
        textureSchiff = new ImageView(img);
        textureSchiff.setFitHeight(minsizeberechner(x));
        textureSchiff.setPreserveRatio(true);

         img = new Image("GUI/Textures/trefferSchiff.png");
        textureSchiffTreffer = new ImageView(img);
        textureSchiffTreffer.setFitHeight(minsizeberechner(x));
        textureSchiffTreffer.setPreserveRatio(true);

         img = new Image("GUI/Textures/trefferWasser.png");
        textureWasserTreffer = new ImageView(img);
        textureWasserTreffer.setFitHeight(minsizeberechner(x));
        textureWasserTreffer.setPreserveRatio(true);

         img = new Image("GUI/Textures/trefferSchiff.png");
        textureversenkt = new ImageView(img);
        textureversenkt.setFitHeight(minsizeberechner(x));
        textureversenkt.setPreserveRatio(true);

         img = new Image("GUI/Textures/ausgeaehltesWasser.png");
        textureauswahlWasser= new ImageView(img);
        textureauswahlWasser.setFitHeight(minsizeberechner(x));
        textureauswahlWasser.setPreserveRatio(true);

    }

    public double minsizeberechner(int x) {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        //System.out.println("Höhe: "+screen.getHeight()+" Weite: "+screen.getWidth());
        //return -((double)x-10)+50;
        //return -(0.75* (double) x-7.5)+50;
        //double zahl = java.lang.Math.exp(-(0.05*x-4.3))+5;
        double zahl = (screen.getHeight()>screen.getWidth())?screen.getHeight():screen.getWidth();
        zahl*= 0.7;
        zahl = (zahl/2)/(double) x;
        //System.out.println("Wundervolle Zahl: "+zahl);
        if (zahl > 200) zahl=200;
        return zahl;
    }
    public Label textureWasser(Label z,int x) {
        switch (texture) {
            case 0:
                z.setStyle("-fx-background-color: #03fcf4");
                break;
            case 1:
                z.setGraphic(textureWasser);
            break;

        }
        return z;
    }
    public Label textureSchiff(Label z,int x) {
        switch (texture) {
            case 0:
                z.setStyle("-fx-background-color: grey");
                break;
            case 1:
                z.setGraphic(textureSchiff);
                break;

        }
        return z;
    }
    public Label textureSchiffTreffer(Label z,int x) {
        switch (texture) {
            case 0:
                z.setStyle("-fx-background-color: red");
                break;
            case 1:
                z.setGraphic(textureSchiffTreffer);
                break;

        }
        return z;
    }
    public Label textureWasserTreffer(Label z,int x) {
        switch (texture) {
            case 0:
                z.setStyle("-fx-background-color: blue");
                break;
            case 1:
                z.setGraphic(textureWasserTreffer);
                break;

        }
        return z;
    }
    public Label textureversenkt(Label z,int x) {
        switch (texture) {
            case 0:
                z.setStyle("-fx-background-color: black");
                break;
            case 1:
                z.setGraphic(textureversenkt);
                break;

        }
        return z;
    }
    public Label textureauswahlWasser(Label z,int x) {
        switch (texture) {
            case 0:
                z.setStyle("-fx-background-color: white");
                break;
            case 1:
                z.setGraphic(textureauswahlWasser);
                break;

        }
        return z;
    }

}
