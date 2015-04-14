package agilec.ikeaswipe;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the items via loading from a JSON file and parsing
 * @user @marcusnygren @ingelhag
 */
public class Items {

    private Context context;

    public class Item {
        String id;
        String text;
        List steps;

        public Item(String id, String text, List steps){
            this.id = id;
            this.text = text;
            this.steps = steps;
        }
    }

    /**
     * Constructor of Items
     * @param filename JSON file where items are stored
     * @param context activity context
     * @user @marcusnygren @ingelhag
     */
    public Items(String filename, Context context) {
        this.context = context;

        try {
            InputStream is = context.getAssets().open(filename);
            readJsonStream(is); // load the items immediately, so we don't have to do this later
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readItemsArray(reader);
        }
        finally {
            reader.close();
        }
    }

    public List readItemsArray(JsonReader reader) throws IOException {
        List itemsList = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            itemsList.add(readItem(reader));
        }
        reader.endArray();
        return itemsList;
    }

    public Item readItem(JsonReader reader) throws IOException {
        String id = "";
        String text = null;
        List stepList = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String parts = reader.nextName();
            if (parts.equals("articleNumber")) {
                id = reader.nextString();
                //Log.d("articleNumber", id);
            } else if (parts.equals("title")) {
                text = reader.nextString();
                //Log.d("title", text);
            } else if (parts.equals("step") && reader.peek() != JsonToken.NULL) {
                stepList = readStepsArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Item(id, text, stepList);
    }

    public List readStepsArray(JsonReader reader) throws IOException {
        List stepsList = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            stepsList.add(reader.nextInt());
        }
        reader.endArray();
        return stepsList;
    }
}