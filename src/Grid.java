import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;


public class Grid {
    static ImageView imgV;
    static Image img;

    public static void display(Stage window, int x, int y, Scene sceneOld)  {
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


        try{
            img=new Image(new FileInputStream("./img/crosshair.png"));
            imgV =new ImageView();
            imgV.setVisible(false);
        }catch (FileNotFoundException e){
            imgV =null;
        }




        VBox vBox=new VBox();
        vBox.getChildren().addAll(gridPane,buttonZuruck, imgV);

        Scene sceneGrid=new Scene(vBox);
        ImageCursor imgC=new ImageCursor(img,10,10);

        gridPane.setOnMouseEntered(e-> {imgV.setVisible(true);
            sceneGrid.setCursor(imgC);});
        gridPane.setOnMouseExited(e-> {imgV.setVisible(false);
            sceneGrid.setCursor(ImageCursor.DEFAULT);
             });
        window.setScene(sceneGrid);

    }
}
