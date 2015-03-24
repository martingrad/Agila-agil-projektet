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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = new ListView(getActivity());
        listView.setId(android.R.id.list);

        setListAdapter(new ArrayAdapter<ListItem>(getActivity(), android.R.layout.simple_list_item_activated_1, ITEMS) );

        return listView;
    }


}
