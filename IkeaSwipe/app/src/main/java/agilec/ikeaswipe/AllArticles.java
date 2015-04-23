package agilec.ikeaswipe;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
     * @user @ingelhag
     */
    public AllArticles(String filename, Context activityContext) throws JSONException {
        this.filename = filename;
        this.activityContext = activityContext;

        // Read JSON-file ..
        JsonToArticle jsonToArticle = new JsonToArticle(filename, activityContext);

        // .. and parse it into article objects and save into Arraylist
        articles = jsonToArticle.parseJSONtoItem();
    }

    public void updateCheckedWithImgUrl(String imgUrl) {
      // Iterate through all articles
      for (int i = 0; i < articles.size(); i++) {
        //Get one article
        Article article = articles.get(i);
        if(article.getImgUrl().equals(imgUrl)) {
          article.setChecked(true);
          break;
        }
      }
    }

    /**
     * Get all articles
     * @return articles
     * @user @ingelhag
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Check which article belongs to each step.
     * @param step Which step we want to check
     * @return  articlesInStep - contains all articles in a step
     * @user @ingelhag
     */
    public List<Article> getArticlesInStep(int step) {

        // If the current step is 0 - return all articles
        if(step == 0) {
            System.out.println("Return All");
            return getArticles();
        }
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
            }
        }
        // articlesInStep - contains all articles in a step
        return articlesInStep;
    }

    public void updateJson(Context context) {

        JSONObject he = new JSONObject();

        JSONArray parts = new JSONArray();

        try {
            for (int i = 0; i < articles.size(); i++) {
                Article article = articles.get(i);
                JSONObject obj = new JSONObject();

                obj.put("title", article.getTitle());
                obj.put("articleNumber", article.getArticleNumber());
                obj.put("quantity", article.getQuantity());
                obj.put("quantityLeft", article.getQuantityLeft());
                obj.put("imgUrl", article.getImgUrl());
                obj.put("checked", article.getChecked());

                JSONArray list = new JSONArray();
                for (int j = 0; j < article.getSteps().length; j++) {
                    list.put(article.getSteps()[j]);
                }

                obj.put("step", list);
                parts.put(obj);
            }

            he.put("parts", parts);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Writing to file
        Log.d("JSON: " , he.toString());

        try {
            FileOutputStream fos = context.getApplicationContext().openFileOutput("kritter_parts_edit.json", context.MODE_PRIVATE);
            fos.write(he.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
