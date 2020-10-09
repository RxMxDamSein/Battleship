import javax.swing.*;
import java.awt.*;


public class TEST_GUI extends JFrame {
    JLabel text=new JLabel();
    JPanel panel=new JPanel();
    JButton bt_dennis = new JButton();

    public TEST_GUI(){
        System.out.println("STARTING JFRAME!");
        setTitle("unser Fenster");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 360);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        initComponents();

    }

    private void initComponents(){
        text.setText("Hi Dennis und Mirko");
        bt_dennis.addActionListener(new TEST_GUI_ActionListener(bt_dennis));
        bt_dennis.setText("Dennis einmal draufklicken bitte");
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(text);
        panel.add(bt_dennis);
        add(panel);
    }
}
