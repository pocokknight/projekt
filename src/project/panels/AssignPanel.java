package project.panels;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JPanel;
import project.Main;
import project.windows.WindowError;
import project.windows.WindowFirstSetup;

public class AssignPanel extends JPanel {

    Main main;
    WindowFirstSetup wfs;
    String[] table;
    String rowtype;
    Vector<AssignPanelRow> rows;
    Vector<Vector<String[]>> items;
        
    public AssignPanel(Main main,WindowFirstSetup wfs) {
        this.main = main;
        this.wfs = wfs;
        rows = new Vector();
    }
    
    public void addRow(String[] t,String s){
        AssignPanelRow apr = new AssignPanelRow(main,wfs,this,t,items,s);
        rows.add(apr);
        setLayout(new GridLayout(0, 1, 20, 20));
        add(apr);
        
        revalidate();
        repaint();
    }

    public void addRow(String s) {
        switch (s) {
            case "Tanárok által tanított tárgyak":
                addRow(null,null);
                break;
            case "Tanévben tartott órák":
                addRow(null,null);
                break;
            case "Osztályok diákjai":
                addRow(null,"diak");
                break;
            case "Épületek épületrészei":
                addRow(null,"epuletresz");
                break;
            case "Épületrészek osztálytermei":
                addRow(null,"terem");
                break;
            default:
                WindowError.showError(null, "Hiba a hozzárendelés generálásánál(2): "+s, "Hiba");
        }
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
        
        items = new Vector(); 
        
        switch (s) {
            case "Tanárok által tanított tárgyak":
                table = new String[]{"tanitotttargy","tanarID","tantargyID"};
                items.add(main.getDatabase().querry("SELECT tanarID, nev FROM `iskolai_adatbazis`.`tanar`"));
                items.add(main.getDatabase().querry("SELECT tantargyID, nev FROM `iskolai_adatbazis`.`tantargy`"));
                break;
            case "Tanévben tartott órák":
                table = new String[]{"targyevben","tanevID","tanitotttargyID"};
                items.add(main.getDatabase().querry("SELECT tanevID, megnevezes FROM `iskolai_adatbazis`.`tanev`"));
                items.add(main.getDatabase().querry("SELECT tanitotttargy.tanitotttargyID, CONCAT((SELECT tanar.nev FROM tanar WHERE tanar.tanarID = tanitotttargy.tanarID),' ',(SELECT tantargy.nev FROM tantargy WHERE tantargy.tantargyID = tanitotttargy.tantargyID)) AS 'megnevezes' FROM iskolai_adatbazis.tanar, iskolai_adatbazis.tantargy, iskolai_adatbazis.tanitotttargy GROUP BY tanitotttargy.tanitotttargyID"));
                break;
            case "Osztályok diákjai":
                table = new String[]{"diak","osztalyID"};
                items.add(main.getDatabase().querry("SELECT osztalyID, CONCAT(evfolyam,'/',megnevezes) FROM `iskolai_adatbazis`.`osztaly`"));
                break;
            case "Épületek épületrészei":
                table = new String[]{"epuletresz","epuletID"};
                items.add(main.getDatabase().querry("SELECT epuletID, megnevezes FROM `iskolai_adatbazis`.`epulet`"));
                break;
            case "Épületrészek osztálytermei":
                table = new String[]{"terem","epuletreszID"};
                items.add(main.getDatabase().querry("SELECT epuletreszID, megnevezes FROM `iskolai_adatbazis`.`epuletresz`"));
                break;
            default:
                WindowError.showError(null, "Hiba a hozzárendelés generálásánál: "+s, "Hiba");
        }
        
        String order = "SELECT * FROM "+main.getDatabase().getDataBaseName()+"."+table[0]+"";
        
        Vector<String[]> vst = main.getDatabase().querry(order);
        
        for (int i = 0; i < vst.size(); i++) {
            if(table[0].equals("tanitotttargy") || table[0].equals("targyevben")){
                addRow(vst.get(i),null);
            }else{
                addRow(vst.get(i),table[0]);
            }
        }
    }
}
