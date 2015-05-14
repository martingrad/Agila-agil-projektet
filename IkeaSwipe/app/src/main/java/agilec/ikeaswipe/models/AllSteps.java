package agilec.ikeaswipe.models;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import agilec.ikeaswipe.json.JsonToStep;
import agilec.ikeaswipe.models.Step;

/**
 * Created by Ingelhag on 05/05/15.
 */
public class AllSteps {
  private String filename;            // Filename of the json-file
  private Context activityContext;    // Context of the activity
  private List<Step> steps = new ArrayList<Step>(); // All steps collected in an arrayList

  /**
   * Constructor
   * @param filename
   * @param activityContext
   * @throws JSONException
   */
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

  /**
   * (( Not in use - maybe in the future? ))
   * Get the current step
   * @return
   */
  public int getCurrentStep() {
    for(int i=0;i<steps.size();i++){
      if(steps.get(i).getCurrentStep()){
        return steps.get(i).getStep();
      }
    }
    return 0;
  }

  /**
   * (( Not in use - maybe in the future? ))
   * Set the current step
   * @param theStep
   */
  public void setCurrentStep(int theStep) {
    for(int i=0;i<steps.size();i++){
      steps.get(i).setCurrentStep(false);
    }
    steps.get(theStep).setCurrentStep(true);
  }
}
