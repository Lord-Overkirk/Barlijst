import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;
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
        JButton increaseButton = new JButton("+1");

        increaseButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                BarLijstMenu.this.screen.increaseButtonPressed(1);
            }
        });

        return increaseButton;
    }


    public JButton addDecreaseButton() {
        JButton decreaseButton = new JButton("-1");

        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarLijstMenu.this.screen.increaseButtonPressed(-1);
            }
        });

        return decreaseButton;
    }

    public JButton addIncreaseThirtyButton() {
        JButton increaseThirtyButton = new JButton("+30");

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
