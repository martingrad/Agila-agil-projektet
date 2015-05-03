package agilec.ikeaswipe;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the articles
 */
public class AllArticles {

  private String filename;            // Filename of the json-file
  private Context activityContext;    // Context of the activity
  private List<Article> articles = new ArrayList<Article>(); // A List with all articles

  /**
   * Constructor for creating and parsing the JSON data into articles
   *
   * @param filename
   * @param activityContext
   * @throws JSONException
   * @user @ingelhag
   */
  public AllArticles(String filename, Context activityContext) throws JSONException {
    this.filename = filename;
    this.activityContext = activityContext;

    // When first creating the articles, read the JSON-file..
    JsonToArticle jsonToArticle = new JsonToArticle(filename, activityContext);

    // .. and parse it into article objects and save into Arraylist
    articles = jsonToArticle.parseJSONtoArticle();
  }

  /**
   * Method used to update an article to checked when identified
   *
   * @param imageName name of the image in drawable, without file extension
   */
  public void updateCheckedFromArActivity(String imageName) {
    // Iterate through all articles
    for (int i = 0; i < articles.size(); i++) {
      //Get one article
      Article article = articles.get(i);
      if (article.getImgUrl().equals(imageName)) {
        article.setChecked(true);
        break;
      }
    }
  }

  /**
   * Get all articles
   *
   * @return articles
   * @user @ingelhag
   */
  public List<Article> getArticles() {
    return articles;
  }

  /**
   * Check which article belongs to each step.
   *
   * @param step Which step we want to check
   * @return articlesInStep - contains all articles in a step
   * @user @ingelhag
   */
  public List<Article> getArticlesInStep(int step) {

    // If the current step is 0 - return all articles
    if (step == 0) {
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
      if (article.getSteps()[step] != 0) {
        articlesInStep.add(article);
      }
    }

    // articlesInStep - contains all articles in a step
    return articlesInStep;
  }

  /**
   * Whenever the JSON data should change, use this method to create the new JSON file
   *
   * @param context A context (e.g. an activity) is needed in order to write to a file
   * @user @marcusnygren @jacobselg @hannesingelhag
   */
  public void updateAndSaveJson(Context context) {
    JSONObject newJson = new JSONObject(); // the JSON to replace the existing JSON with

    JSONArray parts = new JSONArray(); // an array to store a JSON object for each part

    try {
      // For each article, create a new JSON object which stores information about the article
      for (int i = 0; i < articles.size(); i++) {
        Article article = articles.get(i);
        JSONObject part = new JSONObject();

        part.put("title", article.getTitle());
        part.put("articleNumber", article.getArticleNumber());
        part.put("quantity", article.getQuantity());
        part.put("quantityLeft", article.getQuantityLeft());
        part.put("imgUrl", article.getImgUrl());
        part.put("checked", article.getChecked());

        // If an array should be stored, parse the article values and add in to a new array
        JSONArray list = new JSONArray();
        for (int j = 0; j < article.getSteps().length; j++) {
          list.put(article.getSteps()[j]);
        }
        part.put("step", list);

        // Finally, add the part object to the parts array
        parts.put(part);
      }

      // When all parts are stored, add the parts to the new JSON object
      newJson.put("parts", parts);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    //Writing to file
    Log.d("JSON: ", newJson.toString());

    // Replace the existing file with the new JSON data
    try {
      FileOutputStream fos = context.getApplicationContext().openFileOutput("kritter_parts_edit.json", context.MODE_PRIVATE); // MODE_PRIVATES gives access to replace the private app storage with our new data. For appending, use MODE_APPEND
      fos.write(newJson.toString().getBytes()); //convert the new JSON to a string and write it to the file
      fos.close(); //close the file
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
