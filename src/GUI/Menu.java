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
    private JComboBox SVariationPercentageCombo;
    private JButton createSellerAgentButton;
    private JPanel SellerAgentPanel;
    private JPanel BuyerAgentPanel;
    private JPanel BuyersInfoPanel;
    private JPanel SellersInfoPanel;
    private JButton createBuyerAgentButton;
    private JTextField BinsertProductNameTextField;
    private JTextField BinsertDesiredPriceTextField;
    private JTextField BinsertNumberOfItemsTextField;
    private JComboBox SECommerceCombo;
    private JTextField SinsertMaxNegTime;
    private JTextField BinsertMaxNegTime;
    private JComboBox BECommerceCombo;
    private JComboBox BVariationPercentageCombo;

    private Integer SellerAgentCount;
    private Integer BuyerAgentCount;

    public Menu(IComponentManagementService cms, ThreadSuspendable sus)
    {
        super("Electronic Agent Market");
        this.cms = cms;
        this.sus = sus;
        SellersInfoPanel.setLayout(new BoxLayout(SellersInfoPanel, BoxLayout.PAGE_AXIS));
        BuyersInfoPanel.setLayout(new BoxLayout(BuyersInfoPanel, BoxLayout.PAGE_AXIS));

        this.SellerAgentCount = 0;
        this.BuyerAgentCount = 0;
        this.setContentPane(rootPanel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        createSellerAgentButton.addActionListener(new CreateSellerButtonListener() {
        });
        createBuyerAgentButton.addActionListener(new CreateBuyerButtonListener() {
        });
    }

    public String getInsertProductNameTextField() {
        return SinsertProductNameTextField.getText();
    }

    public double getInsertInitialPriceTextField() {
        return Double.valueOf(SinsertInitialPriceTextField.getText());
    }

    public double getInitialStockTextField() {
        return Double.valueOf(SinitialStockTextField.getText());
    }

    public String getBinsertProductNameTextField() {
        return BinsertProductNameTextField.getText();
    }

    public double getBinsertDesiredPriceTextField() {
        return Double.valueOf(BinsertDesiredPriceTextField.getText());
    }

    public int getBinsertNumberOfItemsTextField() {
        return Integer.valueOf(BinsertNumberOfItemsTextField.getText());
    }



    public class CreateSellerButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            startSellerAgent(cms, sus);

        }
    }

    public class CreateBuyerButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            startBuyerAgent(cms, sus);

        }
    }

    public void startSellerAgent(IComponentManagementService cms, ThreadSuspendable sus) {

        this.SellerAgentCount = this.SellerAgentCount+1;
        String name = "Agent " + Integer.toString(this.SellerAgentCount);
        Map<String, Object> agentArgs = new HashMap<String, Object>();
        agentArgs.put("product", this.getInsertProductNameTextField());
        agentArgs.put("initPrice", this.getInsertInitialPriceTextField());
        agentArgs.put("initStock", this.getInitialStockTextField());
        JLabel Label = new JLabel("Seller " + name);
        SellersInfoPanel.add(Label);
        SellersInfoPanel.repaint();
        CreationInfo SellerInfo = new CreationInfo(agentArgs);

        IComponentIdentifier cid = cms.createComponent(name, "SellerAgentBDI.class", SellerInfo).getFirstResult(sus);
        System.out.println(cid.getName());

        System.out.println("Started Seller Agent component: " + cid.getName());

    }



    public void startBuyerAgent(IComponentManagementService cms, ThreadSuspendable sus) {

        this.BuyerAgentCount = this.BuyerAgentCount+1;
        String name = "Agent " + Integer.toString(this.BuyerAgentCount);

        Map<String, Object> agentArgs = new HashMap<String, Object>();
        agentArgs.put("product", this.getBinsertProductNameTextField());
        agentArgs.put("desiredPrice", this.getBinsertDesiredPriceTextField());
        agentArgs.put("nou", this.getBinsertNumberOfItemsTextField());
        JLabel Label = new JLabel("Buyer " + name);
        BuyersInfoPanel.add(Label);
        BuyersInfoPanel.repaint();
        CreationInfo BuyerInfo = new CreationInfo(agentArgs);

        IComponentIdentifier cid = cms.createComponent(name, "BuyerAgentBDI.class", BuyerInfo).getFirstResult(sus);


    }

}
