import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Screen {
    JFrame jFrame;
    JButton plusButton;
    JButton increaseButton;
    JButton increaseThirtyButton;
    JButton decreaseButton;
    JButton saveButton;
    JPanel namePanel;
    JPanel buttonPanel;
    JTextField firstName;
    JTextField lastName;
    JTextField plusVal;
    JTextField minVal;
    JTable table;

    Screen() {
        this.jFrame = new JFrame();
        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.setSize(400, 500);
        this.namePanel = new JPanel(new BorderLayout());
        this.buttonPanel = new JPanel(new GridLayout(5, 0));

        firstName = new JTextField();
        this.firstName.setPreferredSize(new Dimension(200, 20));
        this.namePanel.add(firstName, BorderLayout.LINE_START);

        lastName = new JTextField();
        this.lastName.setPreferredSize(new Dimension(200, 20));
        this.namePanel.add(lastName, BorderLayout.CENTER);

        this.addButton();
        this.addSaveButton();
        this.addIncreaseButton();
        this.addIncreaseThirtyButton();
        this.addDecreaseButton();

        this.plusVal = new JTextField();
        this.minVal = new JTextField();
        this.buttonPanel.add(plusVal);
        this.buttonPanel.add(minVal);

        this.jFrame.add(buttonPanel, BorderLayout.LINE_START);
        this.jFrame.add(namePanel, BorderLayout.PAGE_START);

        Object[] columnNames = { "User id", "First name", "Last name", "Balance", "Selected" };

        BarTableModel barTableModel = new BarTableModel(null, columnNames);
        barTableModel.addTableModelListener(new CheckListener());
        this.table = new JTable(barTableModel);
        this.table.setBounds(300, 40, 200, 300);
        JScrollPane scrollPane = new JScrollPane(this.table);
        refreshTable();

        this.jFrame.add(scrollPane, BorderLayout.CENTER);
        this.jFrame.setVisible(true);

        this.jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Saving...");
                shutdownHook();
                System.out.println("Closing 'Barlijst'");
                System.exit(0);
            }
        });
        this.jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void addSaveButton() {
        this.saveButton = new JButton("Save");

        this.saveButton.addActionListener(e -> shutdownHook());

        this.buttonPanel.add(this.saveButton);
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

    public void addIncreaseButton() {
        this.increaseButton = new JButton("+1");

        this.increaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseButtonPressed(1);
            }
        });

        this.buttonPanel.add(this.increaseButton);
    }

    public void addDecreaseButton() {
        this.decreaseButton = new JButton("-1");

        this.decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseButtonPressed(-1);
            }
        });

        this.buttonPanel.add(this.decreaseButton);
    }

    public void addIncreaseThirtyButton() {
        this.increaseThirtyButton = new JButton("+30");

        this.increaseThirtyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseButtonPressed(30);
            }
        });

        this.jFrame.add(this.increaseThirtyButton, BorderLayout.LINE_END);
    }

    private void addButtonPressed() {
        Customer new_customer = new Customer(firstName.getText(), lastName.getText());
        BarLijst.customers.add(new_customer);
        System.out.println(BarLijst.customers);
        BarTableModel tableModel =  (BarTableModel) this.table.getModel();
        Object[] new_names = {new_customer.getUid(), new_customer.getFirstName(), new_customer.getLastName(), new_customer.getBalance(), false};
        tableModel.addRow(new_names);
    }

    private void increaseButtonPressed(int incrVal) {
        System.out.println("Selected is:" + BarLijst.selectedCustomers);
        for (Customer selected : BarLijst.selectedCustomers) {
            int current = selected.getBalance();
            selected.setBalance(current + incrVal);
            System.out.println("Balance is now: " + selected.getBalance());
        }
        refreshTable();
    }

    private void refreshTable() {
        //update table visually
        BarTableModel barTableModel = (BarTableModel) this.table.getModel();
        Object[] columnNames = { "User id", "First name", "Last name", "Balance", "Selected" };
        Object[][] res = getCustomersObjectArray(BarLijst.customers);
        barTableModel.setDataVector(res, columnNames);
    }

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

    private void shutdownHook() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Writing to a file
            mapper.writeValue(new File("list.json"), BarLijst.customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}