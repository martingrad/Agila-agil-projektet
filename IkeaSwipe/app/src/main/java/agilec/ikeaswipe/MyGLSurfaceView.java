package agilec.ikeaswipe;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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

  /**
   * Returns the a MyGlRenderer class
   * @return
   */
    public MyGLRenderer getGLRenderer(){
      return mRenderer;
    }
}
