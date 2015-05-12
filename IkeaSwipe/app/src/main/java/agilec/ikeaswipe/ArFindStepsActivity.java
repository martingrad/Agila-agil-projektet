// Copyright 2007-2014 metaio GmbH. All rights reserved.
package agilec.ikeaswipe;

import java.io.File;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.MetaioSDK;
import com.metaio.sdk.jni.MetaioSDKJNI;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;

public class ArFindStepsActivity extends ARViewActivity
{


  /**
   * Reference to loaded metaioman geometry
   */
  private IGeometry mMetaioStep1;
  private IGeometry mMetaioStep2;
  private IGeometry mMetaioStep3;
  private IGeometry mMetaioStep4;
  private IGeometry mMetaioStep5;
  private IGeometry mMetaioStep6;

  /**
   * Metaio SDK callback handler
   */
  private MetaioSDKCallbackHandler mCallbackHandler;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    System.out.println("***** I oncreate");
    Intent intent = getIntent();
    mCallbackHandler = new MetaioSDKCallbackHandler();
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    mCallbackHandler.delete();
    mCallbackHandler = null;
  }


  @Override
  protected int getGUILayout()
  {
    return R.layout.activity_ar_view_find_steps;
  }

  public void onButtonClick(View v)
  {
    finish();
  }

  /**
   * This functin loads the tracking file, which contains the images which are used as trackers.
   * This function then loads the geometries which shall be used for each tracker.
   */
  @Override
  protected void loadContents()
  {
    System.out.println("***** I loadContents");

    try
    {
      // Load the desired tracking configuration
      AssetsManager.extractAllAssets(this, true);
      final File trackingConfigFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "scanningsteps/TrackingData_MarkerlessFast.xml");

      final boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
      MetaioDebug.log("Tracking configuration loaded: " + result);

      // Load all the geometries with its corresponding texture
      mMetaioStep1 = loadModel("scanningsteps/objects/step_01.obj", "scanningsteps/textures/step00.png");
      mMetaioStep2 = loadModel("scanningsteps/objects/step_02.obj", "scanningsteps/textures/step00.png");
      mMetaioStep3 = loadModel("scanningsteps/objects/step_03.obj", "scanningsteps/textures/step00.png");
      mMetaioStep4 = loadModel("scanningsteps/objects/step_04.obj", "scanningsteps/textures/step00.png");
      mMetaioStep5 = loadModel("scanningsteps/objects/step_05.obj", "scanningsteps/textures/step00.png");
      mMetaioStep6 = loadModel("scanningsteps/objects/step_06.obj", "scanningsteps/textures/step00.png");

      System.out.println("****** Application context????????" + trackingConfigFile);

    }
    catch (Exception e)
    {
      MetaioDebug.log(Log.ERROR, "Error loading contents!");
      MetaioDebug.printStackTrace(Log.ERROR, e);
    }
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
  protected void onGeometryTouched(IGeometry geometry)
  {
    // TODO Auto-generated method stub
  }


  @Override
  protected IMetaioSDKCallback getMetaioSDKCallbackHandler()
  {
    return mCallbackHandler;
  }

  final class MetaioSDKCallbackHandler extends IMetaioSDKCallback
  {

    @Override
    public void onSDKReady()
    {
      // show GUI
      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          mGUIView.setVisibility(View.VISIBLE);
        }
      });
    }

    @Override
    public void onTrackingEvent(TrackingValuesVector trackingValues)
    {
      // if we detect any target, we bind the loaded geometry to this target
      if(mMetaioStep1!=null) {
        mMetaioStep1.setCoordinateSystemID(1);
      }
      if(mMetaioStep2 != null) {
        mMetaioStep2.setCoordinateSystemID(2);
      }
      if(mMetaioStep3!=null) {
        mMetaioStep3.setCoordinateSystemID(3);
      }
      if(mMetaioStep4!=null) {
        mMetaioStep4.setCoordinateSystemID(4);
      }
      if(mMetaioStep5!=null) {
        mMetaioStep5.setCoordinateSystemID(5);
      }
      if(mMetaioStep6!=null){
        mMetaioStep6.setCoordinateSystemID(6);
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

  @Override
  protected void startCamera() {
    super.startCamera();
    
  }
}
