package project.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import project.DataBase;
import project.Main;
import project.windows.WindowFirstSetup;

public class AssignPanelRow extends JPanel {

    Main main;
    WindowFirstSetup wfs;
    AssignPanel ap;
    int id;
    Vector<Container> parts;
    String[] t;
    Vector<Vector<String[]>> items;
    boolean wassave = false;
    boolean canChange = false;
    String name;
    String colid;

    AssignPanelRow(Main main, WindowFirstSetup wfs, AssignPanel ap, String[] t, Vector<Vector<String[]>> items, String name) {
        //System.out.println(name);
        this.main = main;
        this.wfs = wfs;
        this.ap = ap;
        this.t = t;
        this.items = items;
        this.name = name;
        if (t == null) {
            this.id = 0;
        } else {
            this.id = Integer.parseInt(t[0]);
        }

        parts = new Vector();

        if (name == null) {
            if (t == null) { //uj
                if (items.size() == 2) {
                    setLayout(new GridLayout(1, 3));
                    for (int i = 0; i < items.size(); i++) {
                        Vector<String> vec = new Vector();
                        for (int j = 0; j < items.get(i).size(); j++) {
                            vec.add(items.get(i).get(j)[1]);
                        }
                        JComboBox<String> cb = new JComboBox(vec);
                        cb.addActionListener(new AL());
                        parts.add(cb);
                        add(cb);
                        addNew(Integer.parseInt(items.get(0).get(0)[0]), Integer.parseInt(items.get(1).get(0)[0]));
                    }
                }
            } else {//meglévő
                wassave = true;
                setLayout(new GridLayout(1, items.size() + 1));
                for (int i = 0; i < items.size(); i++) {
                    Vector<String> vec = new Vector();
                    for (int j = 0; j < items.get(i).size(); j++) {
                        vec.add(items.get(i).get(j)[1]);
                    }
                    JComboBox<String> cb = new JComboBox(vec);
                    cb.addActionListener(new AL());
                    parts.add(cb);
                    add(cb);
                    cb.setSelectedItem(getNameByID(Integer.parseInt(t[i + 1]), i));
                }
            }

            JButton b = new JButton("Törlés");
            b.addActionListener(new AL3(this));
            add(b);

        } else {
            String labelname = "";
            switch (name) {
                case "diak":
                    colid = "osztalyID";
                    labelname = t[1];
                    break;
                case "epuletresz":
                    colid = "epuletID";
                    labelname = t[2];
                    break;
                case "terem":
                    colid = "epuletreszID";
                    labelname = t[2];
                    break;
                default:
                    System.err.println("Hiba AssignPanelRow");
            }

            JLabel label = new JLabel(labelname);
            parts.add(label);
            add(label);

            Vector<String> vs = new Vector();

            if (t.length > 3) {
                vs.add("Nincs osztálya");
            }

            for (int i = 0; i < items.get(0).size(); i++) {
                vs.add(items.get(0).get(i)[1]);
            }

            JComboBox<String> cb = new JComboBox(vs);
            try {
                cb.setSelectedItem(getNameByID(Integer.parseInt(t[1]), 0));
            } catch (Exception e) {
                if (t[5] == null) {
                    cb.setSelectedIndex(0);
                } else {
                    cb.setSelectedItem(getNameByID(Integer.parseInt(t[5]), 0));
                }
            }
            cb.addActionListener(new AL2());
            parts.add(cb);
            add(cb);

        }

        canChange = true;
    }

