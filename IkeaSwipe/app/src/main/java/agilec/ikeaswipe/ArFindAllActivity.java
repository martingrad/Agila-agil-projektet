package agilec.ikeaswipe;

import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.tools.io.AssetsManager;

/*
    Camera environment for Metaio
    edge based tracking with 3D models
    @author @antonosterblad
 */
public class ArFindAllActivity extends ARViewActivity {

    /**
     * Rim model
     */
    private IGeometry mRimModel = null;

    /**
     * Edge visualization model
     */
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
        mRimModel = loadModel("custom/rim.obj");
        mVizAidModel = loadModel("custom/VizAid.obj");

        if (mRimModel != null)
            mRimModel.setCoordinateSystemID(1);

        if (mVizAidModel != null)
            mVizAidModel.setCoordinateSystemID(2);

        setTrackingConfiguration("custom/rim_tracking/Tracking.xml");
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
    }

    private IGeometry loadModel(final String path)
    {
        IGeometry geometry = null;
        Log.i("info", "Path: " + path);
        try
        {
            // Load model
            final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
            //Log.i("info", "modelPath: " + modelPath);
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