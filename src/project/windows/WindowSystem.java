package project.windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import project.DataBase;
import project.Main;
import project.panels.AddRemovePanel;

public class WindowSystem extends Window{

    JPanel mainPanel;
    JPanel selectPanel,genPanel,setAtributesPanel;
            
    //selectPanel
    JButton gen, delete;
    
    //genpanel
    JButton student, teacher, generate;
    JButton back1;
    
    //setAtributesPanel
    JScrollPane sp;
    AddRemovePanel onSP;
    JButton back2, newRow, save;
    int type;
    
    public WindowSystem(Main main) {
        super(main, Toolkit.getDefaultToolkit().getScreenSize().width / 3*2, Toolkit.getDefaultToolkit().getScreenSize().height / 3*2);
        frame.setLocationRelativeTo(null);
        setQuiter(true);
        frame.setVisible(false);

        setupWindow();
    }

    @Override
    protected void setupWindow() {
        super.setupWindow();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        
        selectPanel = new JPanel();
        selectPanel.setLayout(new GridLayout(1, 2, 10, 10));
        mainPanel.add(selectPanel, "0");
        
        gen = new JButton("Adatbázis generálása");
        selectPanel.add(gen);
        gen.addActionListener(new AL());
        
        delete = new JButton("Adatbázis törlése");
        selectPanel.add(delete);
        delete.addActionListener(new AL());
        
        genPanel = new JPanel();
        genPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainPanel.add(genPanel, "1");
        
        student = new JButton("Diák adatainak beállítása");
        genPanel.add(student);
        student.addActionListener(new AL());
        
        teacher = new JButton("Tanár adatainak beállítása");
        genPanel.add(teacher);
        teacher.addActionListener(new AL());
        
        generate = new JButton("Generál");
        genPanel.add(generate);
        generate.addActionListener(new AL());
        
        back1 = new JButton("Vissza");
        genPanel.add(back1);
        back1.addActionListener(new AL());
        
        setAtributesPanel = new JPanel();
        setAtributesPanel.setLayout(new BorderLayout(10,10));
        mainPanel.add(setAtributesPanel, "2");
        
        onSP = new AddRemovePanel();
        sp = new JScrollPane(onSP);
        onSP.setLayout(null);
        
        setAtributesPanel.add(sp,BorderLayout.CENTER);
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 1, 10, 10));
        setAtributesPanel.add(p,BorderLayout.EAST);
        
        newRow = new JButton("Új Sor");
        p.add(newRow,BorderLayout.EAST);
        newRow.addActionListener(new AL());
        
        back2 = new JButton("Mentés nélkül vissza");
        p.add(back2,BorderLayout.EAST);
        back2.addActionListener(new AL());
        
        save = new JButton("Mentés");
        p.add(save,BorderLayout.EAST);
        save.addActionListener(new AL());
    }

    private void changePanel(int i){
        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, i+"");
    }
    
    private class AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == student) {
                changePanel(2);
                onSP.setType(0);
            }else if (e.getSource() == teacher) {
                changePanel(2);
                onSP.setType(1);
            }else if (e.getSource() == back1) {
                changePanel(0);
            }else if (e.getSource() == back2) {
                changePanel(1);
            }else if (e.getSource() == newRow) {
                onSP.addRow();
            }else if (e.getSource() == save) {
                DataBase db = main.getDatabase();
                if(onSP.getType() == 0){
                    db.setDiakTable(db.getTableString("diak", onSP.getNames(), onSP.getTypes()));
                }else if(onSP.getType() == 1){
                    db.setTanarTable(db.getTableString("tanar", onSP.getNames(), onSP.getTypes()));
                }
                changePanel(1);
            }else if (e.getSource() == gen) {
                changePanel(1);
            }else if (e.getSource() == generate) {                
                main.getDatabase().generateDataBase();
                dispose();
                main.getWindowFirstSetup().setVisible(true);
                main.getWindowFirstSetup().setQuiter(true);
                main.getWindowFirstSetup().getPanel().changeRowsType("Tanár");
            }else if(e.getSource() == delete){
                Object[] t = {"Törlés","Mégsem"};
                int i = WindowError.showQuestionMessage(frame, "Biztosan törölni szeretné az adatbázist? Ezzel az adatbázis és annak teljes tartalma elveszik!", t, "Figyelmeztetés");
                if(i == 0){
                    main.getDatabase().deleteAll();
                }
            }
        }
    }

}
