package project;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import project.windows.WindowError;

public class DataBase {

    private Main main;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private String username, password, dataBaseName, dataBasePlace;
    private String diakTable, tanarTable;

    public DataBase(Main m) {
        main = m;
        dataBaseName = "iskolai_adatbazis";
        dataBasePlace = "localhost";

        diakTable = "CREATE TABLE IF NOT EXISTS `diak` (\n"
                + "  `diakID` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `osztalyID` int(11) ,\n"
                + "  `nev` varchar(64) COLLATE utf8_hungarian_ci NOT NULL,\n"
                + "  PRIMARY KEY (`diakID`)\n"
                + ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
        tanarTable = "CREATE TABLE IF NOT EXISTS `tanar` (\n"
                + "  `tanarID` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `nev` varchar(64) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                + "  PRIMARY KEY (`tanarID`)\n"
                + ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
    }

    public Vector<String[]> querry(String order) {
        Vector<String[]> v = new Vector();
        try {
            resultSet = statement.executeQuery(order);
            metaData = resultSet.getMetaData();

            while (resultSet.next()) {
                String[] t = new String[metaData.getColumnCount()];
                for (int i = 0; i < t.length; i++) {
                    t[i] = resultSet.getString(i + 1);
                }
                v.add(t);
            }
        } catch (Exception ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba a lekérdezés közben");
        }
        return v;
    }

    public void order(String order){
        try {
            statement.execute(order);
        } catch (SQLException e) {
            WindowError.showError(null, order+" --- "+e.getMessage(), "Hiba a lekérdezésnél!");
        }
    }
    
    public void deleteSubject(int id){
        Vector<String[]> vst = querry("SELECT tanitotttargyID FROM `" + dataBaseName + "`.`tanitotttargy` WHERE tantargyID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLessonTeacher(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`tantargy` WHERE `tantargyID`="+id+";");
    }
    
    public void deleteTeacher(int id){
        Vector<String[]> vst = querry("SELECT tanitotttargyID FROM `" + dataBaseName + "`.`tanitotttargy` WHERE tanarID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLessonTeacher(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`tanar` WHERE `tanarID`="+id+";");
    }
    
    public void deleteLessonTeacher(int id){
        Vector<String[]> vst = querry("SELECT targyevbenID FROM `" + dataBaseName + "`.`targyevben` WHERE tanitotttargyID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLessonInYear(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`tanitotttargy` WHERE `tanitotttargyID`="+id+";");
    }
    
    public void deleteYear(int id){
        Vector<String[]> vst = querry("SELECT targyevbenID FROM `" + dataBaseName + "`.`targyevben` WHERE tanevID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLessonInYear(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`tanev` WHERE `tanevID`="+id+";");
    }
    
    public void deleteLessonInYear(int id){
        Vector<String[]> vst = querry("SELECT tanoraID FROM `" + dataBaseName + "`.`tanora` WHERE targyevbenID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLesson(Integer.parseInt(vst.get(i)[0]));
        }
        vst = querry("SELECT jegyID FROM `" + dataBaseName + "`.`jegy` WHERE targyevbenID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteGrade(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`targyevben` WHERE `targyevbenID`="+id+";");
    }
    
    public void deleteClass(int id){
        order("UPDATE `" + dataBaseName + "`.`diak` SET `osztalyID`=NULL WHERE `osztalyID`="+id+";");
        order("DELETE FROM `" + dataBaseName + "`.`osztaly` WHERE `osztalyID`="+id+";");
    }
    
    public void deleteStudent(int id){
        Vector<String[]> vst = querry("SELECT diakokazoranID FROM `" + dataBaseName + "`.`diakokazoran` WHERE diakID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLesson(Integer.parseInt(vst.get(i)[0]));
        }
        vst = querry("SELECT jegyID FROM `" + dataBaseName + "`.`jegy` WHERE diakID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteGrade(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`diak` WHERE `diakID`="+id+";");
    }
    
    public void deleteLesson(int id){
        Vector<String[]> vst = querry("SELECT diakokazoranID FROM `" + dataBaseName + "`.`diakokazoran` WHERE tanoraID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteStudentOnLesson(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`tanora` WHERE `tanoraID`="+id+";");
    }

    public void deleteGrade(int id){
        order("DELETE FROM `" + dataBaseName + "`.`jegy` WHERE `jegyID`="+id+";");
    }
    
    public void deleteStudentOnLesson(int id){
        order("DELETE FROM `" + dataBaseName + "`.`diakokazoran` WHERE `diakokazoranID`="+id+";");
    }
    
