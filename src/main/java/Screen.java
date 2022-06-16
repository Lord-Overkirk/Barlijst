import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.JXTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


public class Screen {
    JFrame jFrame;
    JButton plusButton;

    JPanel namePanel;
    JPanel buttonPanel;

    JXTextField firstName;
    JXTextField lastName;
    JTextField nVal;
    JButton nButton;

    JTable table;
    BarTableModel barTableModel;
    TableRowSorter<BarTableModel> sorter;

    Screen() {
        this.jFrame = new JFrame();
        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.setSize(1250, 800);
        this.jFrame.setLocationRelativeTo(null);

        this.jFrame.setJMenuBar(new BarLijstMenu(this).jMenuBar);

        this.namePanel = new JPanel(new BorderLayout());
        this.buttonPanel = new JPanel(new GridLayout(5, 0));

        firstName = new JXTextField();
        this.firstName.setPreferredSize(new Dimension(200, 20));
        this.firstName.setPrompt("First name");
        this.namePanel.add(firstName, BorderLayout.LINE_START);

        lastName = new JXTextField();
        this.lastName.setPreferredSize(new Dimension(200, 20));
        this.lastName.setPrompt("Last name");
        this.namePanel.add(lastName, BorderLayout.CENTER);

        this.addButton();

        this.nVal = new JTextField();
        this.nButton = new JButton();
        this.buttonPanel.add(nVal);
        this.addNButton();

        this.jFrame.add(buttonPanel, BorderLayout.LINE_START);
        this.jFrame.add(namePanel, BorderLayout.PAGE_START);

        Object[] columnNames = { "User id", "First name", "Last name", "Balance", "Selected" };

        barTableModel = new BarTableModel(null, columnNames);
        barTableModel.addTableModelListener(new CheckListener());
        this.table = new JTable(barTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (col == 3) {
                    Integer intValue = (Integer) getValueAt(row, col);
                    c.setForeground(getColor(intValue));
                } else {
                    c.setForeground(getForeground());
                }
                return c;
            }

            private Color getColor(int intValue) {
                Color color = null;
                if (intValue > 0) {
                    color = Color.GREEN;
                } else if (intValue < 0) {
                    color = Color.RED;
                } else {
                    color = getForeground();
                }
                return color;
            }
        };
        this.table.setBounds(600, 300, 2000, 300);
        table.setFont(new Font("Arial", Font.PLAIN, 20));
        table.setRowHeight(table.getRowHeight()+5);

        sorter = new TableRowSorter<BarTableModel>(barTableModel);
        this.table.setRowSorter(sorter);

        ArrayList <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane = new JScrollPane(this.table);
        refreshTable();

        this.jFrame.add(scrollPane, BorderLayout.CENTER);
        this.jFrame.setVisible(true);

        this.jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Saving...");
                BarLijstMenu.shutdownHook();
                System.out.println("Closing 'Barlijst'");
                System.exit(0);
            }
        });
        this.jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void addButton() {
        this.plusButton = new JButton("Add user");

        this.plusButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                addButtonPressed();
            }
        });

        this.namePanel.add(this.plusButton, BorderLayout.LINE_END);
    }

    public void addNButton() {
        this.nButton = new JButton("N");

        this.nButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int readVal = 0;
                System.out.println(Screen.this.nVal.getText());
                if (!Screen.this.nVal.getText().equals("")) {
                    readVal = Integer.parseInt(Screen.this.nVal.getText());
                }

                final int incrVal = readVal;
                    increaseButtonPressed(incrVal);
            }
        });

        this.buttonPanel.add(this.nButton);
    }

    private void addButtonPressed() {
        Customer new_customer = new Customer(firstName.getText(), lastName.getText());
        BarLijst.customers.add(new_customer);
        System.out.println(BarLijst.customers);
        BarTableModel tableModel =  (BarTableModel) this.table.getModel();
        Object[] new_names = {new_customer.getUid(), new_customer.getFirstName(), new_customer.getLastName(), new_customer.getBalance(), false};
        tableModel.addRow(new_names);
    }

    public void increaseButtonPressed(int incrVal) {
        System.out.println("Selected is:" + BarLijst.selectedCustomers);
        for (Customer selected : BarLijst.selectedCustomers) {
            int current = selected.getBalance();
            selected.setBalance(current + incrVal);
            System.out.println("Balance is now: " + selected.getBalance());
        }
        refreshTable();
    }

    /**
     * Update the tableModel display. The old sorting is preserved during this routine.
     */
    private void refreshTable() {
        //update table visually
        List<? extends SortKey> old_sort_keys = table.getRowSorter().getSortKeys();

        Object[] columnNames = { "User id", "First name", "Last name", "Balance", "Selected" };
        Object[][] res = getCustomersObjectArray(BarLijst.customers);
        this.barTableModel.setDataVector(res, columnNames);

        // Preserve the old sorting of the displayed tableModel.
        table.getRowSorter().setSortKeys(old_sort_keys);
    }

    /**
     * Read the data from main memory. This is needed to update the displayed table.
     * @param customers, the list of customers currently in memory.
     * @return, the Object array that needs to be loaded into the table.
     */
    private Object[][] getCustomersObjectArray(ArrayList<Customer> customers) {
        Object[][] result = new Object[customers.size()][5];
        for (int i = 0; i < customers.size(); i++) {
            result[i][0] = customers.get(i).getUid();
            result[i][1] = customers.get(i).getFirstName();
            result[i][2] = customers.get(i).getLastName();
            result[i][3] = customers.get(i).getBalance();
            result[i][4] = customers.get(i).isSelected();
        }
        return result;
    }
}
