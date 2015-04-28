package agilec.ikeaswipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


/**
 * Created by martingrad on 21/04/15.
 */
public class View3dFragment extends Fragment {

  MyGLSurfaceView mGLSV;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_view3d, container, false);
    mGLSV = (MyGLSurfaceView)view.findViewById(R.id.mGLSV);
    return view;
  }

  /**
   * Change an object in the fragmenrt
   * @param currentStep is the current step in the instructions
   */
  public void changeObject(int currentStep) {
    System.out.println("Current Step: " + currentStep);

    // Get the object-id
    int id = getResources().getIdentifier("step_0"+currentStep, "raw", getActivity().getPackageName());
    // Set the model
    mGLSV.getGLRenderer().setModel(new DrawModel(getActivity(), id));
  }

}
