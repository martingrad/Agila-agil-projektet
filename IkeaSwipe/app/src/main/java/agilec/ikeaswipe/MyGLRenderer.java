package agilec.ikeaswipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Class MyGLRenderer
 * @author @emmaforsling @martingrad @ingelhag
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {


    private MyGLSurfaceView view;
    private DrawModel model;

    private Context context;
    private float angleY = 0f;
    private int[] mTexture = new int[1];

  /**
   * Constructor for the class MyGLRenderer
   * @author @emmaforsling @martingrad
   * @param context
   * @param view
   */
    public MyGLRenderer(Context context, MyGLSurfaceView view) {
        this.view = view;
        this.context = context;
        // create model from specified .obj file
        model = new DrawModel(context, R.raw.teststol8);
    }

  /**
   * Set a new model
   * @param newModel
   */
    public void setModel(DrawModel newModel) {
      //model = newModel;
      model = new DrawModel(context, R.raw.teststol8);
    }

    /**
     * loadTexture,
     * @param gl
     * @param mContext
     * @param mTex
     */
    private void loadTexture(GL10 gl, Context mContext, int mTex) {
        // generate and bind a texture
        gl.glGenTextures(1, mTexture, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);

        // Create a bitmap from image file, and create the texture from it
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTex);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    /**
     * onSurfaceCreated function
     * @author @emmaforsling @martingrad
     * @param gl
     * @param config
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        // GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glLoadIdentity();

        // Set field of view, aspect ratio, and near and far clipping plane distances
        GLU.gluPerspective(gl, 25.0f, (view.getWidth() * 1f) / view.getHeight(), 1, 100);

        // Set the eye position, origin position and define an up direction
        GLU.gluLookAt(gl, 0.f, 0.0f, 20.f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // load the chosen texture
        loadTexture(gl, context, R.drawable.teststol7);

        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

    }

    /**
     * onDrawFrame function
     * @author @emmaforsling @martingrad
     * @param gl
     */
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        // GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Set background color to white
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Set rotation around the y axis
        gl.glPushMatrix();
        gl.glRotatef(angleY, 0f, 1f, 0f);
        model.draw(gl);
        gl.glPopMatrix();

        angleY += 0.4f;
    }

    /**
     * onSurfaceChanged function
     * @author @emmaforsling @martingrad
     * @param gl
     * @param width
     * @param height
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //GLES30.glViewport(0, 0, width, height);
        gl.glViewport(0, 0, width, height);
    }
}