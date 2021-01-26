package GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.Spiel;
import logic.save.SAFE_SOME;

import javax.swing.text.View;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Klasse mit nuetzliche Methoden
 */
public class nuetzlicheMethoden {
    /**
     * int Wert, welcher skin genutzt werden soll
     */
    private int texture = 1;
    /**
     * Images fuer die Labels
     */
    private Image textureWasser, textureSchiff, textureSchiffTreffer, textureWasserTreffer, textureversenkt, textureauswahlWasser;
    /**
     * Timeline
     */
    private Timeline timeline;
    /**
     * Port fuer Client
     */
    Integer Port = 50000;
    /**
     * Feldgroesse vom Spielfeld
     */
    Integer Feldgroesse = null;
    /**
     * int fuer Bot auswahl
     */
    Integer bot = null;
    /**
     * Save-Game fuers Speichern und Laden
     */
    SAFE_SOME SAFE = null;
    /**
     * Name des Save-Games
     */
    String id = " ";

    /**
     * Standartkonstruktor
     */
    public nuetzlicheMethoden() {
        texture = EinstellungenController.skin;
        textureWasser = new Image("GUI/Textures/WundervollesWasser.png");

        textureSchiff = new Image("GUI/Textures/Schiff.png");

        textureSchiffTreffer = new Image("GUI/Textures/trefferSchiff.png");

        textureWasserTreffer = new Image("GUI/Textures/trefferWasser.png");

        textureversenkt = new Image("GUI/Textures/versenkt.png");

        textureauswahlWasser = new Image("GUI/Textures/ausgeaehltesWasser.png");
    }

    /**
     * Konstruktor
     * @param x Feldgroesse
     */
    public nuetzlicheMethoden(int x) {
        Feldgroesse = x;
        texture = EinstellungenController.skin;
        textureWasser = new Image("GUI/Textures/WundervollesWasser.png");

        textureSchiff = new Image("GUI/Textures/Schiff.png");

        textureSchiffTreffer = new Image("GUI/Textures/trefferSchiff.png");

        textureWasserTreffer = new Image("GUI/Textures/trefferWasser.png");

        textureversenkt = new Image("GUI/Textures/versenkt.png");

        textureauswahlWasser = new Image("GUI/Textures/ausgeaehltesWasser.png");
    }

    /**
     * berechnetnet anhand der Monitorgroesse die groesse der Labels
     * @param x Feldgroesse
     * @return
     */
    public double minsizeberechner(int x) {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        //System.out.println("Höhe: "+screen.getHeight()+" Weite: "+screen.getWidth());
        //return -((double)x-10)+50;
        //return -(0.75* (double) x-7.5)+50;
        //double zahl = java.lang.Math.exp(-(0.05*x-4.3))+5;
        double zahl = (screen.getHeight() > screen.getWidth()) ? screen.getHeight() : screen.getWidth();
        zahl *= 0.7;
        zahl = (zahl / 2) / (double) x;
        //System.out.println("Wundervolle Zahl: "+zahl);
        if (zahl > 200) zahl = 200;
        return zahl;
    }

