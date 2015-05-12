package agilec.ikeaswipe;

import java.util.Arrays;
import java.util.Locale;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONException;


public class SplashActivity extends FragmentActivity {


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
    mViewPager.setCurrentItem(1);
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
      return 3;
    }
  }

  private class SplashScreenOne extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.splash_screen_1, container, false); // Inflate the layout for this fragment

      ImageButton button = (ImageButton) view.findViewById(R.id.backToSwipe1);
      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          System.out.println("Go to swipe from step 1");
          Intent i = new Intent(getApplicationContext(), SwipeActivity.class);
          startActivity(i);
        }
      });

      // Inflate the layout for this fragment
      return view;
    }
  }

  private class SplashScreenTwo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.splash_screen_2, container, false); // Inflate the layout for this fragment

      ImageButton button = (ImageButton) view.findViewById(R.id.backToSwipe2);
      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          System.out.println("Go to swipe from step 2");
          Intent i = new Intent(getApplicationContext(), SwipeActivity.class);
          startActivity(i);
        }
      });

      // Inflate the layout for this fragment
      return view;
    }
  }

  private class SplashScreenThree extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.splash_screen_3, container, false); // Inflate the layout for this fragment

      ImageButton button = (ImageButton) view.findViewById(R.id.backToSwipe3);
      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          System.out.println("Go to swipe from step 3");
          Intent i = new Intent(getApplicationContext(), SwipeActivity.class);
          startActivity(i);
        }
      });

      // Inflate the layout for this fragment
      return view;
    }
  }
}
