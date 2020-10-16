


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import sun.audio.AudioPlayer;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;


public class MAIN extends Application implements EventHandler<ActionEvent> {
    Button button;
    AudioClip explosion=new AudioClip(new File("./sfx/explosion.mp3").toURI().toString());

    public static void main(String[] args) {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TITEL");
        StackPane layout=new StackPane();
        button=new Button();
        button.setText("CLICK! ");
        button.setOnAction(this);
        Scene scene=new Scene(layout,420,300);
        layout.getChildren().add(button);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        button.setText(button.getText()+"pog ");



        explosion.setVolume(0.1);
        explosion.play();
    }

}
