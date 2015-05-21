package agilec.ikeaswipe.activities;

import java.util.Arrays;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;

import agilec.ikeaswipe.R;
import agilec.ikeaswipe.utils.SingleSwipeViewPager;
import agilec.ikeaswipe.utils.SlidingTabLayout;
import agilec.ikeaswipe.views.ArticlesListFragment;
import agilec.ikeaswipe.views.StepByStepFragment;
import agilec.ikeaswipe.views.View3dFragment;

public class SwipeActivity extends FragmentActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */

  ArticlesListFragment alf;
  View3dFragment v3DF = new View3dFragment();

  SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link agilec.ikeaswipe.views.StepByStepFragment} that is displayed in the second section of the ViewPager.
   */
  StepByStepFragment stepFragment;

  /**
   * int currentStep is used to set and track the current step that is being displayed in
   * stepByStepFragment.
   */
  private int currentStep = 0;

  private int totalNumberOfSteps = 7;
  private boolean[] completedStepsArray;

    // Resource paths for SlidingTab icons
    private int[] imageResId = {
        R.drawable.list_icon,
        R.drawable.build_icon,
        R.drawable.threedee_icon
    };

  /**
   * The {@link Bundle} that is used to initialize stepByStepFragment with the current
   * stepNumber.
   */
  Bundle bundle;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  SingleSwipeViewPager mViewPager;
  private SlidingTabLayout mSlidingTabLayout;
  private int currentTab;

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

    // Load object in 3D view on separate thread to increase GUI performance
    Thread thread = new Thread(new Runnable(){
      @Override
      public void run(){
        v3DF.changeObject(currentStep);
      }
    });
    thread.start();

    // Update the header of the 3D view.
    v3DF.setHeader(currentStep);
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
   * @author @martingrad
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

    // From the beginning the application will show the StepByStep Fragment
    Intent intent = getIntent();
    currentTab = intent.getIntExtra("currentTab", 1); // Get the current tab number

    // Get the intent that is created in ArFindAllActivity when a user clicks the "done" button
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      // Set which tab and step that will be shown
      currentTab = extras.getInt("currentTab");
      currentStep = extras.getInt("currentStep");
    }

    // Create the Article List Fragment using the current step
    alf = ArticlesListFragment.createArticlesListFragment(currentStep);

    // Create a bundle with the currentStep = 0 as default, using the key "stepNumber", and
    // pass the arguments bundle to the stepByStepFragment
    bundle = new Bundle();
    bundle.putInt("stepNumber", currentStep);
    stepFragment = new StepByStepFragment();
    stepFragment.setArguments(bundle);

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (SingleSwipeViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);
    mViewPager.setCurrentItem(currentTab); // Set which tab that will be shown

    // Set the sliding tabs layout from the xml and bind the viewPager to it.
    SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
    slidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);
    slidingTabLayout.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
    slidingTabLayout.setViewPager(mViewPager);
    // TODO: Set the color of selectedIndicator. The line below does not seem to do... =(
    //mSlidingTabLayout.setSelectedIndicatorColors(R.color.blue);
  }

  /**
   * When the view is completely loaded,
   * this function will run and return the method findPos
   * which runs in StepByStepFragment to getLocationOnScreen.
   *
   * @param hasFocus
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    if (hasFocus) {
      stepFragment.findPos();
      if(currentTab == 0) {
        alf.findPos();
      } else if (currentTab == 1) {
        alf.findPos();
        v3DF.findPos();
      } else if (currentTab == 2) {
        v3DF.findPos();
      }
    }
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
      } else { // Position == 2
        return v3DF;
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
        // Changed static text tabs to SpannableStrings with ImageSpans to render icons in tabs
        Drawable image = getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
  }
}