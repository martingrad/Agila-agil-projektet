package agilec.ikeaswipe;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handles the items via loading from a JSON file and parsing
 * @user @marcusnygren @ingelhag
 */
public class Items {

    private Context context;

    /**
     * Constructor of Items
     * @param filename JSON file where items are stored
     * @param context activity context
     * @user @marcusnygren @ingelhag
     */
    public Items(String filename, Context context) {
        this.context = context;
        loadJSONFromAsset(filename); // load the items immediately, so we don't have to do this later
    }

    /**
     * Opens a JSON file and parses it into a string
     * @return a string from the JSON asset file
     * @user @marcusnygren, code from http://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
     */
    public String loadJSONFromAsset(String filename) {
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
        Log.d("Print json", json);
        return json;
    }
}
