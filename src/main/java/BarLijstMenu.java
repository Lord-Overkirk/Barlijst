import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BarLijstMenu extends JMenuBar {
    public JMenuBar jMenuBar;
    private Screen screen;

    public BarLijstMenu(Screen s) {
        this.screen = s;

        this.jMenuBar = new JMenuBar();
        this.jMenuBar.add(addSaveButton());
        this.jMenuBar.add(addIncreaseButton());
        this.jMenuBar.add(addDecreaseButton());
        this.jMenuBar.add(addIncreaseThirtyButton());

        // return jMenuBar;
    }

    private JButton addSaveButton() {
        Icon icon = UIManager.getIcon("FileView.floppyDriveIcon");
        JButton saveButton = new JButton(icon);

        saveButton.addActionListener(e -> shutdownHook());

        return saveButton;
    }

    private JButton addIncreaseButton() {
        JButton increaseButton = new JButton("Bier/Radler");

        increaseButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                BarLijstMenu.this.screen.increaseButtonPressed(-2);
            }
        });

        return increaseButton;
    }


    public JButton addDecreaseButton() {
        JButton decreaseButton = new JButton("Fris");

        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarLijstMenu.this.screen.increaseButtonPressed(-1);
            }
        });

        return decreaseButton;
    }

    public JButton addIncreaseThirtyButton() {
        JButton increaseThirtyButton = new JButton("Nieuwe barkaart");
        increaseThirtyButton.setBackground(Color.GREEN);
        increaseThirtyButton.setContentAreaFilled(false);
        increaseThirtyButton.setOpaque(true);
        // increaseThirtyButton.setBorderPainted(false);

        increaseThirtyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarLijstMenu.this.screen.increaseButtonPressed(30);
            }
        });

        return increaseThirtyButton;
    }

    public static void shutdownHook() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Writing to a file
            mapper.writeValue(new File("list.json"), BarLijst.customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
