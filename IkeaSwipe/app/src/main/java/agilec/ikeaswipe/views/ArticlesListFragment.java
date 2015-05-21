package agilec.ikeaswipe.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import agilec.ikeaswipe.activities.ArStepsActivity;
import agilec.ikeaswipe.models.AllArticles;
import agilec.ikeaswipe.activities.ArFindActivity;
import agilec.ikeaswipe.models.AllSteps;
import agilec.ikeaswipe.models.Article;
import agilec.ikeaswipe.R;
import agilec.ikeaswipe.models.Step;

/**
 * List containing all the IKEA items. Used in the ListView.
 */
public class ArticlesListFragment extends ListFragment {

  AllArticles articleHandler = null;
  ListAdapter ourAdapter = null;
  private int currentStep = 0;
  View view;
  private ImageButton helpBtn;
  private AllSteps stepHandler;         // Handles all steps

  //The "x" and "y" position of the "Show Button" on screen.
  private Point p;

  /**
   * Default constructor, fragment constructor should always be empty
   *
   * @author @marcusnygren
   */
  public ArticlesListFragment() {
  }

  /**
   * onCreate method is executed before onCreateView, used to save default data
   *
   * @param savedInstanceState bundle created from createArticlesListFragment
   * @author @marcusnygren
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    currentStep = getArguments().getInt("theCurrentStep");
  }

  /**
   * Factory method to create an ArticleListFragment, with theCurrentStep passed in.
   * Saves the current step into a bundle which is then used when creating the new fragment.
   *
   * @param theCurrentStep
   * @return
   */
  public static ArticlesListFragment createArticlesListFragment(int theCurrentStep) {
    Bundle bundle = new Bundle();
    bundle.putInt("theCurrentStep", theCurrentStep);

    ArticlesListFragment newFragment = new ArticlesListFragment();
    newFragment.setArguments(bundle);

    return newFragment;
  }

