package agilec.ikeaswipe;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Handles the articles via loading from a JSON file and parsing
 * @user @marcusnygren @ingelhag
 */
public class JsonToArticle {

    private Context context;    // Context from activity
    private String jsonString;  // The jsonfile in string format
    private JSONObject jObject; // The JSON object

    /**
     * Constructor of JSONLoader
     * @param filename JSON file where articles are stored
     * @param context activity context
     * @user @marcusnygren @ingelhag
     */
    public JsonToArticle(String filename, Context context) {
        this.context = context;

        File file = new File("/data/data/agilec.ikeaswipe/files/kritter_parts_edit.json");

        if(file.exists()) {
            Log.d("====================== File", "file exists");
            try {
                InputStream is = context.openFileInput("kritter_parts_edit.json"); //checks internal storage
                jsonString = loadJSONFromAsset(is);
                Log.d("jsonString ******", jsonString);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else {
            Log.d("====================== File", "file not found"); //checks assets folder
            InputStream is = null;
            try {
                is = context.getAssets().open(filename);
                jsonString = loadJSONFromAsset(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private String loadJSONFromLocalStorage(String filename) {
        String line;
        StringBuffer text = new StringBuffer();

        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            while ((line = bReader.readLine()) != null) {
                text.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    /**
     * Parse jsonString to JSONObject
     * Get all description from an JSONObject and parse it into Article class
     * @throws JSONException
     * @user @ingelhag
     */
    public ArrayList<Article> parseJSONtoItem() throws JSONException {

        // A List with all articles
        ArrayList<Article> articles = new ArrayList<Article>();

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
            Boolean checked         = obj.getBoolean("checked");

            // JSONArray > int[] so convert the JSONArray into int[]
            int[] stepsArray        = new int[stepsJson.length()+1];
            for(int j=0; j<stepsJson.length(); j++) {
                stepsArray[j] = stepsJson.getInt(j);
            }

            // Define a new article and add this into the ArrayList
            Article newArticle = new Article(title, articleNumber, quantity, quantityLeft, imgUrl, stepsArray, checked);
            articles.add(newArticle);
        }

        return articles;
    }

    /**
     * Opens a JSON file and parses it into a string
     * @return a string from the JSON asset file
     * @user @marcusnygren, code from http://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
     */
    private String loadJSONFromAsset(InputStream is) {
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
}