    /**
     * Wasser texture fuer die Labels
     * @param z Label
     * @param x Feldgroesse
     * @return Label
     */
    public Label textureWasser(Label z, int x) {
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
    /**
     * Schiff texture fuer die Labels
     * @param z Label
     * @param x Feldgroesse
     * @return Label
     */
    public Label textureSchiff(Label z, int x) {
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
    /**
     * Schiff-treffer texture fuer die Labels
     * @param z Label
     * @param x Feldgroesse
     * @return Label
     */
    public Label textureSchiffTreffer(Label z, int x) {
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
    /**
     * Wasser-treffer texture fuer die Labels
     * @param z Label
     * @param x Feldgroesse
     * @return Label
     */
    public Label textureWasserTreffer(Label z, int x) {
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
    /**
     * verdenkt texture fuer die Labels
     * @param z Label
     * @param x Feldgroesse
     * @return Label
     */
    public Label textureversenkt(Label z, int x) {
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
    /**
     * auswahl texture fuer die Labels
     * @param z Label
     * @param x Feldgroesse
     * @return Label
     */
    public Label textureauswahlWasser(Label z, int x) {
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
     * End Bildschirm, welcher anzeigt ob man gewonnen oder verloren hat
     * @param gewinner true wenn man gewonnen hat
     */
    public void GameEnd(boolean gewinner) {

        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.setPadding(new Insets(10, 10, 10, 10));
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
        label.setPrefSize(200, 50);
        Button BackMenu = new Button();
        BackMenu.setPrefSize(120, 30);
        BackMenu.setText("MainMenu");
        BackMenu.setOnAction(event1 -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                Scene scene = new Scene(root);
                MainMenuController.primaryStage.setTitle("MainMenu");
                MainMenuController.primaryStage.setScene(scene);
                MainMenuController.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            newStage.close();
        });
        comp.getChildren().add(label);
        comp.getChildren().add(BackMenu);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
    }

    /**
     * true wenn der Client sich Connecten konnte
     */
    private boolean success;
    /**
     * zaehl-Variable
     */
    private int count = 0;

    /**
     * Wartebildschirm fuer den Client
     * @param Client instanz der Klasse Client
     * @param BotClient instanz der Klasse BotClient
     */
    public void warteBildschirm(Client Client, BotClient BotClient) {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.setPadding(new Insets(10, 10, 10, 10));
        comp.setSpacing(5);
        comp.setStyle("-fx-background-color: DARKCYAN;");
        comp.setAlignment(Pos.CENTER);
        Label label = new Label();
        label.setText("Bitte warten...");
        label.setAlignment(Pos.CENTER);
        //label.setFont(new Font("Ink Free",14));
        label.setFont(new Font("System", 14));
        label.setPrefSize(250, 30);
        Button BackMenu = new Button();
        BackMenu.setPrefSize(80, 30);
        BackMenu.setText("Abbruch");
        BackMenu.setOnAction(event1 -> {
            timeline.stop();
            newStage.close();
        });
        comp.getChildren().add(label);
        comp.getChildren().add(BackMenu);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();

        timeline = new Timeline(new KeyFrame(new Duration(50), event -> {
            count++;
            if (!success) {
                if (count % 70 == 0) {
                    timeline.stop();
                    newStage.close();
                }
                return;
            }
            try {
                if (BotClient == null) {
                    if (!setMultiClientSpielerGrid(Client)) {
                        if (count % 20 == 0) {
                            label.setText(label.getText() + ".");
                        }

                    } else {
                        timeline.stop();
                        newStage.close();
                    }
                } else {
                    if (!setMultiClientBotGrid(BotClient)) {
                        if (count % 20 == 0) {
                            label.setText(label.getText() + ".");
                        }
                    } else {
                        timeline.stop();
                        newStage.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Client != null && Client.ERROR || BotClient != null && BotClient.ERROR) {
                label.setText("Konnte keine Verbindung herstellen!!");
                label.setStyle("-fx-background-color: #df0052");
                success = false;
                count = 0;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        success = true;
        timeline.play();
    }

    /**
     * wechselt zum MultiClientSpielerGrid, wenn man sich verbinden konnte
     * @param Client instanz der Klasse Client
     * @return
     * @throws IOException
     */
    private boolean setMultiClientSpielerGrid(Client Client) throws IOException {
        if (Client.ERROR || Client.status < 1) {
            if (Client.ERROR) System.err.println("Error or status < 1!");
            return false;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiClientSpielerGrid.fxml"));
        Parent r = loader.load();
        MultiClientSpielerController controller = loader.getController();
        controller.setVariables(Client);
        Scene s = new Scene(r);
        MainMenuController.primaryStage.setScene(s);
        MainMenuController.primaryStage.setTitle("Client Spieler");
        MainMenuController.primaryStage.show();
        timeline.stop();
        return true;
    }

    /**
     * wechselt zum MultiClientBotGrid, wenn man sich verbinden konnte
     * @param Client instanz der Klasse BotClient
     * @return
     * @throws IOException
     */
    private boolean setMultiClientBotGrid(BotClient Client) throws IOException {
        if (Client.ERROR || Client.status < 1) {
            if (Client.ERROR) System.err.println("Error or status < 1!");
            return false;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiClientBotGrid.fxml"));
        Parent r = loader.load();
        MultiClientBotController controller = loader.getController();
        controller.setVariables(Client);
        Scene s = new Scene(r);
        MainMenuController.primaryStage.setScene(s);
        MainMenuController.primaryStage.setTitle("Client Bot");
        MainMenuController.primaryStage.show();
        timeline.stop();
        return true;
    }

    //Integer Port,Integer Feldgroesse, SAFE_SOME SAFE, String id
    /**
     * neue Stage fuer den HostwarteBildschirm
     */
    Stage newStage;

    /**
     * HostwarteBildschirm, welcher ueberprueft ob der Server erstellt werden konnte
     */
    public void HostwarteBildschirm() {
        newStage = new Stage();
        Label label = new Label();
        VBox comp = new VBox();
        comp.setPadding(new Insets(10, 10, 10, 10));
        comp.setSpacing(5);
        comp.setStyle("-fx-background-color: DARKCYAN;");
        comp.setAlignment(Pos.CENTER);

        label.setText("Sever wird erstellt...");
        label.setAlignment(Pos.CENTER);
        //label.setFont(new Font("Ink Free",14));
        label.setFont(new Font("System", 14));
        label.setPrefSize(250, 30);
        Button BackMenu = new Button();
        BackMenu.setPrefSize(80, 30);
        BackMenu.setText("Abbruch");
        BackMenu.setOnAction(event1 -> {
            //timeline.stop();
            newStage.close();
        });
        Button start = new Button("Start");
        start.setOnAction(event3 -> {
            try {
                if (bot == null) {
                    if (SAFE == null) {
                        //Port, Feldgroesse, null, null
                        if (!setMultiHostSpielerGrid()) {
                            label.setText("Sever fehler");
                            label.setStyle("-fx-background-color: #df0052");
                            /*
                            timeline = new Timeline(new KeyFrame(new Duration(10000),event -> {
                                label.setText(label.getText()+".");
                            }));
                            timeline.setCycleCount(5);
                            timeline.play();

                             */
                            Integer i = 1;
                            while (i % 1000990990 != 0) {
                                i++;
                                //System.out.println("PENIS "+i);
                            }
                            i = 1;

                            while (i % 1000990990 != 0) {
                                i++;
                                //System.out.println("PENIS "+i);
                            }
                            i = 1;
                            //newStage.close();
                            System.out.println("Penis");
                            return;
                        }
                        newStage.close();
                    } else {
                        //Port, null, SAFE, id
                        if (!setMultiHostSpielerGrid()) {
                            label.setText("Sever fehler");
                            label.setStyle("-fx-background-color: #df0052");
                            Thread.sleep(60);
                            newStage.close();
                        }
                        newStage.close();
                    }
                } else {
                    if (SAFE == null) {
                        //Port,Feldgroesse,bot,null,null
                        if (!setMultiHostBotGrid()) {
                            label.setText("Sever fehler");
                            label.setStyle("-fx-background-color: #df0052");
                            Thread.sleep(60);

                            newStage.close();
                        }
                        newStage.close();
                    } else {
                        //Port,null,bot,SAFE,id
                        if (!setMultiHostBotGrid()) {
                            label.setText("Sever fehler");
                            label.setStyle("-fx-background-color: #df0052");
                            Thread.sleep(60);
                            newStage.close();
                        }
                        newStage.close();
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("HostWarteBildschirmFehler");
                e.printStackTrace();
            }

        });
        comp.getChildren().add(label);
        comp.getChildren().add(start);
        comp.getChildren().add(BackMenu);
        Scene stageScene = new Scene(comp, 300, 150);
        newStage.setScene(stageScene);
        newStage.show();
        ////

        /*
        try{
            if (bot == null) {
                if (SAFE == null) {
                    //Port, Feldgroesse, null, null
                    if (!setMultiHostSpielerGrid()) {
                        label.setText("Sever fehler");
                        label.setStyle("-fx-background-color: #df0052");
                        timeline = new Timeline(new KeyFrame(new Duration(10000),event -> {
                            label.setText(label.getText()+".");
                        }));
                        timeline.setCycleCount(5);
                        timeline.play();
                        newStage.close();
                    }
                    newStage.close();
                } else {
                    //Port, null, SAFE, id
                    if (!setMultiHostSpielerGrid()) {
                        label.setText("Sever fehler");
                        label.setStyle("-fx-background-color: #df0052");
                        Thread.sleep(60);
                        newStage.close();
                    }
                    newStage.close();
                }
            }else {
                if (SAFE == null) {
                    //Port,Feldgroesse,bot,null,null
                    if (!setMultiHostBotGrid()) {
                        label.setText("Sever fehler");
                        label.setStyle("-fx-background-color: #df0052");
                        Thread.sleep(60);
                        newStage.close();
                    }
                    newStage.close();
                } else {
                    //Port,null,bot,SAFE,id
                    if (!setMultiHostBotGrid()) {
                        label.setText("Sever fehler");
                        label.setStyle("-fx-background-color: #df0052");
                        Thread.sleep(60);
                        newStage.close();
                    }
                    newStage.close();
                }
            }
        }catch (IOException | InterruptedException e){
            System.out.println("HostWarteBildschirmFehler");
            e.printStackTrace();
        }

         */


        ///////////////////////
/*
        timeline = new Timeline(new KeyFrame(new Duration(50),event->{
            count++;
            if (!success){
                if (count%70==0){
                    timeline.stop();
                    newStage.close();
                }
                return;
            }
            try {
                if (SAFE == null){
                    //nicht laden
                    if (HostBotController == null) {
                        if () {
                            if (count%20==0) {
                                label.setText(label.getText() + ".");
                            }

                        } else {
                            timeline.stop();
                            newStage.close();
                        }
                    } else {
                        if (!setMultiClientBotGrid(BotClient)) {
                            if (count%20==0) {
                                label.setText(label.getText() + ".");
                            }
                        } else {
                            timeline.stop();
                            newStage.close();
                        }
                    }
                }else {
                    //laden

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Client != null && Client.ERROR || BotClient != null && BotClient.ERROR) {
                label.setText("Konnte keine Verbindung herstellen!!");
                label.setStyle("-fx-background-color: #df0052");
                success=false;
                count=0;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        success=true;
        timeline.play();

 */
    }

    //Integer Port,Integer Feldgroesse, SAFE_SOME SAFE, String id
    //int i = 1;
    //Timeline t;

    /**
     * wechselt auf MultiHostSpielerGrid, wenn der Server erstellt werden konnte
     * @return
     * @throws IOException
     */
    private boolean setMultiHostSpielerGrid() throws IOException {
        if (SAFE == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostSpielerGrid.fxml"));
            Parent r = loader.load();
            MultiHostSpielerController controller = loader.getController();
            controller.setVariables(Port, Feldgroesse);
            /*
            while (i%10000!=0){
                i++;
                //System.out.println("PENIS "+i);
            }
            i=1;

             */

            ////////////////////
            /*
             t = new Timeline(new KeyFrame(new Duration(10000),event -> {
                i++;
                if (i%1000==0){
                    t.stop();
                }
            }));
            t.setCycleCount(Animation.INDEFINITE);
            t.play();

             */
            /*
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

             */
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!controller.Host.Hosted) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (controller.Host.ERROR || !controller.Host.Hosted) {
                    return false;
                }
            }
            Scene s = new Scene(r);
            MainMenuController.primaryStage.setScene(s);
            MainMenuController.primaryStage.setTitle("Host Spieler");
            MainMenuController.primaryStage.show();
            return true;
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostSpielerGrid.fxml"));
            Parent r = loader.load();
            MultiHostSpielerController controller = loader.getController();
            controller.setVariables(Port, SAFE, id);
            while (!controller.Host.Hosted) {
                if (controller.Host.ERROR) {
                    return false;
                }
            }
            Scene s = new Scene(r);
            MainMenuController.primaryStage.setScene(s);
            MainMenuController.primaryStage.setTitle("Host Spieler");
            MainMenuController.primaryStage.show();
            return true;
        }
    }

    //Integer Port,Integer Feldgroesse,Integer bot, SAFE_SOME SAFE, String id
    /**
     * wechselt auf MultiHostBotGrid, wenn der Server erstellt werden konnte
     * @return
     * @throws IOException
     */
    private boolean setMultiHostBotGrid() throws IOException {
        if (SAFE == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostBotGrid.fxml"));
            Parent r = loader.load();
            MultiHostBotController controller = loader.getController();
            controller.setVariables(Port, Feldgroesse, bot);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!controller.Host.Hosted) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (controller.Host.ERROR || !controller.Host.Hosted) {
                    return false;
                }
            }
            Scene s = new Scene(r);
            MainMenuController.primaryStage.setScene(s);
            MainMenuController.primaryStage.setTitle("Host Bot");
            MainMenuController.primaryStage.show();
            return true;
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiHostBotGrid.fxml"));
            Parent r = loader.load();
            MultiHostBotController controller = loader.getController();
            controller.setVariables(Port, SAFE, id, bot);
            while (!controller.Host.Hosted) {
                if (controller.Host.ERROR) {
                    return false;
                }
            }
            Scene s = new Scene(r);
            MainMenuController.primaryStage.setScene(s);
            MainMenuController.primaryStage.setTitle("Host Bot");
            MainMenuController.primaryStage.show();
            return true;
        }
    }

    /**
     * setzt Host Variablen
     * @param Port Port
     * @param Feldgroesse Feldgroesse
     */
    public void setHostVariablen(Integer Port, Integer Feldgroesse) {
        this.Port = Port;
        this.Feldgroesse = Feldgroesse;
    }

    /**
     * setzt Host Variablen
     * @param Port Port
     * @param SAFE Sava-Game
     * @param id Save-Game-ID
     */
    public void setHostVariablen(Integer Port, SAFE_SOME SAFE, String id) {
        this.Port = Port;
        this.SAFE = SAFE;
        this.id = id;
    }

    /**
     * setzt Host Variablen
     * @param Port Port
     * @param Feldgroesse Feldgroesse
     * @param bot Bot-Schwierigkeit
     */
    public void setHostVariablen(Integer Port, Integer Feldgroesse, Integer bot) {
        setHostVariablen(Port, Feldgroesse);
        this.bot = bot;
    }

    /**
     * setzt Host Variablen
     * @param Port Port
     * @param bot Bot-Schwierigkeit
     * @param SAFE Sava-Game
     * @param id Save-Game-ID
     */
    public void setHostVariablen(Integer Port, Integer bot, SAFE_SOME SAFE, String id) {
        this.Port = Port;
        this.bot = bot;
        this.SAFE = SAFE;
        this.id = id;
    }

    /*
    public static void setStageCenter() {
        double width = MainMenuController.primaryStage.getWidth();
        double height = MainMenuController.primaryStage.getHeight();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        System.out.println("Stage W: " + width + " H: " + height + ";Screen W: " + bounds.getWidth() + " H: " + bounds.getHeight());
        MainMenuController.primaryStage.setX((bounds.getWidth() - width) / 2);
        MainMenuController.primaryStage.setY((bounds.getHeight() - height) / 2);
    }

     */
    /**
     * Timeline fuer setAbschussLabelTimeline
     */
    private Timeline abschussLabel;

    /**
     * zeigt mithilfe der Toplabels an, welcher Spieler am zug ist
     * @param dasSpiel das Spiel aus dem Logic Package
     * @param label1   linkes Label des Spielfelds
     * @param label2   rechtes Label des Spielfelds
     */
    public void setAbschussLabelTimeline(Spiel dasSpiel, Label label1, Label label2) {
        abschussLabel = new Timeline(new KeyFrame(new Duration(100), event -> {
            if (dasSpiel.isOver()) {
                abschussLabel.stop();
            }
            int spieler = dasSpiel.getAbschussSpieler();
            if (spieler == 0) {
                label1.setText("Gegner schießt!");
                label2.setText("");
            } else if (spieler == 1) {
                label1.setText("");
                label2.setText("Du schießt");
            }
        }));
        abschussLabel.setCycleCount(Animation.INDEFINITE);
        abschussLabel.play();
    }
}
