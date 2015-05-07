package agilec.ikeaswipe;

import java.io.File;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

/**
 * Camera environment for Metaio
 * Edge based tracking with 3D models
 * Based on code from metaioSDK tutorials https://dev.metaio.com/sdk/tutorials
 */

public class ArFindAllActivity extends ARViewActivity {

  /**
   * Instance variables for 3D geometry that can be loaded within the system
   */

  private float xPrev, x; // X Position
  private float yPrev, y; // Y Position
  private float density;  // Density for the device

  // Tracking coordinate system rotation offsets
  private float cosXRotationOffset = 0.f;
  private float cosYRotationOffset = 0.f;
  private float cosZRotationOffset = 0.f;

  // 3D model
  private IGeometry mRimModel = null;

  // Edge visualization model
  private IGeometry mVizAidModel = null;

  private String articleImgUrl = "";

  /**
   * Metaio SDK callback handler
   */
  private MetaioSDKCallbackHandler mCallbackHandler;

  /**
   * Create instance for callbackhandler.
   *
   * @param savedInstanceState
   * @author @antonosterblad @linneamalcherek
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    articleImgUrl = intent.getStringExtra("article");
    mCallbackHandler = new MetaioSDKCallbackHandler();
    density = getResources().getDisplayMetrics().density;
  }


  /**
   * Delete the callbackhandler on destroy.
   *
   * @author @antonosterblad @linneamalcherek
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    mCallbackHandler.delete();
    mCallbackHandler = null;
  }

  /**
   * getMetaioSDKCallbackHandler() is called in metaioSDKViewActivity.
   * It is used to register the callback.
   *
   * @return mCallbackHandler
   * @author @antonosterblad @linneamalcherek
   */
  @Override
  protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
    return mCallbackHandler;
  }

  /**
   * Perform when onButtonClick.
   *
   * @param v View
   * @author @antonosterblad @linneamalcherek
   */
  public void onButtonClick(View v) {
    finish();
  }

  /**
   * Reset the tracking.
   *
   * @param v View
   * @author @antonosterblad @linneamalcherek
   */
  public void onResetButtonClick(View v) {
    metaioSDK.sensorCommand("reset");
  }

  /**
   * Set paths to which files to load.
   * Define their local coordinate system ID.
   *
   * @author @antonosterblad @linneamalcherek @jacobselg
   */
  @Override
  protected void loadContents() {
    // Set path for the model/file to load

    mRimModel = loadModel("custom/" + articleImgUrl + "/" + articleImgUrl + ".obj");
    mVizAidModel = loadModel("custom/" + articleImgUrl + "/" + articleImgUrl + ".obj");

    // Set id for each models individual coordinate system
    if (mRimModel != null)
      mRimModel.setCoordinateSystemID(1);

    if (mVizAidModel != null)
      mVizAidModel.setCoordinateSystemID(2);

    // Tracking.xml defines how to track the model
    setTrackingConfiguration("custom/" + articleImgUrl + "/Tracking.xml");
  }

  /**
   * IMetaioSDKCallback is an abstract class.
   * Extends IMetaioSDKCallback to make a child class MetaioSDKCallbackHandler, to implement our own functions.
   *
   * @author @antonosterblad @linneamalcherek
   */
  public final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

    /**
     * runOnUiThread; runs the specified action on the UI thread.
     * The UIThread is the main thread of execution for your application.
     *
     * @author @antonosterblad @linneamalcherek
     */
    @Override
    public void onSDKReady() {
      // show GUI
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mGUIView.setVisibility(View.VISIBLE);
        }
      });
    }

    /**
     * onTrackingEvent is used to determine if an object has been identified.
     * Doxygen: http://doxygen.metaio.com/metaioSDK40/classmetaio_1_1_i_metaio_s_d_k.html#1ae808bf2113950a6a17689c57fe9b4fe0
     *
     * @param trackingValuesVector Return pose of the tracked coordinate system.
     *                             A pose consists of a 3D translation and a 3D rotation which should act as offset.
     * @author @martingrad @antonosterblad
     */
    @Override
    // TrackingValuesVector get the pose of all tracked coordinate systems.
    public void onTrackingEvent(TrackingValuesVector trackingValuesVector) {
      super.onTrackingEvent(trackingValuesVector);
      {
        // Returned tracking values may have a state Found, Tracking or Lost.
        for (int i = 0; i < trackingValuesVector.size(); i++) {
          final TrackingValues v = trackingValuesVector.get(i);
          boolean foundObject = v.isTrackingState();
          if (foundObject) {
            // If the object is found this is displayed on the screen with toast.
            if (v.getCoordinateSystemID() == 1) {
              System.out.println("Object found!");
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  // Toast message that informs a user that an object has been found.
                  CharSequence text = "Object found!";
                  int duration = Toast.LENGTH_SHORT;
                  Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                  toast.show();

                  // Make "done button" appear when an object has been identified.
                  Button doneButton = (Button) findViewById(R.id.btnDone);
                  doneButton.setVisibility(View.VISIBLE);
                  // Set listener to run the function returnToSwipeActivity when onClick.
                  doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      returnToSwipeActivity(articleImgUrl);
                    }
                  });
                }
              });
            }
          } else {
            // If the object is lost this is displayed on the screen with toast.
            System.out.println("Object lost!");
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                if (v.getCoordinateSystemID() == 1) {
                  CharSequence text = "Object lost!";
                  int duration = Toast.LENGTH_SHORT;
                  Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                  toast.show();
                }
              }
            });
          }
        }
      }
    }
  }

  /**
   * Load 3D model
   *
   * @param path Path to object to load
   * @return geometry
   * @author @antonosterblad @linneamalcherek @jacobselg
   */
  private IGeometry loadModel(final String path) {
    IGeometry geometry = null;
    try {
      // Load model
      AssetsManager.extractAllAssets(this, true);
      final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
      // Log.i("info", "modelPath: " + modelPath);
      geometry = metaioSDK.createGeometry(modelPath);

      MetaioDebug.log("Loaded geometry " + modelPath);
    } catch (Exception e) {
      MetaioDebug.log(Log.ERROR, "Error loading geometry: " + e.getMessage());
      return geometry;
    }
    return geometry;
  }

  /**
   * Define how to track the 3D model
   *
   * @param path Path to which object will be used to track.
   * @return result
   * @author @antonosterblad @linneamalcherek @jacobselg
   */
  private boolean setTrackingConfiguration(final String path) {
    boolean result = false;
    try {
      // Set tracking configuration
      final File xmlPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
      result = metaioSDK.setTrackingConfiguration(xmlPath);
      MetaioDebug.log("Loaded tracking configuration " + xmlPath);
    } catch (Exception e) {
      MetaioDebug.log(Log.ERROR, "Error loading tracking configuration: " + path + " " + e.getMessage());
      return result;
    }
    return result;
  }

  /**
   * returnToSwipeActivity starts a new SwipeActivity,
   * with information on if an object has been identified.
   *
   * @param objectFound String with "bool" if object is found.
   * @author @martingrad
   */
  private void returnToSwipeActivity(String objectFound) {
    Intent i = new Intent(getApplicationContext(), SwipeActivity.class);
    i.putExtra("objectFound", objectFound);
    startActivity(i);
  }

  /**
   * Add xml layout on top of the camera view.
   *
   * @return activity_ar_view_find_all
   * @author @antonosterblad @linneamalcherek
   */
  @Override
  protected int getGUILayout() {
    return R.layout.activity_ar_view_find_all;
  }

  /**
   * Empty function.
   * Have to be added when extending ARViewActivity.
   * Else the class will get build errors.
   *
   * @param geometry Geometry that is touched
   * @author @antonosterblad @linneamalcherek
   */
  @Override
  protected void onGeometryTouched(IGeometry geometry) {
  }

  /**
   * OnTouch is overridden in order to use swipe gestures to rotate the visualization aid model.
   *
   * @param v
   * @param event
   * @return
   * @author @martingrad, @byggprojektedarn
   */
  @Override
  public boolean onTouch(View v, MotionEvent event) {
    setVizAidRotation(event);
    return super.onTouch(v, event);
  }

  private boolean setVizAidRotation(MotionEvent event) {
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
        break;
      }
      case MotionEvent.ACTION_MOVE: { // a pointer was moved
        rotateAndScaleModel(event.getPointerCount()); // Call and set Dx and Dy depending on the x, y, xPrev and yPrev positions
        break;
      }
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL: {
        break;
      }
    }

    // Set prev values
    xPrev = x;
    yPrev = y;

    return true;
  }

  /**
   * rotateAndScaleModel rotates and scales the visualization aid model. One finger is used to
   * rotate around the x and y axes, and two fingers are used to rotate around the z axis (using
   * horizontal gestures) and to scale the model (using vertical gestures).
   * TODO: Implement scaling. No sensor commands seem to achieve this...
   *
   * @param pointerCount
   * @author @martingrad, @byggprojektledarn
   */
  private void rotateAndScaleModel(int pointerCount) {
    // Initialize transformation variables.
    float dx = 0, dy = 0, dz = 0;
    // The density is multiplied by 100 before use to account for incorrect values. (possible bug?)
    float tempDensity = density * 100.f;

    // Set dx and dy depending on density and the current and previous pointer (finger) positions.
    if (pointerCount == 1) {                         // One finger -> Rotate around x and y axes.
      dx = (x - xPrev) / tempDensity / 2.0f;
      dy = (y - yPrev) / tempDensity / 2.0f;
    } else if (pointerCount == 2) {                  // Two fingers -> Rotate around z axis and scale.
      dz = (x - xPrev) / tempDensity / 2.0f;
    }

    // Set rotation of the model (dx and dy have been swapped to achieve correct rotation).
    metaioSDK.sensorCommand("rotateInitialPoseWorldRad", "" + (dy) + " " + (-dx) + " " + dz);
  }
}
