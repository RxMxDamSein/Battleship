import javax.swing.*;

public class TEST_GUI extends JFrame {
    public TEST_GUI(){
        System.out.println("STARTING JFRAME!");
        setTitle("unser Fenster");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 360);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
