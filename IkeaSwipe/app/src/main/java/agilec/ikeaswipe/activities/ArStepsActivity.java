// Copyright 2007-2014 metaio GmbH. All rights reserved.
package agilec.ikeaswipe.activities;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.Camera;
import com.metaio.sdk.jni.CameraVector;
import com.metaio.sdk.jni.ELIGHT_TYPE;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.ILight;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector2di;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import agilec.ikeaswipe.R;

public class ArStepsActivity extends ARViewActivity {


  /**
   * Reference to loaded step geometry
   */
  private IGeometry mMetaioStep1;
  private IGeometry mMetaioStep2;
  private IGeometry mMetaioStep3;
  private IGeometry mMetaioStep4;
  private IGeometry mMetaioStep5;
  private IGeometry mMetaioStep6;

  /*
  * Geometries for the animated steps
  */
  private IGeometry geometry;

  /*
   * Light sources
   */
  private ILight mDirectionalLight;


  /**
   * Metaio SDK callback handler
   */
  private MetaioSDKCallbackHandler mCallbackHandler;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    mCallbackHandler = new MetaioSDKCallbackHandler();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mCallbackHandler.delete();
    mCallbackHandler = null;
  }


  @Override
  protected int getGUILayout() {
    return R.layout.activity_ar_view_steps;
  }

  public void onButtonClick(View v) {
    finish();
  }

  /**
   * This function loads the tracking file, which contains the images which are used as trackers.
   * This function then loads the geometries which shall be used for each tracker.
   *
   * @author @emmaforsling @marcusnygren @antonosterblad
   */
  @Override
  protected void loadContents() {
    metaioSDK.setAmbientLight(new Vector3d(0.50f)); // Set the ambient light in the scene

    // TODO: change the color of ambient and diffuse to a more suitable, when the texture are removed.
    mDirectionalLight = metaioSDK.createLight();
    mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL); // Define the light as directional
    mDirectionalLight.setAmbientColor(new Vector3d(0, 0.15f, 0)); // Slightly green color
    mDirectionalLight.setDiffuseColor(new Vector3d(0.6f, 0.2f, 0)); // Orange color
    mDirectionalLight.setCoordinateSystemID(0); // Set the lights coordinate system to the camera, 0

    // Load all the geometries with its corresponding texture
    mMetaioStep1 = loadModel("scanningsteps/animations/animation_step01.zip");
    mMetaioStep2 = loadModel("scanningsteps/animations/animation_step02.zip");
    mMetaioStep3 = loadModel("scanningsteps/animations/animation_step03.zip");
    mMetaioStep4 = loadModel("scanningsteps/animations/animation_step04.zip");
    mMetaioStep5 = loadModel("scanningsteps/animations/animation_step05.zip");
    mMetaioStep6 = loadModel("scanningsteps/animations/animation_step06.zip");

    // Tracking.xml defines how to track the model
    setTrackingConfiguration("scanningsteps/TrackingData_MarkerlessFast.xml");
  }

  /**
   * Load 3D animation
   * This function can be called from loadContents(), when multiple objects
   * want to be shown on the screen. e.g. animation.zip = loadModel("PathToFile.zip");
   *
   * @param path Path to object to load
   * @return geometry
   * @author @antonosterblad
   */
  private IGeometry loadModel(final String path) {
    geometry = null;
    try {
      // Load model
      AssetsManager.extractAllAssets(this, true);
      // Load the zip-file, containing the animation
      final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
      // Log.i("info", "modelPath: " + modelPath);
      // Create a geometry object for the animation
      geometry = metaioSDK.createGeometry(modelPath);

      if (geometry != null) {
        // Set geometry properties
        geometry.setScale(30f);
        MetaioDebug.log("Loaded geometry " + modelPath);

        // Enable lighting for the model
        geometry.setDynamicLightingEnabled(true);

        // Set the model visible
        geometry.setVisible(true);

        // Start the animation.
        // "Default Take", is the animation name which can be read in the log-file (that was created)
        // when using FBXMeshConverter
        geometry.startAnimation("Default Take", true);

        // Stop rendering geometry as relative to screen
        geometry.setRelativeToScreen(IGeometry.ANCHOR_NONE);


      } else {
        MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
      }
    } catch (Exception e) {
      MetaioDebug.log(Log.ERROR, "Error loading geometry: " + e.getMessage());
      return geometry;
    }
    return geometry;
  }

  @Override
  protected void onGeometryTouched(IGeometry geometry) {
    // If we would like to interact with touch events on geometry
  }

  /**
   * @return CallbackHandler
   */
  @Override
  protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
    return mCallbackHandler;
  }

  final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

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
     * onTrackingEvent can be used to determine if an object has been identified
     *
     * @param trackingValues
     * @author @emmaforsling @marcusnygren
     */
    @Override
    public void onTrackingEvent(TrackingValuesVector trackingValues) {
      //Connect a geometry to a tracking marker.
      // The coordinate ID corresponds to the patches in the XML file.
      if (mMetaioStep1 != null) {
        mMetaioStep1.setCoordinateSystemID(1); // Bind the loaded geometry to this target
      }
      if (mMetaioStep2 != null) {
        mMetaioStep2.setCoordinateSystemID(2); // Bind the loaded geometry to this target
      }
      if (mMetaioStep3 != null) {
        mMetaioStep3.setCoordinateSystemID(3); // Bind the loaded geometry to this target
      }
      if (mMetaioStep4 != null) {
        mMetaioStep4.setCoordinateSystemID(4); // Bind the loaded geometry to this target
      }
      if (mMetaioStep5 != null) {
        mMetaioStep5.setCoordinateSystemID(5); // Bind the loaded geometry to this target
      }
      if (mMetaioStep6 != null) {
        mMetaioStep6.setCoordinateSystemID(6); // Bind the loaded geometry to this target
      }
    }

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
   * This function can be used to manipulate the camera in Metaio
   *
   * @user @marcusnygren
   */
  @Override
  protected void startCamera() {
    CameraVector cameras = metaioSDK.getCameraList();
    if (!cameras.isEmpty()) {
      com.metaio.sdk.jni.Camera camera = cameras.get(0);

      // Try to choose the front facing camera
      for (int i = 0; i < cameras.size(); i++) {
        if (cameras.get(i).getFacing() == Camera.FACE_BACK) {
          camera = cameras.get(i);
          camera.setResolution(new Vector2di(1280, 720)); // Use this to decide the resolution of the camera
          break;
        }
      }

      metaioSDK.startCamera(camera);
    } else {
      MetaioDebug.log(Log.WARN, "No camera found on the device!");
    }
  }
}
