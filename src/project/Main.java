package project;

import project.windows.*;

public class Main {

    private DataBase database;
    private WindowMain windowMain;
    private WindowLogin windowLogin;
    private WindowSystem windowSystem;
    private WindowFirstSetup windowFirstSetup;
    
    Main() {
        database = new DataBase(this);
        windowMain = new WindowMain(this);
        windowLogin = new WindowLogin(this);
        windowSystem = new WindowSystem(this);
        windowFirstSetup = new WindowFirstSetup(this);
    }

    public static void main(String[] args) {
        new Main();
    }

    public DataBase getDatabase() {
        return database;
    }

    public WindowMain getWindowMain() {
        return windowMain;
    }

    public WindowLogin getWindowLogin() {
        return windowLogin;
    }

    public WindowSystem getWindowSystem() {
        return windowSystem;
    }

    public WindowFirstSetup getWindowFirstSetup() {
        return windowFirstSetup;
    }

    
    
}
