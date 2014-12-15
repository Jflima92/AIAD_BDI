package GUI;

import Logic.Proposal;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Vascof92 on 14/12/2014.
 */
public class BuyerWindow extends JFrame {
    private JLabel baseprice;
    private JLabel avgpricevalue;
    private JLabel basepricevalue;
    private JProgressBar itemsleft;


    protected JTable buys;

    protected BuysModel buysmodel = new BuysModel();

    public BuyerWindow(String productType, int numberofitems, double baseprice) {

        super("Buyer ");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel AgentArea = new JPanel(new GridLayout(0, 2));

        AgentArea.add(new JLabel("Product: "));
        AgentArea.add(new JLabel(productType));



        basepricevalue.setText(Double.toString(baseprice));
        AgentArea.add(this.baseprice);
        AgentArea.add(basepricevalue);

        //itemsleft = new JProgressBar(0, numberofitems);
        itemsleft.setMinimum(0);
        itemsleft.setMaximum(numberofitems);
        itemsleft.setStringPainted(true);

        AgentArea.add(itemsleft);

        buys = new JTable(buysmodel);
        buys.setFillsViewportHeight(true);



        //AgentArea.add(new JScrollPane(buys));
        add(AgentArea);
        pack();
        setVisible(false);




    }


    protected class BuysModel extends AbstractTableModel {
        private String[] columnNames = new String[]{"Quantity Bought", "Buy Price"};
        private ArrayList<String[]> sales = new ArrayList();


        public String getColumnName(int column) {
            return columnNames[column];
        }

        public String getValueAt(int row, int column) {
            return sales.get(row)[column];
        }

        public int getRowCount() {
            return sales.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public void addProposal(double amount, double price) {
            String[] row = new String[]{ ""+amount,""+ price};
            sales.add(row);

        }




    };

    public void addProposal(Proposal p) {
        buysmodel.addProposal(p.getR().getNumberOfItems(), p.getPrice());
        buys.repaint();
    }

    public void update(int nitems) {
        itemsleft.setValue(itemsleft.getValue() + nitems);


    }
}
