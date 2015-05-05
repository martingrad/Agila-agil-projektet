package agilec.ikeaswipe;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ingelhag on 05/05/15.
 */
public class JsonToStep {
  private Context context;    // Context from activity
  private String jsonString;  // The jsonfile in string format
  private JSONObject jObject; // The JSON object

  public JsonToStep(String filename, Context context) throws JSONException {
    this.context = context;

    InputStream is = null;
    try {
      is = context.getAssets().open(filename); //open specified JSON file from assets folder
      jsonString = loadJSONFromFile(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String loadJSONFromFile(InputStream is) {
    String json = null;
    try {
      int size = is.available();

      byte[] buffer = new byte[size];

      is.read(buffer);

      is.close();

      json = new String(buffer, "UTF-8");

    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;
  }

  public ArrayList<Step> parseJSONtoStep() throws JSONException {

    // A List with all steps
    ArrayList<Step> steps = new ArrayList<Step>();

    // Parse the json string into a JSONObject
    jObject = new JSONObject(jsonString);

    // Make an JSONArray with all steps
    JSONArray jArr = jObject.getJSONArray("steps");

    // Loop through all parts that exits in the JSON-file
    for (int i = 0; i < jArr.length(); i++) {

      // Get an object(in this case each step)
      JSONObject obj = jArr.getJSONObject(i);

      // Set all descriptions
      int step                  = obj.getInt("step");
      String title              = obj.get("title").toString();
      String imgUrl             = obj.get("imgUrl").toString();
      String checkbarButtonUrl  = obj.get("checkbarButtonUrl").toString();
      String completeModelUrl   = obj.get("completeModelUrl").toString();
      Boolean checked           = obj.getBoolean("checked");

      Step newStep = new Step(step, title, imgUrl, checkbarButtonUrl, completeModelUrl, checked);
      steps.add(newStep);
    }

    return steps;
  }
}

