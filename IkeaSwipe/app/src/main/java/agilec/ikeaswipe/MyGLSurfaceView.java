package agilec.ikeaswipe;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Class MyGLSurfaceView
 * @author @emmaforsling @martingrad
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 3.0 context
        // setEGLContextClientVersion(3);
        //mRenderer = new MyGLRenderer();
        mRenderer = new MyGLRenderer(context, this);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 3.0 context
        // setEGLContextClientVersion(3);

        mRenderer = new MyGLRenderer(context, this);


        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }


  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int pointerCount = event.getPointerCount();
    if(pointerCount == 2) {
      System.out.println("Hej du klickade grafiktjosan med två fingrar!");
    } else if(pointerCount == 1) {
      System.out.println("Hej du klickade grafiktjosan med ett finger!");
    } else {
      System.out.println("Du har " + pointerCount + " fingrar!");
    }
    return false; //super.onTouchEvent(event);
  }

  /**
   * Returns the a MyGlRenderer class
   * @return
   */
    public MyGLRenderer getGLRenderer(){
      return mRenderer;
    }
}
