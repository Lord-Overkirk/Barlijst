import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BarLijstMenu {
    
    public static JMenuBar BuildBarLijstMenu() {
        JMenuBar jMenuBar;

        jMenuBar = new JMenuBar();
        jMenuBar.add(addSaveButton());

        return jMenuBar;
    }

    private static JButton addSaveButton() {
        Icon icon = UIManager.getIcon("FileView.floppyDriveIcon");
        JButton saveButton = new JButton(icon);

        saveButton.addActionListener(e -> shutdownHook());

        return saveButton;
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
