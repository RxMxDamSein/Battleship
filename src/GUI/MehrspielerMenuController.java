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

/**
 * Klaase fuer das Mehrspieler-Menu
 */
public class MehrspielerMenuController implements Initializable {
    /**
     * initialize Funktion von JavaFX
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Button um zuruck zum MainMenu zu kommen
     * @param event
     * @throws IOException
     */
    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MainMenu");
        window.show();
    }
    /**
     * Button um zum HostMenu zu kommen
     * @param event
     * @throws IOException
     */
    public void hostButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HostMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("HostMenu");
        window.show();
    }
    /**
     * Button um zum JoinMenu zu kommen
     * @param event
     * @throws IOException
     */
    public void joinButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("JoinMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("JoinMenu");
        window.show();
    }
}
