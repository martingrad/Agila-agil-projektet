package agilec.ikeaswipe;

import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;

/*
    Camera environment for Metaio
    edge based tracking with 3D models
    @author @antonosterblad @linneamalcherek
 */

//This is going great...

public class ArFindAllActivity extends ARViewActivity {


    /**
     * Instance variables for 3D geometry that can be loaded within the system
     */
    //3D model
    private IGeometry mRimModel = null;

    //Edge visualization model
    private IGeometry mVizAidModel = null;

    /**
     * Metaio SDK callback handler
     */
    private MetaioSDKCallbackHandler mCallbackHandler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    public void onButtonClick(View v)
    {
        finish();
    }

    public void onResetButtonClick(View v)
    {
        metaioSDK.sensorCommand("reset");
    }

    @Override
    protected void loadContents()
    {
        // Set path for the model/file to load
        mRimModel = loadModel("custom/rim.obj");
        mVizAidModel = loadModel("custom/VizAid.obj");

        // Set id for each models individual coordinate system
        if (mRimModel != null)
            mRimModel.setCoordinateSystemID(1);

        if (mVizAidModel != null)
            mVizAidModel.setCoordinateSystemID(2);

        // Tracking.xml defines how to track the model
        setTrackingConfiguration("custom/rim_tracking/Tracking.xml");
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
         * onTrackingEvent is used to determine if an object has been identified.
         * TODO: send feedback to SwipeActivity.
         * @param trackingValuesVector
         */
        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValuesVector) {
            super.onTrackingEvent(trackingValuesVector);
            {
                for (int i = 0; i < trackingValuesVector.size(); i++) {
                    final TrackingValues v = trackingValuesVector.get(i);
                    boolean foundObject = v.isTrackingState();
                    if(foundObject) {
                        System.out.println("Object found!");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(v.getCoordinateSystemID() == 1)
                                {
                                    CharSequence text = "Look! An object! =)";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                                    toast.show();
                                }
                            }
                        });
                    } else {
                        System.out.println("Object lost!");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(v.getCoordinateSystemID() == 1)
                                {
                                    CharSequence text = "No! It's gone! =(";
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
     * @param path
     * @return geometry
     */
    private IGeometry loadModel(final String path)
    {
        IGeometry geometry = null;
        try
        {
            // Load model
            AssetsManager.extractAllAssets(this, true);
            final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
            // Log.i("info", "modelPath: " + modelPath);
            geometry = metaioSDK.createGeometry(modelPath);

            MetaioDebug.log("Loaded geometry "+modelPath);
        }
        catch (Exception e)
        {
            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+e.getMessage());
            return geometry;
        }
        return geometry;
    }

    /**
     * Define how to track the 3D model
     * @param path
     * @return result
     */
    private boolean setTrackingConfiguration(final String path)
    {
        boolean result = false;
        try
        {
            // set tracking configuration
            final File xmlPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
            result = metaioSDK.setTrackingConfiguration(xmlPath);
            MetaioDebug.log("Loaded tracking configuration "+xmlPath);
        }
        catch (Exception e)
        {
            MetaioDebug.log(Log.ERROR, "Error loading tracking configuration: "+ path + " " +e.getMessage());
            return result;
        }
        return result;
    }

    @Override
    protected int getGUILayout()
    {
        return R.layout.activity_ar_view_find_all;
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry)
    {

    }
}