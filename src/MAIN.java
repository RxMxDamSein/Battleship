import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.RDM_Bot;
import logic.Spiel;


import GUI.GUImain;
import logic.*;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MAIN extends Application {


    Stage window;
    Scene scene_sizeS;
    TextField inputFieldX;
    TextField inputFieldY;
    TextField inputIP;
    TextField inputPort;
    TextField inputID;
    Button buttonServer;
    Button buttonServer_Load;
    Button buttonKlient;
    Button buttonServer_Bot;
    Button buttonServer_Bot_Load;
    Button buttonKlient_Bot;
    Button buttonSubmit;
    Button buttonSubmitBot;
    Button buttonLoad2P;
    Button buttonLoadRDMBot;
    Button buttonBvB;
    Button buttonLoadBvB;
    Button buttonNightmare;
    Button buttonLoadNightmare;

    public static void main(String[] args) {


        System.out.println("THIS IS GAME LOGIC!");
        //logicOUTput.console2SpielerSpiel();
        //logicOUTput.remoteTestSpiel();
        //logicOUTput.playagainstRDM_Bot();
        /*Spiel dasSpiel=new Spiel(10,2);
        dasSpiel.init();
        dasSpiel.addShip(0,0,false,2,0);
        dasSpiel.addShip(3,0,false,2,1);
        dasSpiel.starteSpiel(0);
        dasSpiel.shoot(0,0,0);
        dasSpiel.shoot(0,0,1);
        dasSpiel.shoot(1,0,0);
        dasSpiel.shoot(1,0,1);
        logicOUTput.printFeld(dasSpiel.getFeld(),true);*/
        //System.out.println("Working Directory = " +
        //       System.getProperty("user.dir"));
        GUImain.main(args);


        //launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("ENTER GRID SIZE");

        inputFieldX = new TextField();
        inputFieldX.setPromptText("5");
        inputFieldX.setOnAction(e -> submitFunc());
        inputFieldY = new TextField();
        inputFieldY.setPromptText("5");
        inputFieldY.setOnAction(e -> submitFunc());
        inputIP = new TextField();
        inputIP.setPromptText("127.0.0.1");
        inputPort = new TextField();
        inputPort.setPromptText("420");
        inputID = new TextField();
        inputID.setPromptText("195858270");
        buttonSubmit = new Button("SUBMIT 2P");
        buttonSubmit.setOnAction(e -> submitFunc());
        buttonSubmitBot = new Button("BOT");
        buttonSubmitBot.setOnAction(e -> submitFuncBot());
        buttonLoad2P = new Button("LOAD 2P");
        buttonLoad2P.setOnAction(e -> load2P());
        buttonLoadRDMBot = new Button("LOAD RDM");
        buttonLoadRDMBot.setOnAction(e -> loadRDM());
        buttonBvB = new Button("BvB");
        buttonBvB.setOnAction(e -> submitBvb());
        buttonLoadBvB = new Button(("LOAD BvB"));
        buttonLoadBvB.setOnAction(e -> loadBvb());
        buttonServer_Load = new Button("LOAD HOST");
        buttonServer_Load.setOnAction(e -> loadNET(false));
        buttonServer_Bot_Load = new Button("LOAD BOT HOST");
        buttonServer_Bot_Load.setOnAction(e -> loadNET(true));

        buttonServer = new Button("Host");
        buttonServer.setOnAction(e -> {
            try {
                submitNET(true, false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonKlient = new Button("Join");
        buttonKlient.setOnAction(e -> {
            try {
                submitNET(false, false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonServer_Bot = new Button("Host_B");
        buttonServer_Bot.setOnAction(e -> {
            try {
                submitNET(true, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonKlient_Bot = new Button("Join_B");
        buttonKlient_Bot.setOnAction(e -> {
            try {
                submitNET(false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        buttonNightmare = new Button(("Nightmare!"));
        buttonNightmare.setOnAction(e -> submitNightmare());
        buttonLoadNightmare = new Button(("LOAD Nightmare"));
        buttonLoadNightmare.setOnAction(e -> loadNightmare());
        //buttonNightmare

        /*MenuBar menuBar=new MenuBar();
        Menu m1=new Menu("Stift!");
        MenuItem mi1=new MenuItem("GROß!");
        mi1.setOnAction(e-> System.out.println("OPEN! YOUR FAVOURITE WEBSITE"));
        m1.getItems().add(mi1);
        menuBar.getMenus().addAll(m1);*/
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(5);
        //vBox.getChildren().addAll(menuBar,inputFieldX,inputFieldY,buttonSubmit);
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(buttonSubmit, buttonLoad2P, buttonSubmitBot, buttonLoadRDMBot, buttonBvB, buttonLoadBvB, buttonNightmare, buttonLoadNightmare);
        HBox hBox_IP = new HBox(5);
        hBox_IP.getChildren().addAll(buttonServer, buttonKlient, inputIP, inputPort, buttonServer_Bot, buttonKlient_Bot);
        HBox hBox_load_Host = new HBox(5);
        hBox_load_Host.getChildren().addAll(buttonServer_Load, buttonServer_Bot_Load, inputID);
        vBox.getChildren().addAll(inputFieldX, inputFieldY, hBox, hBox_IP, hBox_load_Host);


        scene_sizeS = new Scene(vBox);
        window.setScene(scene_sizeS);
        window.show();
    }

    private void loadNET(boolean Bot) {
        String port = inputPort.getText();
        int p = 420;
        try {
            p = Integer.parseInt(port);
        } catch (NumberFormatException e) {

        }
        if (Bot) {
            new Grid_NET_B(window, scene_sizeS, p, inputID.getText());
        } else {
            new Grid_NET(window, scene_sizeS, p, inputID.getText());
        }
    }

    private void submitNET(boolean Server, boolean Bot) throws IOException {
        int[] xy = checkXY();
        int x = xy[0];
        int y = xy[1];
        String ip = inputIP.getText();
        String port = inputPort.getText();
        int p = 420;
        try {
            p = Integer.parseInt(port);
        } catch (NumberFormatException e) {

        }
        if (ip.isEmpty())
            ip = "127.0.0.1";

        if (Server) {
            if (!Bot)
                new Grid_NET(window, x, y, scene_sizeS, p);
            else
                new Grid_NET_B(window, x, y, scene_sizeS, p);
        } else if (!Bot)
            new Grid_NET_Client(window, scene_sizeS, ip, p);
        else
            new Grid_NET_Client_B(window, scene_sizeS, ip, p);
    }

    private void loadBvb() {
        new BvB_GUI(window, scene_sizeS, "bvb");
    }

    private void submitBvb() {
        int[] xy = checkXY();
        int x = xy[0];
        int y = xy[1];
        new BvB_GUI(window, x, y, scene_sizeS);
    }

    private void submitNightmare() {
        int[] xy = checkXY();
        int x = xy[0];
        int y = xy[1];
        new NightmareGrid(window, x, y, scene_sizeS);
    }

    private void loadNightmare() {
        new NightmareGrid(window, scene_sizeS, "NightBOT-");
    }

    private void load2P() {
        new Grid(window, scene_sizeS, "2P");
//>>>>>>> origin/GAMELLOGIC
    }

    private void loadRDM() {
        new BotGrid(window, scene_sizeS, "RDMBOT-");
    }

    private void submitFuncBot() {
        int[] xy = checkXY();
        int x = xy[0];
        int y = xy[1];
        //System.out.println("X: "+x+" Y: "+y);
        new BotGrid(window, x, y, scene_sizeS);
    }

    private void submitFunc() {
        int[] xy = checkXY();
        int x = xy[0];
        int y = xy[1];
        System.out.println("X: " + x + " Y: " + y);
        new Grid(window, x, y, scene_sizeS);

    }

    private int[] checkXY() {
        String xS = inputFieldX.getText();
        String yS = inputFieldY.getText();
        int x = 0, y = 0;
        try {
            if (!xS.equals("")) {
                x = Integer.parseInt(xS);
            }
            if (!yS.equals("")) {
                y = Integer.parseInt(yS);
            }
        } catch (NumberFormatException e) {

        } finally {
            if (x <= 0)
                x = 10;
            if (y <= 0)
                y = 10;
        }
        return new int[]{x, y};
    }
}