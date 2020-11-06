import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;


public class MAIN extends Application  {
    Stage window;
    Scene scene_sizeS;
    TextField inputFieldX;
    TextField inputFieldY;
    Button buttonSubmit;
    Button buttonSubmitBot;
    public static void main(String[] args) {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        window.setTitle("ENTER GRID SIZE");

        inputFieldX=new TextField();
        inputFieldX.setPromptText("5");
        inputFieldX.setOnAction(e->submitFunc());
        inputFieldY=new TextField();
        inputFieldY.setPromptText("5");
        inputFieldY.setOnAction(e->submitFunc());
        buttonSubmit=new Button("SUBMIT 2P");
        buttonSubmit.setOnAction(e->submitFunc());
        buttonSubmitBot=new Button("BOT");
        buttonSubmitBot.setOnAction(e->submitFuncBot());

        /*MenuBar menuBar=new MenuBar();
        Menu m1=new Menu("Stift!");
        MenuItem mi1=new MenuItem("GROß!");
        mi1.setOnAction(e-> System.out.println("OPEN! YOUR FAVOURITE WEBSITE"));
        m1.getItems().add(mi1);
        menuBar.getMenus().addAll(m1);*/
        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.setSpacing(5);
        //vBox.getChildren().addAll(menuBar,inputFieldX,inputFieldY,buttonSubmit);
        HBox hBox=new HBox(5);
        hBox.getChildren().addAll(buttonSubmit,buttonSubmitBot);
        vBox.getChildren().addAll(inputFieldX,inputFieldY,hBox);

        scene_sizeS=new Scene(vBox);
        window.setScene(scene_sizeS);
        window.show();
    }
    private void submitFuncBot()  {
        int[] xy=checkXY();
        int x=xy[0];
        int y=xy[1];
        //System.out.println("X: "+x+" Y: "+y);
        new BotGrid(window,x,y,scene_sizeS);
    }

    private void submitFunc()  {
        int[] xy=checkXY();
        int x=xy[0];
        int y=xy[1];
        System.out.println("X: "+x+" Y: "+y);
        new Grid(window,x,y,scene_sizeS);

    }
    private int[] checkXY(){
        String xS=inputFieldX.getText();
        String yS=inputFieldY.getText();
        int x=0,y=0;
        try {
            if(!xS.equals("")){
                x=Integer.parseInt(xS);
            }
            if(!yS.equals("")){
                y=Integer.parseInt(yS);
            }
        }catch (NumberFormatException e){

        }finally {
            if(x<=0)
                x=5;
            if(y<=0)
                y=5;
        }
        return new int[]{x,y};
    }
}