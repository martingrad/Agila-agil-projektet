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
    if (event.getPointerCount() == 1 && event.getPointerCount() <= 10) {
      //System.out.println("Swipe 1 finger");
      try {
        return super.onTouchEvent(event);
      } catch (Exception e) {
        return true;
      }
    } else {
      //System.out.println("No swipe, more than one finger");
      return false;
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (event.getPointerCount() == 1 && event.getPointerCount() <= 10) {
      try {
        return super.onInterceptTouchEvent(event);
      } catch (Exception e) {
        return true;
      }
    } else {
      return false;
    }
  }

}
