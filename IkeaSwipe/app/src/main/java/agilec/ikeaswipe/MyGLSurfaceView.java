package agilec.ikeaswipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;

/**
 * Class MyGLSurfaceView
 * @author @emmaforsling @martingrad
 */
public class MyGLSurfaceView extends GLSurfaceView {

  private static final int SIZE = 60;
  private SparseArray<PointF> mActivePointers;
  private final MyGLRenderer mRenderer;

  private float xPrev, x; // X Position
  private float yPrev, y; // Y Position
  private float density;  // Density for the device

  private Paint mPaint;
  private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
          Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
          Color.LTGRAY, Color.YELLOW };

  private Paint textPaint;

  public MyGLSurfaceView(Context context){
    super(context);

    initView();

    // Create an OpenGL ES 3.0 context
    // setEGLContextClientVersion(3);
    //mRenderer = new MyGLRenderer();
    mRenderer = new MyGLRenderer(context, this);

    // Set the Renderer for drawing on the GLSurfaceView
    setRenderer(mRenderer);
  }

  public MyGLSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);

    initView();

    // Create an OpenGL ES 3.0 context
    // setEGLContextClientVersion(3);

    mRenderer = new MyGLRenderer(context, this);


    // Set the Renderer for drawing on the GLSurfaceView
    setRenderer(mRenderer);
  }

  private void initView() {
    // Set the density depending on the device
    density = getResources().getDisplayMetrics().density;

    mActivePointers = new SparseArray<PointF>();
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // set painter color to a color you like
    mPaint.setColor(Color.BLUE);
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setTextSize(20);
  }

  /**
   * Handles touch event for interact with the model in the View3DFragment
   * @param event
   * @return
   * @auther @ingelhag @marcusnygren
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
        calcDxAndDy(); // Call and set Dx and Dy depending on the x, y, xPrev and yPrev position
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
  * Returns the a MyGlRenderer class
  * @return
  */
  public MyGLRenderer getGLRenderer(){
    return mRenderer;
  }

  /**
   * Calculate Dx and Dy for rotating the model correct!
   * Call mRenderer to set new values for the angle in vertical and horizontal axis
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

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    System.out.println("Nu målar jag!");
    // draw all pointers
    for (int size = mActivePointers.size(), i = 0; i < size; i++) {
      PointF point = mActivePointers.valueAt(i);
      if (point != null)
        mPaint.setColor(colors[i % 9]);
      canvas.drawCircle(point.x, point.y, SIZE, mPaint);
    }
    canvas.drawText("Total pointers: " + mActivePointers.size(), 10, 40 , textPaint);
  }
}