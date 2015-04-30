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
    mActivePointers = new SparseArray<>();
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // set painter color to a color you like
    mPaint.setColor(Color.BLUE);
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setTextSize(20);
  }

  /**
   * Based on http://www.vogella.com/tutorials/AndroidTouch/article.html#touch_multi
   * @param event
   * @return
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {

    // get pointer index from the event object
    int pointerIndex = event.getActionIndex();

    // get pointer ID
    int pointerId = event.getPointerId(pointerIndex);

    // get masked (not specific to a pointer) action
    int maskedAction = event.getActionMasked();

    switch (maskedAction) {

      case MotionEvent.ACTION_DOWN: {
        mRenderer.setDistanceZ(30f);
      }
      case MotionEvent.ACTION_POINTER_DOWN: {
        // We have a new pointer. Lets add it to the list of pointers
        System.out.println("MYGLSurfaceView::onTouchEvent(), ACTION_POINTER_DOWN, pointer count: " + event.getPointerCount());
        System.out.println("pointerId: " + pointerId);
        PointF f = new PointF();
        f.x = event.getX(pointerIndex);
        f.y = event.getY(pointerIndex);
        System.out.println("PointF f: " + f);
        for(int i = 0; i < mActivePointers.size(); ++i) {
          System.out.println("mActivePointers.valueAt(" + i + "): " + mActivePointers.valueAt(i));
        }
        mActivePointers.put(pointerId, f);
        System.out.println("MYGLSurfaceView::onTouchEvent(), mActivePointers.size(): " + mActivePointers.size());
        break;
      }
      case MotionEvent.ACTION_MOVE: { // a pointer was moved
        System.out.println("MYGLSurfaceView::onTouchEvent(), ACTION_MOVE, pointer count: " + event.getPointerCount());
        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
          PointF point = mActivePointers.get(event.getPointerId(i));
          if (point != null) {
            point.x = event.getX(i);
            point.y = event.getY(i);
          }
          mRenderer.setDistanceZ(3f);
        }
        break;
      }
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL: {
        mActivePointers.remove(pointerId);
        break;
      }
    }
    invalidate();
    requestRender(); // why you no working? =(

    return true;      // Remember to return true at the end of the method to inform the SO that we have handle correctly the event. - See more at: http://www.survivingwithandroid.com/2012/08/multitouch-in-android.html#sthash.9HTAB7hI.dpuf
    //super.onTouchEvent(event);
  }

  /**
   * Returns the a MyGlRenderer class
   * @return
   */
  public MyGLRenderer getGLRenderer(){
    return mRenderer;
  }


  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

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
