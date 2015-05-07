package agilec.ikeaswipe;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Defines how the application in SwipeView will react on touch events.
 */
public class SingleSwipeViewPager extends ViewPager {

  public SingleSwipeViewPager(Context context) {
    super(context);
  }

  public SingleSwipeViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * onTouchEvent handles touch screen motion events.
   * @param event
   * @return
   *
   * @author @martingrad @antonosterblad
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getPointerCount() == 1 && event.getPointerCount() <= 10) { // If only one finger is pointed at the screen.
      try {
        return super.onTouchEvent(event);
      } catch (Exception e) {
        return true;
      }
    } else { // else the user has pointed with more than one finger.
      return false;
    }
  }

  /**
   * onInterceptTouchEvent allows you to watch events as they are dispatched to your children,
   * and take ownership of the current gesture at any point.
   * More info: http://developer.android.com/reference/android/view/ViewGroup.html#onInterceptTouchEvent%28android.view.MotionEvent%29
   * @param event
   * @return
   *
   * author @martingrad @antonosterblad
   */
  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (event.getPointerCount() == 1 && event.getPointerCount() <= 10) { // If only one finger is pointed at the screen.
      try {
        return super.onInterceptTouchEvent(event);
      } catch (Exception e) {
        return true;
      }
    } else { // else the user has pointed with more than one finger.
      return false;
    }
  }

}
