package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jorgelima on 20-11-2014.
 */
public class SellerAgentConfigurations extends JFrame{

    JTextField insertProductNameTextField;
    JTextField insertInitialPriceTextField;
    JTextField initialStockTextField;
    JComboBox insertMaxDiscountComboBox;
    JComboBox electronicCommerceTacticComboBox;
    JButton createSellerAgentButton;
    private JPanel rootPanel;

    CountDownLatch doneSignal = new CountDownLatch(1);

    public SellerAgentConfigurations() {

        super("Agent Configuration");
        System.out.println("damns");
        System.out.println("go");

        this.setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        System.out.println("estou visivel");
        createSellerAgentButton.addActionListener(new ButtonListener());
    }

    public class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            doneSignal.countDown();
        }
    }

    public String getInsertProductNameTextField() {
        return insertProductNameTextField.toString();
    }

    public int getInsertInitialPriceTextField() {
        return Integer.valueOf(insertInitialPriceTextField.getText());
    }

    public int getInitialStockTextField() {
        return Integer.valueOf(insertInitialPriceTextField.getText());
    }

    public JComboBox getInsertMaxDiscountComboBox() {
        return insertMaxDiscountComboBox;
    }


    public JComboBox getElectronicCommerceTacticComboBox() {
        return electronicCommerceTacticComboBox;
    }

    public void waitForEnd() {
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            System.err.println("Wait for config dialog interrupted");
            e.printStackTrace();
        }
    }
}
