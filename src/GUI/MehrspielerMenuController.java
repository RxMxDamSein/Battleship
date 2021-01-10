package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MehrspielerMenuController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MainMenu");
        window.show();
    }

    public void hostButton(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("HostMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("HostMenu");
        window.show();
    }

    public void joinButton(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("JoinMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("JoinMenu");
        window.show();
    }
}
