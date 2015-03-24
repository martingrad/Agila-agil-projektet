package agilec.ikeaswipe;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        addItem(new ListItem("1", "Item 1"));
        addItem(new ListItem("2", "Item 2"));
        addItem(new ListItem("3", "Item 3"));
        addItem(new ListItem("4", "Item 4"));
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
        setListAdapter(new ArrayAdapter<ListItem>(getActivity(), android.R.layout.simple_list_item_activated_1, ITEMS) );

        return listView;
    }


}
