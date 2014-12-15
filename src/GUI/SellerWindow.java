package GUI;


import Logic.Proposal;
import Logic.Request;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Created by Vascof92 on 13/12/2014.
 */
public class SellerWindow extends JFrame {

    protected JLabel balance, balancevalue;
    protected JLabel baseprice, basepricevalue;
    protected JLabel stock, stockvalue, avgpricevalue, avgprice;
    protected JTable sales;

    protected class SalesModel extends AbstractTableModel {
        private String[] columnNames = new String[]{"Quantity Sold", "Sell Price"};
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

    protected SalesModel salesmodel = new SalesModel();

    public SellerWindow(String productType, double stock, double baseprice) {
        super("Seller ");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel AgentArea = new JPanel(new GridLayout(0, 2));

        AgentArea.add(new JLabel("Product: "));
        AgentArea.add(new JLabel(productType));


        AgentArea.add(this.stock);
        stockvalue.setText(Double.toString(stock));
        AgentArea.add(stockvalue);

        balancevalue.setText(Double.toString(0));
        AgentArea.add(balance);
        AgentArea.add(balancevalue);

        avgpricevalue.setText(Double.toString(0));
        AgentArea.add(avgprice);
        AgentArea.add(avgpricevalue);

        basepricevalue.setText(Double.toString(baseprice));
        AgentArea.add(this.baseprice);
        AgentArea.add(basepricevalue);

        sales = new JTable(salesmodel);
		sales.setFillsViewportHeight(true);


		add(new JScrollPane(sales));
        add(AgentArea);
        pack();
        setVisible(false);
    }

    public void update(double stock, double totalEarned, double avgprice) {
        stockvalue.setText(Double.toString(stock));
        avgpricevalue.setText((Double.toString(avgprice)));
        balancevalue.setText(Double.toString(totalEarned));
    }

    public void updatestock(double stock) {
        stockvalue.setText(Double.toString(stock));
    }

    public void updateprice(double price) {
        basepricevalue.setText(Double.toString(price));
    }


    public void addProposal(Proposal p) {
        salesmodel.addProposal(p.getR().getNumberOfItems(), p.getPrice());
        sales.repaint();
    }
}
