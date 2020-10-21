import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;


public class Grid {

    public static void display(Stage window, int x, int y, Scene sceneOld){
        window.setTitle("GRID!");
        Label[][] labels=new Label[x][y];
        Random random=new Random();
        GridPane gridPane=new GridPane();
        for(int i=0;i<x;i++) {
            for (int j = 0; j < y; j++) {
                switch (random.nextInt(4)) {
                    default:
                        //System.out.println("RAND ERROR");
                        labels[i][j] = new Label("ERROR");
                        labels[i][j].setTextFill(Color.web("red"));
                        break;
                    case 0: case 1: case 2:
                        labels[i][j] = new Label("W ");
                        labels[i][j].setTextFill(Color.web("blue"));
                        break;
                    case 3:
                        labels[i][j] = new Label("S ");
                        break;
                }
                GridPane.setConstraints(labels[i][j], i, j, 1, 1, HPos.CENTER, VPos.CENTER);
                gridPane.getChildren().add(labels[i][j]);
            }
        }
        Button buttonZuruck=new Button("Zurück");
        buttonZuruck.setOnAction(e->{window.setScene(sceneOld);
        window.setTitle("ENTER GRID SIZE");});

        VBox vBox=new VBox();
        vBox.getChildren().addAll(gridPane,buttonZuruck);

        Scene sceneGrid=new Scene(vBox);
        window.setScene(sceneGrid);

    }
}
