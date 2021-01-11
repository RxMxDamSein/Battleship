package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public  class nuetzlicheMethoden {
    private int texture=0;
    private Image textureWasser,textureSchiff,textureSchiffTreffer,textureWasserTreffer,textureversenkt,textureauswahlWasser;


    public nuetzlicheMethoden(int x){
        //TODO DENNIS MACH SACHEN (NUR IMAGE SPEICHERN)
        texture = EinstellungenController.skin;
        textureWasser = new Image("GUI/Textures/WundervollesWasser.png");

        textureSchiff = new Image("GUI/Textures/Schiff.png");

        textureSchiffTreffer = new Image("GUI/Textures/trefferSchiff.png");

        textureWasserTreffer = new Image("GUI/Textures/trefferWasser.png");

        textureversenkt = new Image("GUI/Textures/trefferSchiff.png");

        textureauswahlWasser = new Image("GUI/Textures/ausgeaehltesWasser.png");
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
                ImageView view = new ImageView(textureWasser);
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
                ImageView view = new ImageView(textureSchiff);
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
                ImageView view = new ImageView(textureSchiffTreffer);
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
                ImageView view = new ImageView(textureWasserTreffer);
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
                ImageView view = new ImageView(textureversenkt);
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
                ImageView view = new ImageView(textureauswahlWasser);
                view.setFitHeight(minsizeberechner(x));
                view.setPreserveRatio(true);
                z.setGraphic(view);
                break;

        }
        return z;
    }

    /**
     *
     * @param gewinner true wenn man gewonnen hat
     */
    public void GameEnd(boolean gewinner) {

        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.setPadding(new Insets(10,10,10,10));
        comp.setSpacing(5);
        comp.setStyle("-fx-background-color: DARKCYAN;");
        comp.setAlignment(Pos.CENTER);
        Label label = new Label();
        if (gewinner) {
            label.setText("Du hast Gewonnen!!");
        } else {
            label.setText("Du hast Verloren!!");
        }
        if (gewinner) {
            label.setStyle("-fx-background-color: green");
        } else {
            label.setStyle("-fx-background-color: red");
        }
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(200,50);
        Button BackMenu = new Button();
        BackMenu.setPrefSize(60,30);
        BackMenu.setText("Exit");
        BackMenu.setOnAction(event1 -> {
            newStage.close();
        });
        comp.getChildren().add(label);
        comp.getChildren().add(BackMenu);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
    }

}
