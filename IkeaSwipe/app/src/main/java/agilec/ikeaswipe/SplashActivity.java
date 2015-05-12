package agilec.ikeaswipe;

import java.util.Arrays;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;


public class SplashActivity extends ActionBarActivity {


  SectionsPagerAdapter mSectionsPagerAdapter;
  SingleSwipeViewPager mViewPager;


  /**
   * This function is overridden to save the current step number when the activity is recreated
   *
   * @param outState
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  /**
   * Perform initialization of all fragments and loaders.
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (SingleSwipeViewPager) findViewById(R.id.pagerSplash);
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
         return new SplashScreenOne();
      } else if (position == 1) {
        return new SplashScreenTwo();
      } else { // Position == 2
        return new SplashScreenOne();
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
      return 2;
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

  private class SplashScreenOne extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.splash_screen_1, container, false);
    }
  }
  private class SplashScreenTwo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.splash_screen_2, container, false);
    }
  }
}
