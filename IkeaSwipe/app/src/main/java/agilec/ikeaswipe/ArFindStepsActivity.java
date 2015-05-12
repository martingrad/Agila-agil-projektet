// Copyright 2007-2014 metaio GmbH. All rights reserved.
package agilec.ikeaswipe;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;

public class ArFindStepsActivity extends ARViewActivity
{

  /**
   * Reference to loaded metaioman geometry
   */
  private IGeometry mMetaioStep2;
  private IGeometry mMetaioStep3;

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
    return R.layout.tutorial_tracking_samples;
  }

  public void onButtonClick(View v)
  {
    finish();
  }

  @Override
  protected void loadContents()
  {
    System.out.println("***** I loadContents");

    try
    {
      // Load the desired tracking configuration
      AssetsManager.extractAllAssets(this, true);
      System.out.println("******* Försöker läsa in trackingCongigFile");
      final File trackingConfigFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "scanningsteps/TrackingData_MarkerlessFast.xml");

      System.out.println("****** Application context????????" + trackingConfigFile);
      System.out.println("****** Misslyckades jag????????");
      final boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
      System.out.println("*******  kallar på trackingConfigFile !!!!!");
      MetaioDebug.log("Tracking configuration loaded: " + result);

      // Load all the geometries. First - Model
      final File metaioObject2 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "scanningsteps/step_00.obj");
      final File metaioObject3 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "scanningsteps/step_01.obj");

      System.out.println("****** Application context????????" + trackingConfigFile);

      // For step 2
      if (metaioObject2 != null)
      {
        mMetaioStep2 = metaioSDK.createGeometry(metaioObject2);
        mMetaioStep2.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "scanningsteps/step00.png"));
        if (mMetaioStep2 != null)
        {
          // Set geometry properties
          mMetaioStep2.setScale(50f);
          MetaioDebug.log("Loaded geometry "+mMetaioStep2);
        }
        else
          MetaioDebug.log(Log.ERROR, "Error loading geometry: "+mMetaioStep2);
      }

      // For step 3
      if (metaioObject3 != null)
      {
        mMetaioStep3 = metaioSDK.createGeometry(metaioObject3);
        //mMetaioStep2.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(),"TutorialTrackingSamples/Assets/step00.png"));
        if (mMetaioStep3 != null)
        {
          // Set geometry properties
          mMetaioStep3.setScale(50f);
          MetaioDebug.log("Loaded geometry "+metaioObject3);
        }
        else
          MetaioDebug.log(Log.ERROR, "Error loading geometry: "+metaioObject3);
      }


    }
    catch (Exception e)
    {
      MetaioDebug.log(Log.ERROR, "Error loading contents!");
      MetaioDebug.printStackTrace(Log.ERROR, e);
    }
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
      if(mMetaioStep2 != null)
      {
        //for (int i=0; i<trackingValues.size(); i++)
        //{
        //final TrackingValues tv = trackingValues.get(i);
        //if (tv.isTrackingState())
        //{
        //mMetaioStep2.setCoordinateSystemID(tv.getCoordinateSystemID());
        mMetaioStep2.setCoordinateSystemID(1);
//						break;
//					}
//				}
      }
      if(mMetaioStep3!=null){
        mMetaioStep3.setCoordinateSystemID(2);
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

}
