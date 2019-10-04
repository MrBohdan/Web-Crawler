package activity;

import database.dbConnection;
import static database.dbConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 *  @author Bohdan Skrypnyk
 */
public class Display_Items_Activity {

    private static final Display_Items_Activity display_items_activity = new Display_Items_Activity();
    private static dbConnection db = new dbConnection();

    public void select_By_Query() {
        Scanner select = new Scanner(System.in);
        System.out.print("\n--------------Select--------- \n| "
                + ">[1]Dispay All Items\n| "
                + ">[2]Dispay By Price\n| "
                + ">[3]Dispay By Style\n| "
                + ">[4]Dispay By Climate\n| "
                + ">>>");
        String input = select.nextLine();
        if ("1".equals(input)) {
            db.get_All_Items();
        }
        if ("2".equals(input)) {
            db.get_ByPrice_Items();
        }
        if ("3".equals(input)) {
            db.get_ByStyle_Items();
        }
        if ("4".equals(input)) {
            db.get_ByClimate_Items();
        }
        if (!"4".equals(input) || !"3".equals(input) || !"2".equals(input) || !"1".equals(input) || input.isEmpty()) {
            select_By_Query();
        }
    }

    public static void display_Items(ResultSet item) {
        try {
            // loop through the result set
            while (item.next()) {
                System.out.println("\n" + "=------------------------------------------=");
                System.out.println("Name : " + item.getString(COLUMN_NAME));
                System.out.println("Price : " + item.getDouble(COLUMN_PRICE));
                Double price_check = item.getDouble(COLUMN_UPPER_PRICE);
                if (price_check != 0.0) {
                    System.out.println("Upper Price : " + item.getDouble(COLUMN_UPPER_PRICE));
                }
                System.out.println("Description : " + item.getString(COLUMN_DESCRIPTION));

                if (!item.getString(COLUMN_TRAILERS_DESCRIPTION).equals("")) {
                    System.out.println("Trailers Description : " + item.getString(COLUMN_TRAILERS_DESCRIPTION));
                }

                if (!item.getString(COLUMN_MATERIAL).equals("")) {
                    System.out.println("Material : " + item.getString(COLUMN_MATERIAL));
                }

                if (!item.getString(COLUMN_PATTERN).equals("")) {
                    System.out.println("Pattern : " + item.getString(COLUMN_PATTERN));
                }

                if (!item.getString(COLUMN_CLIMATE).equals("")) {
                    System.out.println("Climate : " + item.getString(COLUMN_CLIMATE));
                }

                if (!item.getString(COLUMN_ACTIVITY).equals("")) {
                    System.out.println("Activity : " + item.getString(COLUMN_ACTIVITY));
                }

                if (!item.getString(COLUMN_GENDER).equals("")) {
                    System.out.println("Gender : " + item.getString(COLUMN_GENDER));
                }

                if (!item.getString(COLUMN_FORMAT).equals("")) {
                    System.out.println("Format : " + item.getString(COLUMN_FORMAT));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        display_items_activity.select_By_Query();
    }
}
