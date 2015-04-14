package agilec.ikeaswipe;

import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.EENV_MAP_FORMAT;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.tools.io.AssetsManager;

/*
    Creates a camera environment for Metaio
    @author @antonosterblad @ingelhag
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
        mCallbackHandler = null;
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
/*        mRimModel = loadModel("TutorialEdgeBasedInitialization/Assets/Custom/rim.obj");
        mVizAidModel = loadModel("TutorialEdgeBasedInitialization/Assets/Custom/VizAid.obj");

        final File envmapPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), "TutorialEdgeBasedInitialization/Assets/Custom/env_map.png");
        metaioSDK.loadEnvironmentMap(envmapPath, EENV_MAP_FORMAT.EEMF_LATLONG);

        if (mRimModel != null)
            mRimModel.setCoordinateSystemID(1);

        if (mVizAidModel != null)
            mVizAidModel.setCoordinateSystemID(2);*/

        //setTrackingConfiguration("TutorialEdgeBasedInitialization/Assets/Custom/rim_tracking/Tracking.xml");
    }

/*    private IGeometry loadModel(final String path)
    {
        IGeometry geometry = null;
        try
        {
            // Load model
            final File modelPath = AssetsManager.getAssetPathAsFile(getApplicationContext(), path);
            geometry = metaioSDK.createGeometry(modelPath);

            MetaioDebug.log("Loaded geometry "+modelPath);
        }
        catch (Exception e)
        {
            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+e.getMessage());
            return geometry;
        }
        return geometry;
    }*/


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

    @Override
    protected int getGUILayout()
    {
        return R.layout.tutorial_edge_based_initialization;
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry)
    {

    }
}