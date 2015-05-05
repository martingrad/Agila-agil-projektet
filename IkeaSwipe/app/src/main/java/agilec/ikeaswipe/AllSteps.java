package agilec.ikeaswipe;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ingelhag on 05/05/15.
 */
public class AllSteps {
  private String filename;            // Filename of the json-file
  private Context activityContext;    // Context of the activity
  private List<Step> steps = new ArrayList<Step>();

  public AllSteps(String filename, Context activityContext) throws JSONException {
    this.filename = filename;
    this.activityContext = activityContext;

    // When first creating the articles, read the JSON-file..
    JsonToStep jsonToArticle = new JsonToStep(filename, activityContext);

    // .. and parse it into article objects and save into Arraylist
    steps = jsonToArticle.parseJSONtoStep();
  }

  public List<Step> getSteps(){
    return steps;
  }

}
