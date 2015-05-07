package agilec.ikeaswipe;

import java.util.Arrays;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;


public class SwipeActivity extends ActionBarActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */

  ArticlesListFragment alf = new ArticlesListFragment();
  View3dFragment v3DF = new View3dFragment();

  SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link StepByStepFragment} that is displayed in the second section of the ViewPager.
   */
  StepByStepFragment stepFragment;

  /**
   * int currentStep is used to set and track the current step that is being displayed in
   * stepByStepFragment.
   */
  private int currentStep = 0;

  private int totalNumberOfSteps = 7;
  private boolean[] completedStepsArray;


  /**
   * The {@link Bundle} that is used to initialize stepByStepFragment with the current
   * stepNumber.
   */
  Bundle bundle;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  SingleSwipeViewPager mViewPager;

  /**
   * setStepNumber is called to update the current step number from the StepByStepFragment
   *
   * @param stepNumber
   * @author @emmaforsling @martingrad @byggprojektledarn
   */
  public void setStepNumber(int stepNumber) throws JSONException {
    currentStep = stepNumber;

    // Update the list - Only show articles that belongs to the current step
    alf.updateListWithStep(currentStep);
    v3DF.changeObject(currentStep);
  }

  public void setCompletedStep(int stepNumber, boolean isCompleted) {
    completedStepsArray[stepNumber] = isCompleted;
  }

  public boolean getCompletedStep(int stepNumber) {
    return completedStepsArray[stepNumber];
  }

  /**
   * This function is overridden to save the current step number when the activity is recreated
   *
   * @param outState
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putInt("stepNumber", currentStep);
    outState.putBooleanArray("completedStepsArray", completedStepsArray);
    super.onSaveInstanceState(outState);
  }

  /**
   * Perform initialization of all fragments and loaders.
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Check if any step has been stored, if so - remain on the last step stored.
    if (savedInstanceState != null) {
      currentStep = savedInstanceState.getInt("stepNumber");
      completedStepsArray = savedInstanceState.getBooleanArray("completedStepsArray");
    } else {
      currentStep = 0;

      // Instantiate the boolean array, containing the steps.
      completedStepsArray = new boolean[totalNumberOfSteps];
      Arrays.fill(completedStepsArray, Boolean.FALSE); // Fill the array with FALSE as default
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_swipe);

    // Create a bundle with the currentStep = 0 as default, using the key "stepNumber", and
    // pass the arguments bundle to the stepByStepFragment
    bundle = new Bundle();
    //currentStep = 0;
    bundle.putInt("stepNumber", currentStep);
    stepFragment = new StepByStepFragment();
    stepFragment.setArguments(bundle);

    // Get the intent that is created in ArFindAllActivity when a user clicks the "done" button
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String objectFound = extras.getString("objectFound");
      CharSequence text = "Metaio har hittat något: " + objectFound;
      int duration = Toast.LENGTH_SHORT;
      Toast toast = Toast.makeText(getApplicationContext(), text, duration);
      toast.show();
    }

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (SingleSwipeViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);
  }

  /**
   * Initialize the contents of the Activity's standard options menu.
   *
   * @param menu
   * @return true
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_swipe, menu);
    return true;
  }

  /**
   * This hook is called whenever an item in your options menu is selected.
   *
   * @param item
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {

      super(fm);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * This method can be used if you want to add an activity you've created to a certain swipe view position in the app.
     *
     * @param position Position for your swipe view, starts from 0. So position 1, will be the next window to the right.
     * @return a PlaceholderFragment (defined as a static inner class below).
     * @author @marcusnygren
     */
    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return alf;
      } else if (position == 1) {
        return stepFragment;
      } else if (position == 2) {
        return v3DF;
      } else {
        return PlaceholderFragment.newInstance(position + 1);
      }
    }

    /**
     * Return the number of views available.
     *
     * @return 3
     */
    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }

    /**
     * This method may be called by the ViewPager to obtain a title string to describe the specified page.
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
      Locale l = Locale.getDefault();
      switch (position) {
        case 0:
          return getString(R.string.title_section1).toUpperCase(l);
        case 1:
          return getString(R.string.title_section2).toUpperCase(l);
        case 2:
          return getString(R.string.title_section3).toUpperCase(l);
      }
      return null;
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    public PlaceholderFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_swipe, container, false); // if no other layout is loaded for a position, this is the layout which is used
      return rootView;
    }
  }
}