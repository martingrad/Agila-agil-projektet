package agilec.ikeaswipe.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import agilec.ikeaswipe.utils.MyGLDrawModel;
import agilec.ikeaswipe.utils.MyGLSurfaceView;
import agilec.ikeaswipe.R;


/**
 * @author martingrad
 */
public class View3dFragment extends Fragment {

  MyGLSurfaceView mGLSV;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_view3d, container, false);
    mGLSV = (MyGLSurfaceView) view.findViewById(R.id.mGLSV);
    return view;
  }

  /**
   * changeObject changes objects and textures depending on the current.
   * @param currentStep is the current step in the instructions
   */
  public void changeObject(int currentStep) {
    System.out.println("Current Step: " + currentStep);

    // Get the object-id & the texture-id
    int objectId = getResources().getIdentifier("step_0"+currentStep, "raw", getActivity().getPackageName());
    int textureId = getResources().getIdentifier("step0"+currentStep, "drawable", getActivity().getPackageName());

    // Set the model and texture
    mGLSV.getGLRenderer().setModel(new MyGLDrawModel(getActivity(), objectId),textureId);
  }
}
