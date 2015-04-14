package agilec.ikeaswipe;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Handles the articles
 * Created by Ingelhag on 14/04/15.
 */
public class AllArticles {

    private String filename;            // Filename of the json-file
    private Context activityContext;    // Context of the activity
    private ArrayList<Article> articles = new ArrayList<Article>(); // A List with all articles

    /**
     * Constructor
     * @param filename
     * @param activityContext
     * @throws JSONException
     */
    public AllArticles(String filename, Context activityContext) throws JSONException {
        this.filename = filename;
        this.activityContext = activityContext;

        // Read JSON-file ..
        JsonToItems jsonToItems = new JsonToItems(filename, activityContext);

        // .. and parse it into article objects and save into Arraylist
        articles = jsonToItems.parseJSONtoItem();
    }

    /**
     * Get all articles
     * @return articles
     */
    public ArrayList<Article> getArticles() {
        return articles;
    }
}