  /**
   * Creates a list view  with all the items added to the list
   *
   * @param inflater           too fill the layout with content
   * @param container
   * @param savedInstanceState
   * @return The list view with all the items added to the list
   * @author @martingrad @marcusnygren @LinneaMalcherek
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.fragment_item_list, container, false); // Inflate the layout for this fragment

    System.out.println("view: " + view);
    //ListView listView = new ListView(getActivity()); // Create a list view in the current activity
    ListView listView = (ListView)view.findViewById(R.id.item_list);
    System.out.println("listView: " + listView);
    listView.setId(android.R.id.list); // Sets ID according to the Android documentation

    helpBtn = (ImageButton) view.findViewById(R.id.listHelpButton);

    helpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showPopup(getActivity(), p);
      }
    });

    // Get all articles for Kritter
    try {
      articleHandler = new AllArticles("kritter_parts.json", getActivity());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    try {
      stepHandler = new AllSteps("kritter_steps.json", getActivity());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Intent intent = getActivity().getIntent();

    String articleImgUrl = intent.getStringExtra("objectFound");
    if (articleImgUrl != null) {
      articleHandler.updateCheckedFromArActivity(articleImgUrl); // set the article to true
      articleHandler.updateAndSaveJson(getActivity()); // notify the database of the change
    }

    // Set our adapter with the current step
    ArrayList<Article> currentList = new ArrayList(articleHandler.getArticlesInStep(currentStep));
    ourAdapter = new ListAdapter(getActivity(), R.layout.list_item, currentList);

    /**
     *  Connects the items to the list view activity, using the layout specified in the second parameter
     *  Third parameter = an ArrayList with all our articles
     */
    setListAdapter(ourAdapter);
    return view;
  }


  /**
   * OnClick on a list item
   *
   * @param l        ListView
   * @param v        View
   * @param position int
   * @param id       long
   * @author @jacobselg
   */
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    Article article = (Article) getListAdapter().getItem(position);
    System.out.println(article.getTitle());

    Intent arIntent = new Intent(getActivity(), ArFindActivity.class);
    arIntent.putExtra("article", article.getImgUrl());
    startActivity(arIntent);
  }

  /**
   * Updates out list depending on which step is active
   *
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

  public void findPos() {

    int[] location = new int[2];
    helpBtn = (ImageButton) getActivity().findViewById(R.id.stepByStepHelpButton);

    // Get the x, y location and store it in the location[] array
    // location[0] = x, location[1] = y.
    helpBtn.getLocationOnScreen(location);

    //Initialize the Point with x, and y positions
    p = new Point();
    p.x = location[0];
    p.y = location[1];
    System.out.println("HÄR1: " + p);

  }

  /**
   * Displays pop up and its contents
   * @param context Context
   * @param p       Point for help buttons position
   * @author @antonosterblad @ingelhag @emmaforsling
   */
  // The method that displays the popup.
  private void showPopup(final Activity context, Point p) {
    findPos();
    System.out.println("HÄR2: " + p);

    int popupWidth = 600;
    int popupHeight = 400;

    // Inflate the popup_layout.xml
    LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
    LayoutInflater layoutInflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View layout = layoutInflater.inflate(R.layout.help_popup_layout, viewGroup);

    // Creating the PopupWindow
    final PopupWindow popup = new PopupWindow(context);
    popup.setContentView(layout);
    popup.setWidth(popupWidth);
    popup.setHeight(popupHeight);
    popup.setFocusable(true);

    // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
    int OFFSET_X = -435;
    int OFFSET_Y = 75;

    // Clear the default translucent background
    popup.setBackgroundDrawable(new BitmapDrawable());

    // Displaying the popup at the specified location, + offsets.
    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

    // Getting a reference to ARHelp button, and send to new activity when clicked
    ImageButton ARHelp = (ImageButton) layout.findViewById(R.id.ARHelp);
    ARHelp.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent arIntent = new Intent(getActivity(), ArStepsActivity.class);
        arIntent.putExtra("currentTab", 1);
        startActivity(arIntent);

      }
    });

    // Displaying the popup at the specified location, + offsets.
    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

    // Getting a reference to ARHelp button, and send to new activity when clicked
    ImageButton ARCheckComplete = (ImageButton) layout.findViewById(R.id.ARCheckComplete);
    ARCheckComplete.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent arIntent = new Intent(getActivity(), ArFindActivity.class);
        Step _currentStep = stepHandler.getSteps().get(currentStep);

        arIntent.putExtra("article", _currentStep.getCompleteModelUrl());
        arIntent.putExtra("currentTab", 1);
        arIntent.putExtra("currentStep", _currentStep.getStep());
        startActivity(arIntent);
      }
    });

  }

  /**
   * A custom ArrayAdapter, used to achieve the desirable list style with an image alongside text.
   *
   * @user @martingrad @jacobselg @ingelhag
   */
  private class ListAdapter extends ArrayAdapter<Article> {
    //Copy of the arrayList of Articles
    private ArrayList<Article> article;

    //Constructor, copying passed ListItems to items.
    public ListAdapter(Context context, int textViewResourceId, ArrayList<Article> article) {
      super(context, textViewResourceId, article);
      this.article = article;
    }


    /* Display a single article
       * @param position The position of the item within the list
       * @param convertView The old view to reuse, if possible.
       * @param parent The list view containing all the articles, see docs for ArrayAdapter
       * @return view for a single article
       * @user @ingelhag
       */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.list_item, null);
      }

      // Get one article and print it into a view
      final Article a = article.get(position);
      if (a != null) {
        //Finding the current ListItems TopText, BottomText and Image

        TextView listTopText = (TextView) v.findViewById(R.id.toptext);
        TextView listBottomText = (TextView) v.findViewById(R.id.bottomtext);
        ImageView listImg = (ImageView) v.findViewById(R.id.icon);
        ImageButton arButton = (ImageButton) v.findViewById(R.id.arButton);

        // ImageButton for 'checked' status of article
        final ImageButton statusButton = (ImageButton) v.findViewById(R.id.status);

        // Checking what status the checked button has for correct rendering on initiation
        if (a.getChecked()) {
            ((ImageButton) v.findViewById(R.id.status)).setImageResource(R.drawable.ic_action_done_after);
        }
        else{
            ((ImageButton) v.findViewById(R.id.status)).setImageResource(R.drawable.ic_action_done_before);
        }

        /*
         * Checks if arButton show be shown for the article
         * If it should not - set the image button as INVISIBLE
         * else             - set the image button as VISIBLE
         */
        if(!a.getArAvailable()){
          arButton.setVisibility(ImageButton.INVISIBLE);
        } else {
          arButton.setVisibility(ImageButton.VISIBLE);

          // Set correct image from beginning
          ((ImageButton) v.findViewById(R.id.arButton)).setImageResource(R.drawable.icon_3d_scan);
        }

        /**
         * If user click on Ar Button:
         * 1. Creates an intent
         * 2. Send a string with imgUrl of the article
         * 3. Start activity
         */
        arButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            // Change image when clicked
            ((ImageButton) v.findViewById(R.id.arButton)).setImageResource(R.drawable.icon_3d_scan_clicked);
            Intent arIntent = new Intent(getActivity(), ArFindActivity.class);
            arIntent.putExtra("article", a.getImgUrl());    // Add the image url for the article
            arIntent.putExtra("currentStep", currentStep);  // Add which the current step
            startActivity(arIntent);
          }
        });

        // OnClickListener for 'checked'-button
        statusButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (a.getChecked()) {
                      // Update article variable
                      a.setChecked(false);
                      // Update checked button
                      ((ImageButton) v.findViewById(R.id.status)).setImageResource(R.drawable.ic_action_done_before);
                      // Notify change to AllArticles, forces a save to JSON
                      articleHandler.updateAndSaveJson(getActivity());
                  }
                  else{
                      // Update article variable
                      a.setChecked(true);
                      // Update checked button
                      ((ImageButton) v.findViewById(R.id.status)).setImageResource(R.drawable.ic_action_done_after);
                      // Notify change to AllArticles, forces a save to JSON
                      articleHandler.updateAndSaveJson(getActivity());
                  }
              }
        });

        // Set title
        listTopText.setText(a.getTitle());

        /**
         * Set quantity
         * If step != 0 - set quantity for each step
         */
        if (currentStep == 0) {
          listBottomText.setText(a.getQuantity() + "x");
        } else {
          listBottomText.setText(a.getSteps()[currentStep - 1] + "x");
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