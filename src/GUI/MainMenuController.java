package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class MainMenuController implements Initializable {
    @FXML private TextField GridSizex;
    @FXML private TextField GridSizey;
    @FXML private ChoiceBox choiceBox;
    @FXML private Button Start;
    @FXML private Button Clear;
    public Integer x=0,y=0;



    public void StartButton(ActionEvent actionEvent) throws IOException {
        String sx = String.valueOf(GridSizex.getText()); //Text aus der Benutzereingabe
        String sy = String.valueOf(GridSizey.getText()); //Text aus der Benutzereingabe
        Object value = choiceBox.getValue();
        String ch = (String) value;

        //bidde wieder weg machen
        if (sx.equals("") || sy.equals("")) {
            sx = "4";
            sy = "4";
        }




        x = Integer.parseInt(sx);
        y = Integer.parseInt(sy);

        int q=0;
        if(ch.equals("Singleplayer")  ) {
            q=1;
        }
        if(ch.equals("Multiplayer")) q=2;
        if(ch.equals("0") | ch.equals("NULL")) q=0;

        System.out.println(x);
        System.out.println(y);
        System.out.println(q);
        //System.out.println(f);
/*
        Parent root = FXMLLoader.load(getClass().getResource("GameGrid.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();

 */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGrid.fxml"));
        Parent r = loader.load();
        GameGridController controller = loader.getController();
        controller.setInteger(x,y);
        Scene s = new Scene(r);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();
    }
    public void clearButton(ActionEvent event) {
        GridSizex.clear();
        GridSizey.clear();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox.getItems().add("Singleplayer");
        choiceBox.getItems().add("Multiplayer");
        choiceBox.setValue("Singleplayer");

    }


}
