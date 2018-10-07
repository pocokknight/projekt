package project.panels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;

public class AddRemovePanel extends JPanel {

    int type;
    Vector<JTextField> tf;
    Vector<JComboBox> cb;
    Vector<JButton> b;

    int w, h;

    public AddRemovePanel() {
        this.type = 0;

        w = getSize().width;
        h = getSize().height;

        tf = new Vector();
        cb = new Vector();
        b = new Vector();
    }

    public void refresh() {
        for (int i = 0; i < b.size(); i++) {
            JPanel p = new JPanel();

            JTextField jtf = tf.get(i);
            add(p);
            p.add(jtf);
            jtf.setColumns(15);

            p = new JPanel();
            JComboBox jcb = cb.get(i);
            add(p);
            p.add(jcb);
            jcb.setSize(jcb.getMinimumSize());

            JButton jb = b.get(i);
            p = new JPanel();
            add(p);
            p.add(jb);
        }
    }

    public void addRow() {
        tf.add(new JTextField());
        cb.add(new JComboBox(cbm()));
        JButton but = new JButton("A sor törlése");
        b.add(but);
        but.addActionListener(new ALB());

        //System.out.println(tf.size());
        setLayout(new GridLayout(0, 3, 20, 20));
        JPanel p = new JPanel();

        JTextField jtf = tf.get(tf.size() - 1);
        add(p);
        p.add(jtf);
        jtf.setColumns(15);

        p = new JPanel();
        JComboBox jcb = cb.get(cb.size() - 1);
        add(p);
        p.add(jcb);
        jcb.setSize(jcb.getMinimumSize());

        p = new JPanel();
        add(p);
        p.add(but);

        revalidate();
        repaint();

    }

    ComboBoxModel cbm() {
        String[] st = {
            "Egész szám",
            "Tört szám-0.1",
            "Tört szám-0.2",
            "Tört szám-0.5",
            "Szöveg-1",
            "Szöveg-10",
            "Szöveg-50",
            "Szöveg-100",
            "Szöveg-500"
        };
        
//        |Egész szám|
//        |Tört szám||0.1|
//        |Tört szám||0.2|
//        |Tört szám||0.5|
//        |Szöveg||1|
//        |Szöveg||10|
//        |Szöveg||50|
//        |Szöveg||100|
//        |Szöveg||500|
        
        return new DefaultComboBoxModel(st);
    }

    public void setType(int i) {
        type = i;
        tf = new Vector();
        cb = new Vector();
        b = new Vector();

        removeAll();
        addRow();
        tf.get(0).setText("nev");
        cb.get(0).setSelectedItem("Szöveg-100");
    }

    public int getType() {
        return type;
    }

    public String[] getTypes() {
        String[] s;
        if (type == 0) {
            s = new String[cb.size() + 1];
            s[0] = "osztalyID";
            for (int i = 1; i < s.length; i++) {
                s[i] = (String) cb.get(i-1).getSelectedItem();
            }
        }else{
            s = new String[cb.size()];
            for (int i = 0; i < s.length; i++) {
                s[i] = (String) cb.get(i).getSelectedItem();
            }
        }
        return s;
    }

    public String[] getNames() {
        String[] s;
        if (type == 0) {
            s = new String[tf.size() + 1];
            s[0] = "Egész szám";
            for (int i = 1; i < s.length; i++) {
                s[i] = (String) tf.get(i-1).getText();
            }
        }else{
            s = new String[tf.size()];
            for (int i = 0; i < s.length; i++) {
                s[i] = (String) tf.get(i).getText();
            }
        }
        return s;
    }

    private class ALB implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < b.size(); i++) {
                if (e.getSource() == b.get(i)) {
                    removeAll();
                    b.remove(i);
                    cb.remove(i);
                    tf.remove(i);
                    refresh();
                    revalidate();
                    repaint();
                    return;
                }
            }
        }
    }
}
