package activity;

import database.dbConnection;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Item;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author @author Bohdan Skrypnyk
 */
public class Load_Elements_Activity {
    private static Document document;
    private static final  Pattern pattern = Pattern.compile("([0-9]+.[0-9]+$)");
    private static Matcher matcher ;
    private static Elements name_E, price_final_E,price_upper_E, price_min_E,trailers_description_E,
            description_E,style_E, material_E, pattern_E, climate_E, activity_E, gender_E, format_E;
    
    private static double price_FINAL,price_MIN,price_UPPER;
    
    private static Connection.Response response = null;
    private static final String userAgent_s = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36\"";
    private static String URL;
    
    public static void load_Elemets() {
        get_Elemets();
    }

    private static void get_Elemets() {
        // Select Elements from HTML by using a CSS with jquery-like selector syntax
        name_E = document.select("h1.page-title");
        price_final_E = document.select(".product-options-bottom .price-box .price-container>span, .product-info-price .price-box .price-container>span[data-price-type=\"finalPrice\"]");
        price_min_E = document.select(".product-info-main .product-info-price .price-box .price-container>span[data-price-type=\"minPrice\"]");
        price_upper_E = document.select(".product-info-main .product-info-price .price-box .price-container>span[data-price-type=\"maxPrice\"]");
        description_E = document.select("div.product.attribute.description div[class=\"value\"]");
        trailers_description_E = document.select("div.product.attribute.overview div[itemprop=\"description\"]");
        style_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Style\"]");
        material_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Material\"]");
        pattern_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Pattern\"]");
        climate_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Climate\"]");
        activity_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Activity\"]");
        gender_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Gender\"]");
        format_E = document.select(".table-wrapper .table:not(.totals):not(.cart):not(.table-comparison)>tbody td[data-th=\"Format\"]");

        select_Elemets();
    }

    private static void select_Elemets() {
        if (!name_E.isEmpty() && !price_final_E.isEmpty() && !description_E.isEmpty()|| !price_min_E.isEmpty() ) {
            Item item = new Item();
            
            item.setName(name_E.text());
            item.setDescription(description_E.text());
            item.setTrailers_description(trailers_description_E.text());
            item.setStyle(style_E.text());
            item.setMaterial(material_E.text());
            item.setPattern(pattern_E.text());
            item.setClimate(climate_E.text());
            item.setActivity(activity_E.text());
            item.setGender(gender_E.text());
            item.setFormat(format_E.text());
            item.setUrl(URL);

            if(!price_min_E.text().isEmpty() && !price_upper_E.text().isEmpty()){
                
             // Convert MIN price, from String to Double
             price_MIN = Double.parseDouble(price_min_E.text().replace("$", ""));
             item.setPrice(price_MIN);
             
             // Convert UPPER price, from String to Double
             price_UPPER = Double.parseDouble(price_upper_E.text().replace("$", ""));
             item.setUpper_price(price_UPPER);
            }else{
                // Convert FINAL price price, from String to Double
                matcher = pattern.matcher(price_final_E.text().replace("$", ""));
                while (matcher.find()){
                    price_FINAL = Double.parseDouble(matcher.group().replace(" ", ""));
                    item.setPrice(price_FINAL);
                }
            }
             
            // Insert to SQLite database
            dbConnection con = new dbConnection();
            con.insert_Items(item);
        }
    }
    
    public static void load_Elements_Activity(String url) {
        try {
            response = Jsoup.connect(url)
                    .userAgent(userAgent_s)
                    .timeout(20 * 2000)
                    .execute();
            //Chck response 
            if(200 == response.statusCode()){
            //Get Document object after parsing the html from given url.
            document = response.parse();
            URL = url;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
