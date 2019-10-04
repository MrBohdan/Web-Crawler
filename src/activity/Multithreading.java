package activity;

import static activity.Load_URLs_Activity.load_URLs;
import database.dbConnection;
import static database.dbConnection.create_Items_Table;
import static database.dbConnection.connect_SQLite;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Bohdan Skrypnyk
 */
class Load_URLs_Thread extends Thread{
    @Override
    public void run(){
        load_URLs();
    }
}

class Load_DataBase_Thread extends Thread{
    @Override
    public void run(){
        connect_SQLite();
        create_Items_Table();
    }
}

class Display_Items_Thread extends Thread{
    @Override
    public void run(){
        dbConnection con = new dbConnection();
        con.get_ByPrice_Items();
    }
}

public class Multithreading{
    public static void Run_Treads() throws InterruptedException{          
            Load_DataBase_Thread load_database = new Load_DataBase_Thread();
            load_database.start();
            
            TimeUnit.SECONDS.sleep(5);
            
            Load_URLs_Thread loadURLs_thread = new Load_URLs_Thread();
            loadURLs_thread.start();       
            
            Display_Items_Thread display_items = new Display_Items_Thread();
            display_items.start();
    }
    
}
