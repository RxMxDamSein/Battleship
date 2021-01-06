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
import javafx.stage.Stage;
import logic.Bot;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JoinMenuController implements Initializable {
    @FXML private TextField IPText,PortText;
    @FXML private ChoiceBox<String> SpielartChoice,BotChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BotChoice.getItems().add("Einfach");
        BotChoice.getItems().add("Mittel");
        BotChoice.getItems().add("Schwer");
        BotChoice.setValue("Mittel");
        //BotChoice.hide();

        SpielartChoice.getItems().add("Spieler");
        SpielartChoice.getItems().add("Bot");
        SpielartChoice.setValue("Spieler");

    }
    public void backbutton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MehrspielerMenu.fxml"));
        Scene s = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.setTitle("MehrspielerMenu");
        window.show();
    }

    public void StartButton(ActionEvent event) throws IOException {
        Integer p;
        try {
            p = Integer.parseInt(PortText.getText());
        } catch (NumberFormatException e) {
            p = 420;
        }
        String ip;
        try {
            ip = IPText.getText();
        } catch (NumberFormatException e) {
            ip = "127.0.0.1";
        }
        String ausw = SpielartChoice.getValue();
        String ch = BotChoice.getValue();
        int bot=2;

        if (ch.equals("Einfach")) {
            bot = 1;
        }
        if (ch.equals("Mittel")) {
            bot = 2;
        }
        if (ch.equals("Schwer")) {
            bot = 3;
        }
        //Verbinden mit Host
        if (ausw.equals("Spieler")) {

        }

        if (ausw.equals("Spieler")) {
            Client Client = new Client(ip,p);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiClientSpielerGrid.fxml"));
            Parent r = loader.load();
            MultiClientSpielerController controller = loader.getController();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Client.ERROR|| Client.status<1){
                System.err.println("Error or status < 1!");
                return;
            }

            controller.setVariables(Client);

            //ToDo GUI Einfrieren
            while (Client.status != 3);
            Scene s = new Scene(r);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.setTitle("Client Spieler");
            window.show();
        } else if (ausw.equals("Bot")) {
            BotClient Client = new BotClient(ip,p,bot);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiClientBotGrid.fxml"));
            Parent r = loader.load();
            MultiClientBotController controller = loader.getController();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Client.ERROR|| Client.status<1){
                System.err.println("Error or status < 1!");
                return;
            }
            controller.setVariables(Client);
            Scene s = new Scene(r);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.setTitle("Client Bot");
            window.show();
        }
    }

}