    void addNew(int a, int b) {
        //INSERT INTO `iskolai_adatbazis`.`targyevben` (`tanevID`, `tanitotttargyID`) VALUES ('6', '1');
        String order = "INSERT INTO ";
        String x = main.getWindowFirstSetup().getSelected();
        switch (x) {
            case "Tanárok által tanított tárgyak":
                order += "`iskolai_adatbazis`.`tanitotttargy` (`tanarID`, `tantargyID`) VALUES ";
                break;
            case "Tanévben tartott órák":
                order += "`iskolai_adatbazis`.`targyevben` (`tanevID`, `tanitotttargyID`) VALUES ";
                break;
            default:
                return;
        }
        order += "('" + a + "','" + b + "');";
        //System.out.println(order);
        main.getDatabase().order(order);
        Vector<String[]> vst = null;
        switch (x) {
            case "Tanárok által tanított tárgyak":
                vst = main.getDatabase().querry("SELECT MAX(tanitotttargyID) FROM tanitotttargy ORDER BY tanitotttargyID DESC");
                break;
            case "Tanévben tartott órák":
                vst = main.getDatabase().querry("SELECT MAX(targyevbenID) FROM targyevben ORDER BY targyevbenID DESC");
                break;
        }

        id = Integer.parseInt(vst.get(0)[0]);
    }

    String getNameByID(int id, int on) {
        for (int i = 0; i < items.get(on).size(); i++) {
            if (Integer.parseInt(items.get(on).get(i)[0]) == id) {
                return items.get(on).get(i)[1];
            }
        }
        return null;
    }

    int getIDByName(String name, int on) {
        for (int i = 0; i < items.get(on).size(); i++) {
            if (items.get(on).get(i)[1].equals(name)) {
                return Integer.parseInt(items.get(on).get(i)[0]);
            }
        }
        return -1;
    }

    private class AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //UPDATE `iskolai_adatbazis`.`targyevben` SET `tanevID`='2', `tanitotttargyID`='3' WHERE  `targyevbenID`=1;
            if (canChange) {
                String order = "UPDATE ";
                String x = main.getWindowFirstSetup().getSelected();
                switch (x) {
                    case "Tanárok által tanított tárgyak":
                        order += "`iskolai_adatbazis`.`tanitotttargy` SET `tanarID`='" + getIDByName((String) (((JComboBox) parts.get(0)).getSelectedItem()), 0) + "', `tantargyID`='" + getIDByName((String) (((JComboBox) parts.get(1)).getSelectedItem()), 1) + "' WHERE  `tanitotttargyID`=" + id + ";";
                        break;
                    case "Tanévben tartott órák":
                        order += "`iskolai_adatbazis`.`targyevben` SET `tanevID`='" + getIDByName((String) (((JComboBox) parts.get(0)).getSelectedItem()), 0) + "', `tanitotttargyID`='" + getIDByName((String) (((JComboBox) parts.get(1)).getSelectedItem()), 1) + "' WHERE  `targyevbenID`=" + id + ";";
                        break;
                    case "Osztályok diákjai":
                        break;
                    case "Épületek épületrészei":
                        break;
                    case "Épületrészek osztálytermei":
                        break;
                }
                //System.out.println(order);
                main.getDatabase().order(order);
            }
        }

    }

    private class AL2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //UPDATE `iskolai_adatbazis`.`diak` SET `osztalyID`='0' WHERE  `diakID`=6;
            if (canChange) {
                if (((JComboBox) (parts.get(1))).getSelectedItem().equals("Nincs osztálya")) {
                    main.getDatabase().order("UPDATE `iskolai_adatbazis`.`" + name + "` SET `" + colid + "`= NULL WHERE  `" + name + "ID`=" + id + ";");
                } else {
                    main.getDatabase().order("UPDATE `iskolai_adatbazis`.`" + name + "` SET `" + colid + "`='" + getIDByName((String) ((JComboBox) parts.get(1)).getSelectedItem(), 0) + "' WHERE  `" + name + "ID`=" + id + ";");
                }
            }
        }

    }

    private class AL3 implements ActionListener {

        AssignPanelRow apr;

        AL3(AssignPanelRow apr) {
            this.apr = apr;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            rowDelete();
        }

        private void rowDelete() {
            DataBase db = main.getDatabase();
            switch (main.getWindowFirstSetup().getSelected()) {
                case "Tanárok által tanított tárgyak":
                    db.deleteLessonTeacher(id);
                    break;
                case "Tanévben tartott órák":
                    db.deleteLessonInYear(id);
                    break;
            }
            main.getWindowFirstSetup().renew();
        }
    }

}
