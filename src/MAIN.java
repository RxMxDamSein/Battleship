


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class MAIN extends Application  {
    Stage window;
    Scene scene_sizeS;
    TextField inputFieldX;
    TextField inputFieldY;
    Button buttonSubmit;
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
        inputFieldX.setPromptText("20");
        inputFieldX.setOnAction(e->submitFunc());
        inputFieldY=new TextField();
        inputFieldY.setPromptText("20");
        inputFieldY.setOnAction(e->submitFunc());
        buttonSubmit=new Button("SUBMIT");
        buttonSubmit.setOnAction(e->submitFunc());

        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.setSpacing(5);
        vBox.getChildren().addAll(inputFieldX,inputFieldY,buttonSubmit);

        scene_sizeS=new Scene(vBox);
        window.setScene(scene_sizeS);
        window.show();
    }

    private void submitFunc(){
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
            if(x==0)
                x=20;
            if(y==0)
                y=20;
        }
        System.out.println("X: "+x+" Y: "+y);
        Grid.display(window,x,y,scene_sizeS);

    }

}
