package activity;

import java.io.IOException;
import java.util.HashSet;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static activity.Load_Elements_Activity.load_Elements_Activity;
import static activity.Load_Elements_Activity.load_Elemets;
/**
 *
 * @author Bohdan Skrypnyk
 */

public class Load_URLs_Activity {
    private static final String URL = "<<<<<<Your URL>>>>";
    private static final String attribute = "href";
    private static final String http_s = "http://";
    private static final String anchor_tag = "a[href]";
    private static final String userAgent_s = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36\"";
    
    private static Connection.Response response = null;
    private static Document document;
    //Get links from document object.
    private static Elements links_Elements;
    //HashSet: used to avoid duplicates
    public static HashSet<String> list_URLs = new HashSet<String>();

    public static void load_URLs() {
        get_URLs(URL);
    }

    private static void get_URLs(String url) {
        if(!url.contains("#review-form") && !url.contains("#reviews")&& !url.contains("download")){
            load_URLs_Activity(url);
            load_Elements_Activity(url);
            load_Elemets(); 
        }
        links_Elements = document.select(anchor_tag);

        if (links_Elements.isEmpty()) {
            return;
        }
        //Get Element from a document by attributes
        for (Element link : links_Elements) {
            if (link.attr(attribute).contains(http_s) && !link.attr(attribute).contains("account/login")) {
                String this_url = link.attr(attribute);
                boolean add = list_URLs.add(this_url);
                if (add && link.attr(attribute).contains(http_s)) {
//                    System.out.print(this_url);
                    //Make method Recursive to found all URL's from web site
                    get_URLs(this_url);
                }
            }
        }
    }

    private static void load_URLs_Activity(String URL) {
        try {
            response = Jsoup.connect(URL)
                    .userAgent(userAgent_s)
                    .timeout(20 * 2000)
                    .execute();
            //Chck response 
            if(200 == response.statusCode()){
                //Get Document object after parsing the html from given url.
                 document = response.parse();
            }
        } catch (IOException e) {
            System.out.println("Ex : " + e);
        }
    }
}


