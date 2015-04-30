package agilec.ikeaswipe;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by martingrad on 28/04/15.
 */
public class SingleSwipeViewPager extends ViewPager {

  public SingleSwipeViewPager(Context context) {
    super(context);
  }

  public SingleSwipeViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getPointerCount() == 1) {
      System.out.println("Swipe 1 finger");
      return super.onTouchEvent(event);
    } else {
      System.out.println("No swipe, more than one finger");
      return true;
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (event.getPointerCount() == 1) {
      return super.onInterceptTouchEvent(event);
    } else {
      return false;
    }
  }

}
