package database;

import activity.Display_Items_Activity;
import static activity.Display_Items_Activity.display_Items;
import java.io.File;
import java.sql.*; 
import model.Item;

/**
 *
 * @author @author Bohdan Skrypnyk
 */
public class dbConnection {
    // SQLite connection string
    public static final String url = "jdbc:sqlite:src\\item.db";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_UPPER_PRICE = "upper_price";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TRAILERS_DESCRIPTION = "trailers_description";
    public static final String COLUMN_STYLE = "style";
    public static final String COLUMN_MATERIAL = "material";
    public static final String COLUMN_PATTERN = "pattern";
    public static final String COLUMN_CLIMATE = "climate";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_FORMAT = "format";
    private static final String COLUMN_URL = "url";

    private static final File file = new File("src\\item.db");
    private static final Display_Items_Activity display_items_activity = new Display_Items_Activity();

    public static String values_ByPrice_Query, values_ByALL_Query, values_ByStyle_Query, values_ByClimate_Query;

    public static void connect_SQLite() {
        Connection conn = null;
        try {
            if (!file.exists()) {
                System.out.println("\n\n>>>> It's the first run, database will be fully updated during few hours\n");
            }
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println(">>>> Connection to SQLite has been established.\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void create_Items_Table() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS items (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    name VARCHAR(45) NOT NULL,\n"
                + "    price DOUBLE NOT NULL,\n"
                + "    upper_price DOUBLE ,\n"
                + "    description TEXT NOT NULL,\n"
                + "    trailers_description VARCHAR(255) ,\n"
                + "    style VARCHAR(45),\n"
                + "    material VARCHAR(45),\n"
                + "    pattern VARCHAR(45),\n"
                + "    climate VARCHAR(45),\n"
                + "    activity VARCHAR(45),\n"
                + "    gender VARCHAR(45),\n"
                + "    format VARCHAR(45),\n"
                + "    url VARCHAR(450)\n"
                + ");";
        //create table if not exist
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert_Items(Item item) {
        // Insertion of the records to the database (Query)
        String values_Query = "INSERT OR REPLACE INTO items(" + COLUMN_NAME + ","
                + COLUMN_PRICE + ","
                + COLUMN_UPPER_PRICE + ","
                + COLUMN_DESCRIPTION + ","
                + COLUMN_TRAILERS_DESCRIPTION + ","
                + COLUMN_STYLE + ","
                + COLUMN_MATERIAL + ","
                + COLUMN_PATTERN + ","
                + COLUMN_CLIMATE + ","
                + COLUMN_ACTIVITY + ","
                + COLUMN_GENDER + ","
                + COLUMN_FORMAT + ","
                + COLUMN_URL + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(values_Query)) {
            pstmt.setString(1, item.getName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setDouble(3, item.getUpper_price());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getTrailers_description());
            pstmt.setString(6, item.getStyle());
            pstmt.setString(7, item.getMaterial());
            pstmt.setString(8, item.getPattern());
            pstmt.setString(9, item.getClimate());
            pstmt.setString(10, item.getActivity());
            pstmt.setString(11, item.getGender());
            pstmt.setString(12, item.getFormat());
            pstmt.setString(13, item.getUrl());
            pstmt.executeUpdate();

            delete_Dublicates();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void get_Items() {
        // Selection of the records from the database (Query)
        values_ByALL_Query = "SELECT " + COLUMN_NAME + ","
                + COLUMN_PRICE + ","
                + COLUMN_UPPER_PRICE + ","
                + COLUMN_DESCRIPTION + ","
                + COLUMN_TRAILERS_DESCRIPTION + ","
                + COLUMN_STYLE + ","
                + COLUMN_MATERIAL + ","
                + COLUMN_PATTERN + ","
                + COLUMN_CLIMATE + ","
                + COLUMN_ACTIVITY + ","
                + COLUMN_GENDER + ","
                + COLUMN_FORMAT + " FROM items";

        boolean alreadyExecuted = false;

        if (!alreadyExecuted) {
            try (Connection conn = this.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(values_ByALL_Query)) {

               display_Items(rs);
               
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            alreadyExecuted = true;
        }

        display_items_activity.select_By_Query();
    }

    public void get_All_Items() {
        // Insertion of the records to the database (Query)
         values_ByALL_Query = "SELECT " + COLUMN_NAME + ","
                + COLUMN_PRICE + ","
                + COLUMN_UPPER_PRICE + ","
                + COLUMN_DESCRIPTION + ","
                + COLUMN_TRAILERS_DESCRIPTION + ","
                + COLUMN_STYLE + ","
                + COLUMN_MATERIAL + ","
                + COLUMN_PATTERN + ","
                + COLUMN_CLIMATE + ","
                + COLUMN_ACTIVITY + ","
                + COLUMN_GENDER + ","
                + COLUMN_FORMAT + " FROM items";
         
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(values_ByALL_Query)) {
            
            display_Items(rs);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        display_items_activity.select_By_Query();
    }

    public void get_ByPrice_Items() {
        // Selection of the records from the database by price (Query)
        values_ByPrice_Query = "SELECT " + COLUMN_NAME + ","
                + COLUMN_PRICE + ","
                + COLUMN_UPPER_PRICE + ","
                + COLUMN_DESCRIPTION + ","
                + COLUMN_TRAILERS_DESCRIPTION + ","
                + COLUMN_STYLE + ","
                + COLUMN_MATERIAL + ","
                + COLUMN_PATTERN + ","
                + COLUMN_CLIMATE + ","
                + COLUMN_ACTIVITY + ","
                + COLUMN_GENDER + ","
                + COLUMN_FORMAT + " FROM items GROUP BY price";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(values_ByPrice_Query)) {
            
            display_Items(rs);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        display_items_activity.select_By_Query();
    }

    public void get_ByStyle_Items() {
        // Selection of the records from the database by Style (Query)
        values_ByStyle_Query = "SELECT " + COLUMN_NAME + ","
                + COLUMN_PRICE + ","
                + COLUMN_UPPER_PRICE + ","
                + COLUMN_DESCRIPTION + ","
                + COLUMN_TRAILERS_DESCRIPTION + ","
                + COLUMN_STYLE + ","
                + COLUMN_MATERIAL + ","
                + COLUMN_PATTERN + ","
                + COLUMN_CLIMATE + ","
                + COLUMN_ACTIVITY + ","
                + COLUMN_GENDER + ","
                + COLUMN_FORMAT + " FROM items GROUP BY style";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(values_ByStyle_Query)) {
            
            display_Items(rs);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        display_items_activity.select_By_Query();
    }

    public void get_ByClimate_Items() {
        // Selection of the records from the database by climate (Query)
        values_ByClimate_Query = "SELECT " + COLUMN_NAME + ","
                + COLUMN_PRICE + ","
                + COLUMN_UPPER_PRICE + ","
                + COLUMN_DESCRIPTION + ","
                + COLUMN_TRAILERS_DESCRIPTION + ","
                + COLUMN_STYLE + ","
                + COLUMN_MATERIAL + ","
                + COLUMN_PATTERN + ","
                + COLUMN_CLIMATE + ","
                + COLUMN_ACTIVITY + ","
                + COLUMN_GENDER + ","
                + COLUMN_FORMAT + " FROM items GROUP BY climate";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(values_ByClimate_Query)) {
            display_Items(rs);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        display_items_activity.select_By_Query();
    }

    private void delete_Dublicates() {
        //Delete duplicate records in database
        String values_Query = "DELETE FROM items WHERE rowid NOT IN (SELECT min(rowid) FROM items GROUP BY name, description)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(values_Query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
