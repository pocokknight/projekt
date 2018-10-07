package project.panels;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JPanel;
import project.Main;
import project.windows.WindowFirstSetup;

public class NewPanel extends JPanel {

    Main main;
    WindowFirstSetup wfs;
    String rowtype;
    Vector<NewPanelRow> rows;
    Vector<String[]> build;
        
    public NewPanel(Main main,WindowFirstSetup wfs) {
        this.main = main;
        this.wfs = wfs;
        rows = new Vector();
        build = new Vector();
    }

    public void addRow(String[] data){
        NewPanelRow npr = new NewPanelRow(main,build,data,true);
        rows.add(npr);
        setLayout(new GridLayout(0, 1, 20, 20));
        add(npr);
    }

    public void addRow(){
        String[] data = new String[build.size()];
        for (int i = 0; i < build.size(); i++) {
            switch (build.get(i)[1]) {
                case "varchar":
                    data[i] = "-";
                    break;
                case "int":
                    data[i] = "0";
                    break;
                case "double":
                    data[i] = "0.0";
                    break;
                default:
                    System.out.println("Hiba az új panel létrehozásánál! (ÚJ)");
                    break;
            }
        }
        NewPanelRow npr = new NewPanelRow(main,build,data,false);
        rows.add(npr);
        setLayout(new GridLayout(0, 1, 20, 20));
        add(npr);
        
        revalidate();
        repaint();
    }
    
    public void changeRowsType() {
        if(rowtype != null){
            changeRowsType(rowtype);
        }
    }
    
    public void changeRowsType(String s) {
        removeAll();
        rows.clear();
        rowtype = s;
        String ordername = "select column_name from information_schema.columns where table_name='";
        String table = "";
        switch (s) {
            case "Tanár":
                table = "tanar";
                break;
            case "Tantárgy":
                table = "tantargy";
                break;
            case "Tanév":
                table = "tanev";
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
        ordername += table + "'";
        Vector<String[]> vstnames = main.getDatabase().querry(ordername);
        String[] cols = new String[vstnames.size()];
        build = new Vector();
        for (int i = 0; i < vstnames.size(); i++) {
            cols[i] = vstnames.get(i)[0];
            String[] st = {
                vstnames.get(i)[0],
                main.getDatabase().querry("SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+table+"' AND COLUMN_NAME = '"+vstnames.get(i)[0]+"'").get(0)[0]
            };
            if(vstnames.get(i)[0].equals(table+"ID")){
                build.add(st);
            }else if(!(vstnames.get(i)[0].contains("ID"))){
                build.add(st);
            }
        }
        wfs.changeHeader(cols);
        
        Vector<String[]> vstdata = main.getDatabase().querry("SELECT * FROM `"+main.getDatabase().getDataBaseName()+"`.`"+table+"`");
        
        if(table.equals("terem") || table.equals("epuletresz")){
            for (int i = 0; i < vstdata.size(); i++) {
                vstdata.get(i)[1] = vstdata.get(i)[2];
            }
        }
        
        for (int i = 0; i < vstdata.size(); i++) {
            addRow(vstdata.get(i));
        }
        
        revalidate();
        repaint();
    }
}
