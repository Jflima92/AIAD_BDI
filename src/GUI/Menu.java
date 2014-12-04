package GUI;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.ThreadSuspendable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jorgelima on 04-12-2014.
 */
public class Menu extends JFrame {

    private ThreadSuspendable sus;
    private IComponentManagementService cms;

    private JTabbedPane tabbedPane1;
    private JPanel CreateAgentPanel;
    private JPanel InformationPanel;
    private JPanel rootPanel;
    private JTextField SinsertInitialPriceTextField;
    private JTextField SinsertProductNameTextField;
    private JTextField SinitialStockTextField;
    private JComboBox SinsertMaxDiscountComboBox;
    private JComboBox SelectronicCommerceTacticComboBox;
    private JButton createSellerAgentButton;
    private JPanel SellerAgentPanel;
    private JPanel BuyerAgentPanel;

    public Menu(IComponentManagementService cms, ThreadSuspendable sus)
    {
        this.cms = cms;
        this.sus = sus;

        this.setContentPane(rootPanel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        createSellerAgentButton.addActionListener(new ButtonListener() {
        });
    }

    public String getInsertProductNameTextField() {
        return SinsertProductNameTextField.toString();
    }

    public int getInsertInitialPriceTextField() {
        return Integer.valueOf(SinsertInitialPriceTextField.getText());
    }

    public int getInitialStockTextField() {
        return Integer.valueOf(SinsertInitialPriceTextField.getText());
    }


    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            startSellerAgent(cms, sus);

        }
    }

    public void startSellerAgent(IComponentManagementService cms, ThreadSuspendable sus) {

        Map<String, Object> agentArgs = new HashMap<String, Object>();
        agentArgs.put("product", this.getInsertProductNameTextField());
        agentArgs.put("initPrice", this.getInsertInitialPriceTextField());
        agentArgs.put("initStock", this.getInitialStockTextField());

        CreationInfo SellerInfo = new CreationInfo(agentArgs);

        IComponentIdentifier cid = cms.createComponent("/home/jorgelima/workspace/AIAD/bin/SellerAgentBDI.class", SellerInfo).getFirstResult(sus);
        System.out.println(cid.getName());

        System.out.println("Started Seller Agent component: " + cid);
    }

}
