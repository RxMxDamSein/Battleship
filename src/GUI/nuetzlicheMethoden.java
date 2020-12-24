package GUI;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public  class nuetzlicheMethoden {
    private int texture=0;


    public nuetzlicheMethoden(){}

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
                Image img = new Image("GUI/Textures/WundervollesWasser.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
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
                Image img = new Image("GUI/Textures/Schiff.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
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
                Image img = new Image("GUI/Textures/trefferSchiff.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
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
                Image img = new Image("GUI/Textures/trefferWasser.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
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
                Image img = new Image("GUI/Textures/trefferSchiff.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
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
                Image img = new Image("GUI/Textures/ausgeaehltesWasser.png");
                ImageView view = new ImageView(img);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
                break;

        }
        return z;
    }

}
