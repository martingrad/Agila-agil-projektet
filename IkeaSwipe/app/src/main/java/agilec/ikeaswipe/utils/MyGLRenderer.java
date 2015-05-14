package agilec.ikeaswipe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import agilec.ikeaswipe.R;

/**
 * Class MyGLRenderer
 *
 * @author @emmaforsling @martingrad @ingelhag
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

  private MyGLSurfaceView view;
  private MyGLDrawModel model;

  // Texture variables
  private int mTextureId; // Unique texture id
  private Bitmap mBitmap; // Bitmap being used by the renderer
  private int resourceTextureId = R.drawable.step00;  // Set the initial texture
  private boolean mShouldLoadTexture = false; // Variable to trigger texture reload
  private int[] mTexture = new int[1];

  // Rotation/interaction variables
  private float dx = 0.0f;
  private float dy = 0.0f;

  private Context context;
  private float angleY = 0f;


  /**
   * Constructor for the class MyGLRenderer
   *
   * @param context
   * @param view
   * @author @emmaforsling @martingrad
   */
  public MyGLRenderer(Context context, MyGLSurfaceView view) {
    this.view = view;
    this.context = context;
    // create model from specified .obj file
    model = new MyGLDrawModel(context, R.raw.step_00);
  }

  /**
   * Set a new model and texture
   * @author @jacobselg
   * @param newModel
   */
    public void setModel(MyGLDrawModel newModel, int id) {
        model = newModel;
        mShouldLoadTexture = true;
        resourceTextureId = id;
    }

    /**
     * loadTexture,
     * @author @emmaforsling @jacobselg
     * @param gl
     */
    private void loadTexture(GL10 gl) {
        // Generate and bind a texture...
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        mTextureId = textures[0];

        // ... And bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        // Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);

        // Use the Android GLUtils to specify a two-dimensional texture image
        // from our bitmap
        mBitmap = BitmapFactory.decodeResource(context.getResources(), resourceTextureId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
        mBitmap.recycle();
    }

    /**
     * onSurfaceCreated function
     * @author @emmaforsling @martingrad @jacobselg
     * @param gl
     * @param config
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glLoadIdentity();

        // Set field of view, aspect ratio, and near and far clipping plane distances
        GLU.gluPerspective(gl, 25.0f, (view.getWidth() * 1f) / view.getHeight(), 1, 100);

        // Set the eye position, origin position and define an up direction
        GLU.gluLookAt(gl, 0.f, 0.0f, 20.f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_STENCIL_BITS);

        // load the chosen texture
        loadTexture(gl);

        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // To show the texture on a nexus device
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
    }

    /**
     * onDrawFrame function
     * @author @emmaforsling @martingrad @jacobselg
     * @param gl
     */
    public void onDrawFrame(GL10 gl) {
        // Set background color to white
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        if (mShouldLoadTexture) { // Check if a texture reload is needed
            loadTexture(gl);
            mShouldLoadTexture = false;
        }
        if (mTextureId != -1) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        }

      // Set rotation around the y axis
      gl.glPushMatrix();
      gl.glRotatef(dx, 0f, 1f, 0f);
      gl.glRotatef(dy, 1f, 0f, 0f);
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
        gl.glViewport(0, 0, width, height);
    }

  public void setDxRotation(float newValue) {
    this.dx += newValue;
  }

  public void setDyRotation(float newValue) {
    this.dy += newValue;
  }
}
