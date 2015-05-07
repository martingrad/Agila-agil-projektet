package agilec.ikeaswipe;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This class recognize a gesture
 * Code from: http://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
 *
 * @user @ingelhag
 */
public class OnSwipeTouchListener implements OnTouchListener {

  private final GestureDetector gestureDetector;

  /**
   * Constructor
   *
   * @param ctx
   */
  public OnSwipeTouchListener(Context ctx) {
    gestureDetector = new GestureDetector(ctx, new GestureListener());
  }

  /**
   * When touching on the screen
   *
   * @param v     The View
   * @param event The Event
   * @return Returns ghe gestureDetector function onTouchEvent with event as parameter
   * @user @Ingelhag
   */
  @Override
  public boolean onTouch(View v, MotionEvent event) {
    return gestureDetector.onTouchEvent(event);
  }

  /**
   * The class handling the gesture
   *
   * @user @Ingelhag
   */
  private final class GestureListener extends SimpleOnGestureListener {

    /**
     * Static values thresholds for the gesture
     */
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public boolean onDown(MotionEvent e) {
      return true;
    }

    /**
     * @param e1        The first down motion event that started the fling.
     * @param e2        The move motion event that triggered the current onFling.
     * @param velocityX The velocity of this fling measured in pixels per second along the x axis.
     * @param velocityY The velocity of this fling measured in pixels per second along the y axis.
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      boolean result = false;
      try {
        float diffY = e2.getY() - e1.getY();

        // If user make gesture up/down within our thresholds
        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
          if (diffY > 0) {
            onSwipeBottom();
          } else {
            onSwipeTop();
          }
        }
        result = true;
      } catch (Exception exception) {
        exception.printStackTrace();
      }

      // Returns true if a gesture was done
      return result;
    }
  }

  public void onSwipeTop() {
  }

  public void onSwipeBottom() {
  }
}