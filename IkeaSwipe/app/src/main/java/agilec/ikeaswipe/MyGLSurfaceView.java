package agilec.ikeaswipe;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;

/**
 * MyGLSurfaceView draws a 3D representation of the current step of the instructions.
 * @author @emmaforsling @martingrad
 */
public class MyGLSurfaceView extends GLSurfaceView {

  private static final int SIZE = 60;
  private SparseArray<PointF> mActivePointers;
  private MyGLRenderer mRenderer;

  private float xPrev, x; // X Position
  private float yPrev, y; // Y Position
  private float density;  // Density for the device


  public MyGLSurfaceView(Context context){
    super(context);
    // Initialize variables
    init(context);
  }

  public MyGLSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    // Initialize variables
    init(context);
  }

  private void init(Context context) {
    // Set the density depending on the device
    density = getResources().getDisplayMetrics().density;
    // Initialize pointers array
    mActivePointers = new SparseArray<PointF>();
    // Initialize renderer for the GlSurfaceView
    mRenderer = new MyGLRenderer(context, this);
    // Set the Renderer for drawing on the GLSurfaceView
    setRenderer(mRenderer);
  }

  /**
   * Handles touch events for interaction with the model in the View3DFragment.
   * @param event
   * @return
   * @author @ingelhag @marcusnygren @martingrad
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // get pointer index from the event object
    int pointerIndex = event.getActionIndex();

    // get pointer ID
    int pointerId = event.getPointerId(pointerIndex);

    // get masked (not specific to a pointer) action
    int maskedAction = event.getActionMasked();

    // Set x and y
    x = event.getX();
    y = event.getY();

    switch (maskedAction) {

      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN: {
        // Make empty Point
        PointF f = new PointF();

        // Put the empty point into mActivePointers
        mActivePointers.put(pointerId, f); // Used to count the number of touches
        break;
      }
      case MotionEvent.ACTION_MOVE: { // a pointer was moved
        calcDxAndDy(); // Call and set Dx and Dy depending on the x, y, xPrev and yPrev positions
        break;
      }
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL: {
        mActivePointers.remove(pointerId);
        break;
      }
    }

    // Set prev values
    xPrev = x;
    yPrev = y;

    return true; // Remember to return true at the end of the method to inform the SO that we have handle correctly the event. - See more at: http://www.survivingwithandroid.com/2012/08/multitouch-in-android.html#sthash.9HTAB7hI.dpuf
  }

  /**
  * Returns the MyGlRenderer instance
  * @return
  */
  public MyGLRenderer getGLRenderer(){
    return mRenderer;
  }

  /**
   * Calculate Dx and Dy for rotating the model correctly!
   * Call mRenderer to set new values for the angle in vertical and horizontal axes
   */
  private void calcDxAndDy() {
    if(mActivePointers.size() == 2) {
      // Set Dx and Dy depending on density and prev
      float dx = (x - xPrev) / density / 2.0f;
      float dy = (y - yPrev) / density / 2.0f;

      // reverse direction of rotation above the mid-line
      if (y > getHeight() / 2) {
        dx = dx * -1 ;
      }

      // reverse direction of rotation to left of the mid-line
      if (x < getWidth() / 2) {
        dy = dy * -1 ;
      }

      // Set rotation to our model
      mRenderer.setDxRotation(dx);
      mRenderer.setDyRotation(dy);
    }
  }
}
