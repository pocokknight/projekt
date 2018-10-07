package project.windows;

import java.awt.Toolkit;
import javax.swing.JPanel;
import project.Main;

public class WindowAdmin extends Window {

    JPanel mainPanel;
    JPanel selectPanel;

    public WindowAdmin(Main main) {
        super(main, Toolkit.getDefaultToolkit().getScreenSize().width / 3 * 2, Toolkit.getDefaultToolkit().getScreenSize().height / 3 * 2);
    }

}
