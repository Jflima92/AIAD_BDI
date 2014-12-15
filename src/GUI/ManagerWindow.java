package GUI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jorgelima on 15-12-2014.
 */
public class ManagerWindow extends JFrame {


    protected AgentsModel agentsModel = new AgentsModel();

    private JPanel ManagerPanel;
    private JLabel beforeIntro;
    private JTable table1;

    public ManagerWindow() {

        super("Market Manager ");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel AgentArea = new JPanel(new GridLayout(0, 2));


        AgentArea.add(this.beforeIntro);
        table1 = new JTable(agentsModel);
        table1.setFillsViewportHeight(true);
        add(new JScrollPane(table1));
        add(AgentArea);
        pack();
        setVisible(true);


    }

    protected class AgentsModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"Agent", "Reputation"};
        private ArrayList<String[]> agents = new ArrayList();


        public String getColumnName(int column) {
            return columnNames[column];
        }

        public String getValueAt(int row, int column) {
            return agents.get(row)[column];
        }

        public int getRowCount() {
            return agents.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public void addAgent(String agentName, double reputation) {
            String[] row = new String[]{"" + agentName, "" + reputation};
            agents.add(row);

        }

        public void updateAgent(String agentName, double Reputation) {

            for (int i = 0; i < agents.size(); i++) {
                String[] agent = agents.get(i);
                String str = new String("" + agentName);
                if (agent[0].equals(str)) {
                agent[1] = new String("" + Reputation);
                }
            }
        }
    }

    public void addAgent(String name, double reputation) {
        agentsModel.addAgent(name, reputation);
        table1.repaint();
    }

    public void updateAgent(String name, double reputation) {
        agentsModel.updateAgent(name, reputation);
        table1.repaint();


    }


}