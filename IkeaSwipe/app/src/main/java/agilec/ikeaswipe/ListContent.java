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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = new ListView(getActivity());
        listView.setId(android.R.id.list);

        //setListAdapter(new ArrayAdapter<ListItem>(getActivity(), android.R.layout.lis, ITEMS) );

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
                    tt.setText("Name: "+o.content);                            }
            }
            return v;
        }
    }

}
