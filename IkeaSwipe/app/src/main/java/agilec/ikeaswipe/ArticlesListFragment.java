package agilec.ikeaswipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * List containing all the IKEA items. Used in the ListView.
 * @author @LinneaMalcherek @marcusnygren
 */
public class ArticlesListFragment extends ListFragment {

    AllArticles articleHandler = null;
    ListAdapter ourAdapter = null;
    private int currentStep;

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

        // Get all articles for Kritter
        try {
            articleHandler = new AllArticles("kritter_parts.json", getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set our adapter
        ArrayList<Article> temp = new ArrayList(articleHandler.getArticles());
        ourAdapter = new ListAdapter(getActivity(), R.layout.list_item, temp);

        /**
         *  Connects the items to the list view activity, using the layout specified in the second parameter
         *  Third parameter = an ArrayList with all our articles
         */

        setListAdapter(ourAdapter);
        return listView;
    }



    /**
     * Updates out list depending on which step is active
     * @param step Current step in the application
     * @throws JSONException
     * @user @ingelhag
     */
    public void updateListWithStep(int step) throws JSONException {

        // Set current step - needed to be done to set correct quantity in the list
        // Set all articles that will be shown in the list depending on the current step
        currentStep = step;
        List<Article> theList = articleHandler.getArticlesInStep(step);

        /**
         * Notify the list something will be changed
         * Delete all data from the list
         */
        ourAdapter.clear();
        ourAdapter.addAll(theList);
        ourAdapter.notifyDataSetChanged();
    }

    //@Override
    public void onItemClick(ListView listview, View view, int pos, long l) {
        Log.d("Item","Item:" + pos);
        //Intent i = new Intent(this, ProductActivity.class);
        //i.putExtra("item_id", manager.getItemIdAtIndex(pos));
        //startActivity(i);
    }

    /**
     * A custom ArrayAdapter, used to achieve the desirable list style with an image alongside text.
     * @user @martingrad @jacobselg, @ingelhag
     */
    private class ListAdapter extends ArrayAdapter<Article> {
        //Copy of the arrayList of Articles
        private ArrayList<Article> article;

        //Constructor, copying passed ListItems to items.
        public ListAdapter(Context context, int textViewResourceId, ArrayList<Article> article) {
            super(context, textViewResourceId, article);
            this.article = article;
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         * @user @ingelhag
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }

            // Get one article and print it into a view
            Article a = article.get(position);
            if (a != null) {
                //Finding the current ListItems TopText, BottomText and Image
                TextView listTopText = (TextView) v.findViewById(R.id.toptext);
                TextView listBottomText = (TextView) v.findViewById(R.id.bottomtext);
                ImageView listImg = (ImageView) v.findViewById(R.id.icon);

                // Set title
                listTopText.setText(a.getTitle());

                /**
                 * Set quantity
                 * If step != 0 - set quantity for each step
                 */
                if(currentStep == 0) {
                    listBottomText.setText(a.getQuantity() + "x");
                } else {
                    listBottomText.setText(a.getSteps()[currentStep-1] + "x");
                }

                // Set image
                int id = getResources().getIdentifier(a.getImgUrl(), "drawable", getActivity().getPackageName());
                listImg.setImageResource(id);
            }
            // Return the view
            return v;
        }
    }
}