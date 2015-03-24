package agilec.ikeaswipe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * List containing all the IKEA items. Used in the ListView.
 * @user @LinneaMalcherek @marcusnygren
 */
public class ListContent extends ListFragment {
    /**
     * An array of sample (dummy) items.
     */
    public static List<ListItem> ITEMS = new ArrayList<ListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ListItem> ITEM_MAP = new HashMap<String, ListItem>();

    static {
        // Add 3 sample items.
        addItem(new ListItem("1", "Insektsnyckel (100001) 1x"));
        addItem(new ListItem("2", "Skruv (100214) 6x"));
        addItem(new ListItem("3", "Plugg (101350) 2x"));
        addItem(new ListItem("4", "Övre ryggstödsbräda"));
        addItem(new ListItem("5", "Ryggstöd"));
        addItem(new ListItem("6", "Sits"));
        addItem(new ListItem("7", "Sidsektion"));


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

        public ListItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    /**
     * Creates a list view  with all the items added to the list
     * @param inflater too fill the layout with content
     * @param container
     * @param savedInstanceState
     * @user @martingrad @marcusnygren @LinneaMalcherek
     * @return The list view with all the items added to the list
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = new ListView(getActivity()); // Create a list view in the current activity
        listView.setId(android.R.id.list); // Sets ID according to the Android documentation

        // Connects the items to the list view activity, using the layout specified in the second parameter
        // PS: android.R.layout provides a range of sample list layouts
        setListAdapter(new OrderAdapter(getActivity(), R.layout.list_item, (ArrayList)ITEMS));
        return listView;
    }

    private class OrderAdapter extends ArrayAdapter<ListItem> {

        private ArrayList<ListItem> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<ListItem> items) {
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
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                if (tt != null) {
                    tt.setText(o.content);                            }
            }
            return v;
        }
    }

}
