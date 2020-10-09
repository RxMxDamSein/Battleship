import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TEST_GUI_ActionListener implements ActionListener {
    JButton bt_dennis;

    public  TEST_GUI_ActionListener(JButton bt_dennis){
        this.bt_dennis=bt_dennis;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        bt_dennis.setText(bt_dennis.getText()+"+1");
    }
}
