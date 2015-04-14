package agilec.ikeaswipe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;

/**
 * List containing all the IKEA items. Used in the ListView.
 * @author @LinneaMalcherek @marcusnygren
 */
public class ListContentFragment extends ListFragment {
    /**
     * An array of sample (dummy) items.
     */
    public static List<ListItem> ITEMS = new ArrayList<ListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ListItem> ITEM_MAP = new HashMap<String, ListItem>();

    static {
        // Add the items with parameters, id, content, count and productKey (IKEA ID)
        addItem(new ListItem("1", "Insexnyckel",1, 100001));
        addItem(new ListItem("2", "Skruv", 6, 100214));
        addItem(new ListItem("3", "Plugg", 2, 101350));
        addItem(new ListItem("4", "Övre ryggstödsbräda",1,0));
        addItem(new ListItem("5", "Ryggstöd", 1, 0));
        addItem(new ListItem("6", "Sits", 1,0));
        addItem(new ListItem("7", "Sidsektion",2,0));
    }

    private static void addItem(ListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ListItem {
        public String id;
        public String content;
        public int count;
        public int productKey;

        public ListItem(String id, String content, int count, int productKey) {
            this.id = id;
            this.content = content;
            this.count = count;
            this.productKey = productKey;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    /**
     * Creates a list view  with all the items added to the list
     * @author @martingrad @marcusnygren @LinneaMalcherek
     * @param inflater too fill the layout with content
     * @param container
     * @param savedInstanceState
     * @return The list view with all the items added to the list
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = new ListView(getActivity()); // Create a list view in the current activity
        listView.setId(android.R.id.list); // Sets ID according to the Android documentation

        // Connects the items to the list view activity, using the layout specified in the second parameter
        // PS: android.R.layout provides a range of sample list layouts
        setListAdapter(new ListAdapter(getActivity(), R.layout.list_item, (ArrayList)ITEMS));
        return listView;
    }

    /**
     * A custom ArrayAdapter, used to achieve the desirable list style with an image alongside text.
     * @user @martingrad @jacobselg
     */
    private class ListAdapter extends ArrayAdapter<ListItem> {
        //Copy of the arrayList or ListItem's.
        private ArrayList<ListItem> items;

        //Constructor, copying passed ListItems to items.
        public ListAdapter(Context context, int textViewResourceId, ArrayList<ListItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            ListItem o = items.get(position);
            if (o != null) {
                //Finding the current ListItems TopText, BottomText and Image
                TextView listTopText = (TextView) v.findViewById(R.id.toptext);
                TextView listBottomText = (TextView) v.findViewById(R.id.bottomtext);
                ImageView listImg = (ImageView) v.findViewById(R.id.icon);
                listTopText.setText(o.content);
                listBottomText.setText(o.count + "x");




                //Ugly way of applying the correct image to the correct ListItem.
                if(o.content == "Insexnyckel") listImg.setImageResource(R.drawable.allen_key);
                else if(o.content == "Skruv") listImg.setImageResource(R.drawable.screw);
                else if(o.content == "Plugg") listImg.setImageResource(R.drawable.dowel);
                else if(o.content == "Övre ryggstödsbräda") listImg.setImageResource(R.drawable.upper_back_side);
                else if(o.content == "Ryggstöd") listImg.setImageResource(R.drawable.back_side);
                else if(o.content == "Sits") listImg.setImageResource(R.drawable.seat);
                else if(o.content == "Sidsektion") listImg.setImageResource(R.drawable.side_section);
            }
            return v;
        }

        public void loadItemsFromJSON() {
            String json = ((SwipeActivity)getActivity()).loadJSONFromAsset();

            try{
                //TODO: skapa public static class Items
                JSONObject obj = new JSONObject(json);
            }
            catch (JSONException e){
                Log.e("JSON", "Unexpected JSON exception", e);
            }
        }
    }
}
