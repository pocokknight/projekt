package project.panels;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import project.DataBase;
import project.Main;

public class NewPanelRow extends JPanel {

    Main main;
    int id;
    boolean wasSaved = true;
    Vector<String[]> build;
    Vector<Container> parts;

    NewPanelRow(Main main, Vector<String[]> build, String[] data, boolean ws) {
        this.main = main;
        this.build = build;
        parts = new Vector();
        wasSaved = ws;
        if (!ws) {
            setBackground(Color.RED);
        }
        setLayout(new GridLayout(1, build.size() + 1, 20, 20));
        id = Integer.parseInt(data[0]);
        for (int i = 1; i < build.size(); i++) {
            String[] t = build.get(i);
            switch (t[1]) {
                case "varchar":
                    JTextField jtf = new JTextField(data[i]);
                    jtf.addActionListener(new AL2(this));
                    jtf.getDocument().addDocumentListener(new DL(this));
                    parts.add(jtf);
                    add(jtf);
                    break;
                case "int":
                    JSpinner jsp = new JSpinner(new SpinnerNumberModel(Integer.parseInt(data[i]), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
                    jsp.addChangeListener(new CL(this));
                    parts.add(jsp);
                    add(jsp);
                    break;
                case "double":
                    JSpinner jsp2 = new JSpinner(new SpinnerNumberModel(Double.parseDouble(data[i]), Integer.MIN_VALUE, Integer.MAX_VALUE, 0.01));
                    jsp2.addChangeListener(new CL(this));
                    parts.add(jsp2);
                    add(jsp2);
                    break;
                default:
                    System.out.println("Hiba az új panel létrehozásánál! " + t[0] + " " + t[1]);
                    break;
            }
        }
        JButton jb = new JButton("Mentés");
        add(jb);
        jb.addActionListener(new AL1(this));
        
        
        jb = new JButton("Törlés");
        add(jb);
        jb.addActionListener(new AL3(this));
    }

    public boolean isWasSaved() {
        return wasSaved;
    }

    public void setWasSaved(boolean wasSaved) {
        this.wasSaved = wasSaved;
    }

    private class AL1 implements ActionListener {

        NewPanelRow npr;

        AL1(NewPanelRow npr) {
            this.npr = npr;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!wasSaved) {
                wasSaved = true;
                newRowInsert();
            } else {
                rowUpdate();
            }
            npr.setBackground(Color.GREEN);
        }

        private void newRowInsert() {
            DataBase db = main.getDatabase();
            //INSERT INTO `iskolai_adatbazis`.`terem` (`epuletreszID`, `megnevezes`) VALUES ('30', '134A');
            String order = "INSERT INTO `" + db.getDataBaseName() + "`.`";
            switch (main.getWindowFirstSetup().getSelected()) {
                case "Tanár":
                    order += "tanar";
                    break;
                case "Tantárgy":
                    order += "tantargy";
                    break;
                case "Tanév":
                    order += "tanev";
                    break;
                case "Osztály":
                    order += "osztaly";
                    break;
                case "Diák":
                    order += "diak";
                    break;
                case "Épület":
                    order += "epulet";
                    break;
                case "Épületrész":
                    order += "epuletresz";
                    break;
                case "Terem":
                    order += "terem";
                    break;
            }
            order += "` (";

            for (int i = 1; i < build.size(); i++) {
                order += "`" + build.get(i)[0] + "`";
                if (i != build.size() - 1) {
                    order += ", ";
                }
            }

            order += ") VALUES (";

            for (int i = 0; i < parts.size(); i++) {
                String data;

                if (parts.get(i) instanceof JSpinner) {
                    data = "" + ((JSpinner) parts.get(i)).getValue();
                } else if (parts.get(i) instanceof JTextField) {
                    data = "" + ((JTextField) parts.get(i)).getText();
                } else {
                    data = "";
                }

                order += "'" + data + "'";
                if (i != parts.size() - 1) {
                    order += ", ";
                }
            }

            order += ");";

            //System.out.println(order);
            db.order(order);
        }

        private void rowUpdate() {
            DataBase db = main.getDatabase();
            //UPDATE `iskolai_adatbazis`.`terem` SET `epuletreszID`='301', `megnevezes`='134A+' WHERE  `teremID`=1;
            String order = "UPDATE `" + db.getDataBaseName() + "`.`";
            String table = "";
            switch (main.getWindowFirstSetup().getSelected()) {
                case "Tanár":
                    table = "tanar";
                    break;
                case "Tantárgy":
                    table = "tantargy";
                    break;
                case "Osztály":
                    table = "osztaly";
                    break;
                case "Diák":
                    table = "diak";
                    break;
                case "Épület":
                    table = "epulet";
                    break;
                case "Épületrész":
                    table = "epuletresz";
                    break;
                case "Terem":
                    table = "terem";
                    break;
            }
            order += table + "` SET ";

            for (int i = 1; i < build.size(); i++) {
                order += "`" + build.get(i)[0] + "`=";
                String data;

                if (parts.get(i-1) instanceof JSpinner) {
                    data = "" + ((JSpinner) parts.get(i-1)).getValue();
                } else if (parts.get(i-1) instanceof JTextField) {
                    data = "" + ((JTextField) parts.get(i-1)).getText();
                } else {
                    data = "";
                }

                order += "'" + data + "'";
                
                if (i != build.size() - 1) {
                    order += ", ";
                }
            }

            order += " WHERE `"+table+"ID`="+id+";";

            //System.out.println(order);
            db.order(order);
        }
    }

    private class AL2 implements ActionListener {

        NewPanelRow npr;

        AL2(NewPanelRow npr) {
            this.npr = npr;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (wasSaved) {
                npr.setBackground(Color.YELLOW);
            }
        }
    }

    private class AL3 implements ActionListener {

        NewPanelRow npr;

        AL3(NewPanelRow npr) {
            this.npr = npr;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            rowDelete();
        }
        
        private void rowDelete() {
            DataBase db = main.getDatabase();
            switch (main.getWindowFirstSetup().getSelected()) {
                case "Tanár":
                    db.deleteTeacher(id);
                    break;
                case "Tantárgy":
                    db.deleteSubject(id);
                    break;
                case "Osztály":
                    db.deleteClass(id);
                    break;
                case "Diák":
                    db.deleteStudent(id);
                    break;
                case "Épület":
                    db.deleteBuilding(id);
                    break;
                case "Épületrész":
                    db.deleteBuildingPart(id);
                    break;
                case "Terem":
                    db.deleteRoom(id);
                    break;
            }
            main.getWindowFirstSetup().renew();
        }
    }
    
    private class CL implements ChangeListener {

        NewPanelRow npr;

        CL(NewPanelRow npr) {
            this.npr = npr;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (wasSaved) {
                npr.setBackground(Color.YELLOW);
            }
        }
    }

    private class DL implements DocumentListener {

        NewPanelRow npr;

        DL(NewPanelRow npr) {
            this.npr = npr;
        }

        @Override
        public void insertUpdate(DocumentEvent de) {
            if (wasSaved) {
                npr.setBackground(Color.YELLOW);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            if (wasSaved) {
                npr.setBackground(Color.YELLOW);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            if (wasSaved) {
                npr.setBackground(Color.YELLOW);
            }
        }

    }

}
