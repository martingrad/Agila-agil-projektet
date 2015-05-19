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
   * Reference to loaded metaioman geometry
   */
  private IGeometry mMetaioStep1;
  private IGeometry mMetaioStep2;
  private IGeometry mMetaioStep3;
  private IGeometry mMetaioStep4;
  private IGeometry mMetaioStep5;
  private IGeometry mMetaioStep6;


  private IGeometry testgeometry;

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
    mDirectionalLight.setCoordinateSystemID(0); // Set the lights coordinate system to the camera


    // Load all the geometries with its corresponding texture
//    mMetaioStep1 = loadModel("scanningsteps/objects/step_01.obj", "scanningsteps/textures/step01.png");
//    mMetaioStep2 = loadModel("scanningsteps/objects/step_02.obj", "scanningsteps/textures/step02.png");
//    mMetaioStep3 = loadModel("scanningsteps/objects/step_03.obj", "scanningsteps/textures/step03.png");
//    mMetaioStep4 = loadModel("scanningsteps/objects/step_04.obj", "scanningsteps/textures/step04.png");
//    mMetaioStep5 = loadModel("scanningsteps/objects/step_05.obj", "scanningsteps/textures/step05.png");
//    mMetaioStep6 = loadModel("scanningsteps/objects/step_06.obj", "scanningsteps/textures/step06.png");

    // test
    try {
      AssetsManager.extractAllAssets(this, true);
      final File modelPath2 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "scanningsteps/steg_2.zip");
      Log.d("******", modelPath2.toString());
      testgeometry = metaioSDK.createGeometry(modelPath2);

      testgeometry.setCoordinateSystemID(1);
      testgeometry.startAnimation("Default Take", true);
      Log.d("***** translation", testgeometry.getTranslation().toString());


    } catch (IOException e) {
      e.printStackTrace();
    }

//    Button testBtn = (Button) findViewById(R.id.testButton);
//    testBtn.setVisibility(View.VISIBLE);
//    // Set listener to run the function returnToSwipeActivity when onClick.
//    testBtn.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        System.out.println("Nu klickar jag på knappen");
//        System.out.println("startar animering");
//        testgeometry.startAnimation("Default Take", true);
//        System.out.println("Skalar");
//        testgeometry.setScale(50f);
//        //testgeometry.setRotation(new Rotation((float)(-Math.PI/2.0), 0, 0));
//        //testgeometry.setTranslation(new Vector3d(0, -100, 50));
//
//      }
//    });

//    testgeometry.startAnimation("Default Take", true);
//
//    testgeometry.setScale(10f);
//    testgeometry.setRelativeToScreen(IGeometry.ANCHOR_NONE);
//    mDirectionalLight.setCoordinateSystemID(1);
//    testgeometry.setCoordinateSystemID(1);
//    testgeometry.setScale(100f);






    // Tracking.xml defines how to track the model
    setTrackingConfiguration("scanningsteps/TrackingData_MarkerlessFast2.xml");
  }

  /**
   * Load 3D model
   *
   * @param path Path to object to load
   * @return geometry
   * @author @antonosterblad @linneamalcherek @jacobselg
   */
  private IGeometry loadModel(final String path, final String texturepath) {
    IGeometry geometry = null;
    try {
      // Load model
      AssetsManager.extractAllAssets(this, true);
      final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
      // Log.i("info", "modelPath: " + modelPath);
      geometry = metaioSDK.createGeometry(modelPath);
      geometry.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), texturepath));

      if (geometry != null) {
        // Set geometry properties
        geometry.setScale(30f);
        MetaioDebug.log("Loaded geometry " + modelPath);
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
    // TODO Auto-generated method stub
  }

  /**
   * @return
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
      if(testgeometry != null){
        testgeometry.setCoordinateSystemID(1);
      }
//      if (mMetaioStep1 != null) {
//        mMetaioStep1.setCoordinateSystemID(1); //bind the loaded geometry to this target
//      }
//
//      if (mMetaioStep2 != null) {
//        mMetaioStep2.setCoordinateSystemID(2); //bind the loaded geometry to this target
//      }
//
//      if (mMetaioStep3 != null) {
//        mMetaioStep3.setCoordinateSystemID(3); //bind the loaded geometry to this target
//      }
//
//      if (mMetaioStep4 != null) {
//        mMetaioStep4.setCoordinateSystemID(4); //bind the loaded geometry to this target
//      }
//
//      if (mMetaioStep5 != null) {
//        mMetaioStep5.setCoordinateSystemID(5); //bind the loaded geometry to this target
//      }
//
//      if (mMetaioStep6 != null) {
//        mMetaioStep6.setCoordinateSystemID(6); //bind the loaded geometry to this target
//      }
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
