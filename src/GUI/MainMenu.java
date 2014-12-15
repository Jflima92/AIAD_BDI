package GUI;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.ThreadSuspendable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by jorgelima on 25-11-2014.
 */
public class MainMenu extends JFrame {
    private JButton startAgentMarketButton;
    private JButton instructionsButton;
    private JPanel menuPanel;
    private ThreadSuspendable sus;
    private IComponentManagementService cms;
    JLabel picLabel;


    public MainMenu(IComponentManagementService cms, ThreadSuspendable sus) throws IOException {
        super("Agent Configuration");

        this.cms = cms;
        this.sus = sus;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(menuPanel);
        setVisible(true);
        pack();
        startAgentMarketButton.addActionListener(new ButtonListener());
    }


    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            startSellerAgent(cms, sus);

        }
    }

    public void startSellerAgent(IComponentManagementService cms, ThreadSuspendable sus) {


        IComponentIdentifier cid = cms.createComponent("/home/jorgelima/workspace/AIAD/bin/Logic.SellerAgentBDI.class", null).getFirstResult(sus);
        System.out.println(cid.getName());

        System.out.println("Started Seller Agent component: " + cid);
    }

}