    public void deleteRoom(int id){
        Vector<String[]> vst = querry("SELECT tanoraID FROM `" + dataBaseName + "`.`tanora` WHERE teremID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteLesson(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`terem` WHERE `teremID`="+id+";");
    }
    
    public void deleteBuildingPart(int id){
        Vector<String[]> vst = querry("SELECT teremID FROM `" + dataBaseName + "`.`terem` WHERE epuletreszID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteRoom(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`epuletresz` WHERE `epuletreszID`="+id+";");
    }
    
    public void deleteBuilding(int id){
        Vector<String[]> vst = querry("SELECT epuletreszID FROM `" + dataBaseName + "`.`epuletresz` WHERE epuletID="+id+";");
        for (int i = 0; i < vst.size(); i++) {
            deleteBuildingPart(Integer.parseInt(vst.get(i)[0]));
        }
        order("DELETE FROM `" + dataBaseName + "`.`epulet` WHERE `epuletID`="+id+";");
    }
    
    public boolean connect(String un, String pw) {
        try {
            username = un;
            password = pw;
            connection = DriverManager.getConnection("jdbc:mysql://" + dataBasePlace + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=utf8&characterSetResult=utf8");
            statement = connection.createStatement();
        } catch (SQLException ex) {
            WindowError.showError(null, "Nem sikerült csatlakozni az adatbázishoz!", "Hiba a csatlakozásnál");
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba a lecsatlakozás közben");
        }
    }

    private String getMD5(String s) {
        try {
            String plaintext = s;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba a titkosítás közben");
        }
        return null;
    }

    public String getTableString(String tablanev, String[] names, String[] types) {
        String s = "CREATE TABLE IF NOT EXISTS `" + tablanev + "` ( `" + tablanev + "ID` int(11) NOT NULL AUTO_INCREMENT, ";
        //System.out.println(names.length+" "+types.length);
        for (int i = 0; i < types.length; i++) {
            String[] st = types[i].split("-");
            if(st[0].equals("Egész szám")){
                    s += " `" + names[i] + "` int(11), ";
            }else if(st[0].equals("Tört szám")){
                    //System.out.println(st[1]);
                    s += " `" + names[i] + "` double(11," + st[1].substring(2) + "), ";
            }else if(st[0].equals("Szöveg")){
                    s += " `" + names[i] + "` varchar(" + st[1] + "),";
            }
        }
        if(tablanev.equals("diak")){
            s += " `osztalyID` int(11), ";
        }
        s += "  PRIMARY KEY (`" + tablanev + "ID`)) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;";
        System.out.println(s);
        return s;
    }

    public void generateDataBase() {
        try {
            String databaseGenerateCode = "CREATE DATABASE IF NOT EXISTS `" + dataBaseName + "` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_hungarian_ci */; ";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "USE `" + dataBaseName + "`;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = diakTable;
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `diakokazoran` (\n"
                    + "  `diakokazoranID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `diakID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `tanoraID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `megjelent` varchar(1) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`diakokazoranID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `epulet` (\n"
                    + "  `epuletID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `megnevezes` varchar(100) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`epuletID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `epuletresz` (\n"
                    + "  `epuletreszID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `epuletID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `megnevezes` varchar(100) COLLATE utf8_hungarian_ci DEFAULT NULL,\n"
                    + "  PRIMARY KEY (`epuletreszID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `felhasznalo` (\n"
                    + "  `felhasznaloID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `tanarID` int(11) DEFAULT '0',\n"
                    + "  `diakID` int(11) DEFAULT '0',\n"
                    + "  `felhasznalonev` varchar(100) COLLATE utf8_hungarian_ci DEFAULT '0',\n"
                    + "  `jelszo` varchar(32) COLLATE utf8_hungarian_ci DEFAULT '0',\n"
                    + "  PRIMARY KEY (`felhasznaloID`)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `jegy` (\n"
                    + "  `jegyID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `diakID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `targyevbenID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `ertek` int(11) NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`jegyID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `osztaly` (\n"
                    + "  `osztalyID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `evfolyam` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `megnevezes` varchar(50) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`osztalyID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = tanarTable;
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `tanev` (\n"
                    + "  `tanevID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `megnevezes` varchar(50) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`tanevID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `tanitotttargy` (\n"
                    + "  `tanitotttargyID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `tanarID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `tantargyID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`tanitotttargyID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `tanora` (\n"
                    + "  `tanoraID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `targyevbenID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `teremID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `idopont` datetime NOT NULL,\n"
                    + "  PRIMARY KEY (`tanoraID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `tantargy` (\n"
                    + "  `tantargyID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `nev` varchar(64) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`tantargyID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `targyevben` (\n"
                    + "  `targyevbenID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `tanevID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `tanitotttargyID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`targyevbenID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `terem` (\n"
                    + "  `teremID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `epuletreszID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `megnevezes` varchar(100) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`teremID`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;\n";
            statement.execute(databaseGenerateCode);
            databaseGenerateCode = "CREATE TABLE IF NOT EXISTS `uzenet` (\n"
                    + "  `uzenetID` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `feladofelhasznaloID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `cimzettfelhasznaloID` int(11) NOT NULL DEFAULT '0',\n"
                    + "  `tartalom` varchar(1000) COLLATE utf8_hungarian_ci NOT NULL DEFAULT '0',\n"
                    + "  `feldolgozva` int(11) NOT NULL DEFAULT '0',\n"
                    + "  PRIMARY KEY (`uzenetID`)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;";
            statement.execute(databaseGenerateCode);
            generateRoles();

            WindowError.showMessage(null, "Az adatbázis generálása kész!", "Információ");
        } catch (Exception ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba az adatbázis készítése közben");
        }
    }

    private void generateRoles() {
        try {
            statement.execute("CREATE ROLE IF NOT EXISTS 'admin', 'tanar', 'diak';");
            //admin
            statement.execute("GRANT ALL ON " + dataBaseName + ".* TO 'admin';");
            //tanar
            statement.execute("GRANT SELECT ON " + dataBaseName + ".* TO 'tanar';");
            statement.execute("GRANT INSERT, UPDATE, DELETE ON " + dataBaseName + ".jegy TO 'tanar';");
            statement.execute("GRANT INSERT, UPDATE, DELETE ON " + dataBaseName + ".tanora TO 'tanar';");
            statement.execute("GRANT INSERT, UPDATE, DELETE ON " + dataBaseName + ".diakokazoran TO 'tanar';");
            statement.execute("GRANT INSERT, UPDATE ON " + dataBaseName + ".uzenet TO 'tanar';");
            //diak
            statement.execute("GRANT SELECT ON " + dataBaseName + ".* TO 'diak';");
            statement.execute("GRANT INSERT, UPDATE ON " + dataBaseName + ".uzenet TO 'diak';");
        } catch (SQLException ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba a jogok generálása közben");
        }
    }

    private void createAccount(String username, String password, int userPrivileges) {
        try {
            //ellenőrizni hogy van e már ilyen nevű felhasználó
            Vector<String[]> v = querry("SELECT felhasznalonev FROM felhasznalo");
            for (int i = 0; i < v.size(); i++) {
                if (v.get(i)[0].equals(username)) {
                    return;
                }
            }

            statement.execute("INSERT INTO `" + dataBaseName + "`.`felhasznalo` (`felhasznalonev`, `jelszo`) VALUES ('" + username + "', '" + getMD5(password) + "');");
            statement.execute("CREATE USER '" + username + "'@'" + dataBasePlace + "' IDENTIFIED BY '" + password + "';");
            if (userPrivileges == 0) {
                statement.execute("GRANT `admin` TO '" + username + "'@'" + dataBasePlace + "'");
            } else if (userPrivileges == 1) {
                statement.execute("GRANT `tanar` TO '" + username + "'@'" + dataBasePlace + "'");
            } else {
                statement.execute("GRANT `diak` TO '" + username + "'@'" + dataBasePlace + "'");
            }

            //System.out.println("CREATE USER '" + username + "'@'" + dataBasePlace + "' IDENTIFIED BY '" + password + "';");
        } catch (Exception ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba " + username + " felhasználó készítése közben");
        }
    }
    
    public void deleteAll() {
        try {

            statement.execute("USE `" + dataBaseName + "`");
            Vector<String[]> v = querry("SELECT felhasznalonev FROM felhasznalo");

            for (int i = 0; i < v.size(); i++) {
                statement.execute("DROP USER IF EXISTS '" + v.get(i)[0] + "'@'" + dataBasePlace + "'");
            }

            statement.execute("DROP DATABASE " + dataBaseName + ";");
            statement.execute("DROP ROLE 'admin', 'tanar', 'diak';");
            WindowError.showMessage(null, "Adatbázis törölve", "Információ");
        } catch (Exception ex) {
            WindowError.showError(null, ex.getMessage(), "Hiba az adatbázis törlése közbem");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public ResultSetMetaData getMetaData() {
        return metaData;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public String getDataBasePlace() {
        return dataBasePlace;
    }

    public String getDiakTable() {
        return diakTable;
    }

    public void setDiakTable(String diakTable) {
        this.diakTable = diakTable;
    }

    public String getTanarTable() {
        return tanarTable;
    }

    public void setTanarTable(String tanarTable) {
        this.tanarTable = tanarTable;
    }

}
