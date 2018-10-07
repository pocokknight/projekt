package project.windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import project.Main;
import project.panels.AssignPanel;
import project.panels.NewPanel;

public class WindowFirstSetup extends Window {

    JPanel mainPanel;
    JButton buttonNew, buttonAdd, buttonOk;
    JComboBox<String> select;

    JPanel newPanel, addPanel;

    JScrollPane spnew;
    JPanel header;
    NewPanel onSPnew;

    JScrollPane spadd;
    AssignPanel onSPadd;
    
    String selected;

    public String[] arraynew = {
        "Tanár",
        "Tantárgy",
        "Tanév",
        "Osztály",
        "Diák",
        "Épület",
        "Épületrész",
        "Terem"
    };

    public String[] arrayadd = {
        "Tanárok által tanított tárgyak",
        "Tanévben tartott órák",
        "Osztályok diákjai",
        "Épületek épületrészei",
        "Épületrészek osztálytermei"
    };

    public WindowFirstSetup(Main main) {
        super(main, Toolkit.getDefaultToolkit().getScreenSize().width / 3 * 2, Toolkit.getDefaultToolkit().getScreenSize().height / 3 * 2);
        frame.setLocationRelativeTo(null);
        frame.setTitle("FirstSetup");
        setupWindow();
        selected = arraynew[0];
    }

    public void renew() {
        onSPadd.changeRowsType();
        onSPnew.changeRowsType();
    }

    @Override
    protected void setupWindow() {
        super.setupWindow();

        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 4, 10, 10));
        frame.add(p, BorderLayout.SOUTH);

        select = new JComboBox<>(arraynew);
        select.addActionListener(new AL());
        p.add(select);

        buttonNew = new JButton("Új elem");
        buttonNew.addActionListener(new AL());
        p.add(buttonNew);

        onSPnew = new NewPanel(main,this);
        spnew = new JScrollPane(onSPnew);

        buttonAdd = new JButton("Elem hozzárendelése");
        buttonAdd.addActionListener(new AL());
        p.add(buttonAdd);
        
        buttonOk = new JButton("Kész");
        buttonOk.addActionListener(new AL());
        p.add(buttonOk);

        newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(1, 2, 10, 10));
        mainPanel.add(newPanel, "new");
        newPanel.add(spnew);

        onSPadd = new AssignPanel(main,this);
        spadd = new JScrollPane(onSPadd);
        addPanel = new JPanel(new BorderLayout());
        addPanel.add(spadd,BorderLayout.CENTER);
        mainPanel.add(addPanel, "add");
        
        spadd.getVerticalScrollBar().setUnitIncrement(15);
        spnew.getVerticalScrollBar().setUnitIncrement(15);
    }

    private void changePanel(String s) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, s);
    }

    public void changeHeader(String[] t){
        header = new JPanel(new GridLayout(1, t.length));
        for (int i = 0; i < t.length; i++) {
            if(!t[i].contains("ID"))
                header.add(new JLabel(t[i]));
        }
        //header.setSize(new Dimension(onSP.getWidth(), header.getHeight()));
        spnew.setColumnHeaderView(header);
    }
    
    boolean isNowNewPanel = true;

    private class AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == select) {
                selected = (String) select.getSelectedItem();
                if(isNowNewPanel){
                    onSPnew.changeRowsType(selected);
                    buttonNew.show(true);
                }else{
                    onSPadd.changeRowsType(selected);
                    switch ((String) select.getSelectedItem()) {
                        case "Tanárok által tanított tárgyak":
                            buttonNew.show(true);
                            break;
                        case "Tanévben tartott órák":
                            buttonNew.show(true);
                            break;
                        case "Osztályok diákjai":
                            buttonNew.show(false);
                            break;
                        case "Épületek épületrészei":
                            buttonNew.show(false);
                            break;
                        case "Épületrészek osztálytermei":
                            buttonNew.show(false);
                            break;
                    }
                }
            } else if (e.getSource() == buttonNew) {
                if (isNowNewPanel) {
                    onSPnew.addRow();
                }else{
                    onSPadd.addRow((String) select.getSelectedItem());
                }
            } else if (e.getSource() == buttonAdd) {
                if (isNowNewPanel) {
                    selected = arrayadd[0];
                    changePanel("add");
                    buttonAdd.setText("Új elemek létrehozása");
                    buttonNew.setText("Új hozzárendelés");
                    select.setModel(new DefaultComboBoxModel<>(arrayadd));
                    select.addActionListener(new AL());
                } else {
                    selected = arraynew[0];
                    changePanel("new");
                    buttonAdd.setText("Elem hozzárendelése");
                    buttonNew.setText("Új elem");
                    select.setModel(new DefaultComboBoxModel<>(arraynew));
                    select.addActionListener(new AL());
                }
                isNowNewPanel = !isNowNewPanel;
            }else if(e.getSource() == buttonOk){
                dispose();
                main.getWindowSystem().setVisible(true);
            }
        }
    }

    public String getSelected() {
        return selected;
    }

    public NewPanel getPanel() {
        return onSPnew;
    }

}
