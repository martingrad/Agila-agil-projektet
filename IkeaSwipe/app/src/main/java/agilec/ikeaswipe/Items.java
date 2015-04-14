package agilec.ikeaswipe;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Handles the items via loading from a JSON file and parsing
 * @user @marcusnygren @ingelhag
 */
public class Items {

    private ArrayList<Article> articles = new ArrayList<Article>(); // A List with all articles
    private Context context;    // Context from activity
    private String jsonString;  // The jsonfile in string format
    private JSONObject jObject; // The JSON object


    /**
     * Constructor of Items
     * @param filename JSON file where items are stored
     * @param context activity context
     * @user @marcusnygren @ingelhag
     */
    public Items(String filename, Context context) throws JSONException {
        this.context = context;
        jsonString = loadJSONFromAsset(filename); // load the items immediately, so we don't have to do this later
        parseJSONtoItem();
    }

    /**
     * Parse jsonString to JSONObject
     * Get all description from an JSONObject and parse it into Article class
     * @throws JSONException
     * @user @ingelhag
     */
    private void parseJSONtoItem() throws JSONException {

        // Parse the json string into a JSONObject
        jObject = new JSONObject(jsonString);

        // Make an JSONArray with all parts
        JSONArray jArr = jObject.getJSONArray("parts");

        // Loop through all parts that exits in the JSON-file
        for (int i = 0; i < jArr.length(); i++) {

            // Get an object(in this case each article)
            JSONObject obj = jArr.getJSONObject(i);

            // Set all descriptions
            String title            = obj.get("title").toString();
            String articleNumber    = obj.get("articleNumber").toString();
            int quantity            = obj.getInt("quantity");
            int quantityLeft        = obj.getInt("quantityLeft");
            String imgUrl           = obj.get("imgUrl").toString();
            JSONArray stepsJson     = obj.getJSONArray("step");

            // JSONArray > int[] so convert the JSONArray into int[]
            int[] stepsArray        = new int[stepsJson.length()+1];
            for(int j=0; j<stepsJson.length(); j++) {
                stepsArray[j] = stepsJson.getInt(j);
            }

            // Define a new article and add this into the ArrayList
            Article newArticle = new Article(title, articleNumber, quantity, quantityLeft, imgUrl, stepsArray);
            articles.add(newArticle);
        }
    }

    /**
     * Opens a JSON file and parses it into a string
     * @return a string from the JSON asset file
     * @user @marcusnygren, code from http://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
     */
    private String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);

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
}
