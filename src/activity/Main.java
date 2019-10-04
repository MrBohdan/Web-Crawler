package activity;

import static activity.Multithreading.Run_Treads;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Bohdan Skrypnyk
 */
public class Main {
    public static void main(String[] args)  {
        try {
            Run_Treads();
        } catch (InterruptedException ex) {
            System.out.print("Ex : "+ex);
        }
    }
}


