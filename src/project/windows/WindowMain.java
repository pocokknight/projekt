package project.windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import project.Main;

public class WindowMain extends Window {

    JButton setup, administration;

    public WindowMain(Main main) {
        super(main, Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        frame.setLocationRelativeTo(null);
        setQuiter(true);
        frame.setVisible(true);

        setupWindow();
    }

    @Override
    protected void setupWindow() {
        super.setupWindow();
        JPanel mpanel = new JPanel();
        mpanel.setLayout(new GridLayout(1, 2, 10, 10));
        frame.add(mpanel, BorderLayout.CENTER);

        setup = new JButton("Software beállítások");
        administration = new JButton("Adminisztrátori funkciók");

        mpanel.add(setup);
        mpanel.add(administration);

        setup.addActionListener(new AL());
        administration.addActionListener(new AL());

    }

    private class AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == setup) {
                main.getWindowLogin().summon(frame);
                //WindowError.showMessage(frame, "Kérem az adatbázis elkészítéséhez és beállításához egy olyan felhasználói fiókkal lépjen be aminek a jogköre lehetővé tesz minden műveletet.", "Információ");
            } else if (e.getSource() == administration) {
                System.out.println("Még nem támogatott funkció");
            }
        }
    }

}
