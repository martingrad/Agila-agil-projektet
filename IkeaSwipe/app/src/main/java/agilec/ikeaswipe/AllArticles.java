package agilec.ikeaswipe;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Handles the articles
 * Created by Ingelhag on 14/04/15.
 */
public class AllArticles {

    private String filename;            // Filename of the json-file
    private Context activityContext;    // Context of the activity
    private List<Article> articles = new ArrayList<Article>(); // A List with all articles

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
        JsonToArticle jsonToArticle = new JsonToArticle(filename, activityContext);

        // .. and parse it into article objects and save into Arraylist
        articles = jsonToArticle.parseJSONtoItem();
    }

    /**
     * Get all articles
     * @return articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Check which article belongs to each step.
     * @param step Which step we want to check
     * @return  articlesInStep - contains all articles in a step
     */
    public List<Article> getArticlesInStep(int step) {
        // List with Articles
        List<Article> articlesInStep = new ArrayList<Article>();

        // Decrease step by one - get in correct step in the step array
        step--;

        // Iterate through all articles
        for (int i = 0; i < articles.size(); i++) {
            //Get one article
            Article article = articles.get(i);

            // If the articles step array is not equal to zero in the right step
            // Add this article to the articlesInStep list
            if(article.getSteps()[step] != 0) {
                articlesInStep.add(article);
                // System.out.println(article.getTitle() + " finns i steg:" + (step+1));
            }
        }
        // articlesInStep - contains all articles in a step
        return articlesInStep;
    }
}
