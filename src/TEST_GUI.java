import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TEST_GUI extends JFrame {
    JLabel text=new JLabel();
    JPanel panel=new JPanel();
    JButton bt_dennis = new JButton();
    JButton bt_inClass = new JButton();

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
        bt_inClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bt_inClass.setText("AND IT JUST WORKS! @Bethesda");
                bt_inClass_click();
            }
        });
        bt_inClass.setText("Für diesen Button habe ich eine Klasse in einer Klasse!");
        panel.add(text);
        panel.add(bt_dennis);
        panel.add(bt_inClass);
        add(panel);
    }

    private void bt_inClass_click(){
        System.out.println("Da kann noch viel mehr stehen!");
    }
}
