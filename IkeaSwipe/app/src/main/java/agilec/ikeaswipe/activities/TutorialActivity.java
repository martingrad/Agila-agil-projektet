package agilec.ikeaswipe.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import agilec.ikeaswipe.R;
import agilec.ikeaswipe.utils.SingleSwipeViewPager;

/**
 * Author: @Ingelhag
 */
public class TutorialActivity extends FragmentActivity {

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
    mViewPager.setCurrentItem(1); // Start tab should be number 1
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
     * @param position Position for your swipe view, starts from 1. So position 2, will be the next window to the right.
     *                 and 0 will be the tab to the left
     * @return the correct fragment
     * @author @marcusnygren
     */
    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
         return new SplashScreenOne();
      } else if (position == 1) {
        return new SplashScreenTwo();
      } else { // Position == 2
        return new SplashScreenThree();
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

  /**
   * The left tutorial fragment
   */
  private class SplashScreenOne extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.splash_screen_1, container, false); // Inflate the layout for this fragment

      // Get the image button
      ImageButton button = (ImageButton) view.findViewById(R.id.backToSwipe1);

      // Set an actionlistener - start the SwipeActivity
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
  /**
   * The middle tutorial fragment
   */
  private class SplashScreenTwo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.splash_screen_2, container, false); // Inflate the layout for this fragment

      // Get the image button
      ImageButton button = (ImageButton) view.findViewById(R.id.backToSwipe2);

      // Set an actionlistener - start the SwipeActivity
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
  /**
   * The right tutorial fragment
   */
  private class SplashScreenThree extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view = inflater.inflate(R.layout.splash_screen_3, container, false); // Inflate the layout for this fragment

      // Get the image button
      ImageButton button = (ImageButton) view.findViewById(R.id.backToSwipe3);

      // Set an actionlistener - start the SwipeActivity
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
