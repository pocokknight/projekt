package project.windows;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import project.Main;

public class Window {

    protected Main main;
    protected JFrame frame;

    protected int width, height;

    public Window(Main main, int width, int height) {
        this.main = main;
        frame = new JFrame();
        frame.setSize(width, height);
    }

    protected void setupWindow() {
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(new JPanel(), BorderLayout.EAST);
        frame.add(new JPanel(), BorderLayout.NORTH);
        frame.add(new JPanel(), BorderLayout.SOUTH);
        frame.add(new JPanel(), BorderLayout.WEST);
    }

    public void setQuiter(boolean b) {
        if (b) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    public void dispose() {
        frame.setVisible(false);
        frame.dispose();
    }

}
