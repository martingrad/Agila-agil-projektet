package agilec.ikeaswipe;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
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

    //3D model
    private IGeometry mRimModel = null;

    //Edge visualization model
    private IGeometry mVizAidModel = null;

    /**
     * Metaio SDK callback handler
     */
    private MetaioSDKCallbackHandler mCallbackHandler;

    /**
     * Create instance for callbackhandler.
     * @param savedInstanceState
     * @author @antonosterblad @linneamalcherek
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCallbackHandler = new MetaioSDKCallbackHandler();
    }

    /**
     * Delete the callbackhandler on destroy.
     * @author @antonosterblad @linneamalcherek
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
    }

    /**
     * getMetaioSDKCallbackHandler() is called in metaioSDKViewActivity.
     * It is used to register the callback.
     * @return mCallbackHandler
     * @author @antonosterblad @linneamalcherek
     */
    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    /**
     * Perform when onButtonClick.
     * @param v View
     * @author @antonosterblad @linneamalcherek
     */
    public void onButtonClick(View v)
    {
        finish();
    }

    /**
     * Reset the tracking.
     * @param v View
     * @author @antonosterblad @linneamalcherek
     */
    public void onResetButtonClick(View v)
    {
        metaioSDK.sensorCommand("reset");
    }

    /**
     * Set paths to which files to load.
     * Define their local coordinate system ID.
     * @author @antonosterblad @linneamalcherek @jacobselg
     */
    @Override
    protected void loadContents()
    {
        // Set path for the model/file to load
        mRimModel = loadModel("custom/stolsida.obj");
        mVizAidModel = loadModel("custom/stolsida.obj");

        // Set id for each models individual coordinate system
        if (mRimModel != null)
            mRimModel.setCoordinateSystemID(1);

        if (mVizAidModel != null)
            mVizAidModel.setCoordinateSystemID(2);

        // Tracking.xml defines how to track the model
        setTrackingConfiguration("custom/rim_tracking/Tracking.xml");
    }

    /**
     * IMetaioSDKCallback is an abstract class.
     * Extends IMetaioSDKCallback to make a child class MetaioSDKCallbackHandler, to implement our own functions.
     * @author @antonosterblad @linneamalcherek
     */
    public final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

        /**
         * runOnUiThread; runs the specified action on the UI thread.
         * The UIThread is the main thread of execution for your application.
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
         * TODO: send feedback to SwipeActivity.
         * @param trackingValuesVector Return pose of the tracked coordinate system.
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
                    if(foundObject) {
                        // If the object is found this is displayed on the screen with toast.
                        if(v.getCoordinateSystemID() == 1)
                        {
                            System.out.println("Object found!");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Toast message that informs a user that an object has been found
                                    CharSequence text = "Object found!";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                                    toast.show();

                                    // Make "done button" appear when an object has been identified
                                    Button doneButton = (Button) findViewById(R.id.btnDone);
                                    doneButton.setVisibility(View.VISIBLE);
                                    doneButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            returnToSwipeActivity("true");
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
                                if(v.getCoordinateSystemID() == 1)
                                {
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
     * @param path Path to object to load
     * @return geometry
     * @author @antonosterblad @linneamalcherek @jacobselg
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
     * @param path Path to which object will be used to track.
     * @return result
     * @author @antonosterblad @linneamalcherek @jacobselg
     */
    private boolean setTrackingConfiguration(final String path)
    {
        boolean result = false;
        try
        {
            // Set tracking configuration
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


    /**
     * returnToSwipeActivity starts a new SwipeActivity with information on if an object has been
     * identified.
     * @param objectFound String with "bool" if object is found.
     */
    private void returnToSwipeActivity(String objectFound) {
        Intent i = new Intent(getApplicationContext(), SwipeActivity.class);
        i.putExtra("objectFound", objectFound);
        startActivity(i);
    }

    /**
     * Add xml layout on top of the camera view.
     * @return activity_ar_view_find_all
     * @author @antonosterblad @linneamalcherek
     */
    @Override
    protected int getGUILayout()
    {
        return R.layout.activity_ar_view_find_all;
    }

    /**
     * Empty function.
     * Have to be added when extending ARViewActivity.
     * Else the class will get build errors.
     * @param geometry Geometry that is touched
     * @author @antonosterblad @linneamalcherek
     */
    @Override
    protected void onGeometryTouched(IGeometry geometry)
    {

    }
}