package project.windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import project.Main;

public class WindowLogin extends Window {

    JButton submit;
    JTextField username;
    JPasswordField password;
    
    public WindowLogin(Main main) {
        super(main, Toolkit.getDefaultToolkit().getScreenSize().width / 5, Toolkit.getDefaultToolkit().getScreenSize().height / 5);
        setupWindow();
    }

    @Override
    protected void setupWindow() {
        super.setupWindow();
        JPanel mpanel = new JPanel();
        mpanel.setLayout(new GridLayout(3, 1, 10, 10));
        frame.add(mpanel, BorderLayout.CENTER);
        
        submit = new JButton("Bejelentkezés");
        username = new JTextField();
        password = new JPasswordField();
        
        username.setToolTipText("Felhasználónév");
        //ezt ki kell törölni
        username.setText("root");
        password.setToolTipText("Jelszó");
        
        mpanel.add(username);
        mpanel.add(password);
        mpanel.add(submit);
        
        submit.addActionListener(new AL());
        
    }
    
    public void summon(JFrame f) {
        setVisible(true);
        frame.setLocationRelativeTo(f);
        setQuiter(false);
    }

    private class AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(main.getDatabase().connect(username.getText(), password.getText())){
                dispose();
                main.getWindowMain().dispose();
                main.getWindowSystem().setVisible(true);
            }
        }
    }

}
