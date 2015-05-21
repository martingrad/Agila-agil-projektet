package agilec.ikeaswipe.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;

import agilec.ikeaswipe.activities.ArFindActivity;
import agilec.ikeaswipe.activities.ArStepsActivity;
import agilec.ikeaswipe.models.AllSteps;
import agilec.ikeaswipe.models.Step;
import agilec.ikeaswipe.utils.MyGLDrawModel;
import agilec.ikeaswipe.utils.MyGLSurfaceView;
import agilec.ikeaswipe.R;

/**
 * @author martingrad
 */
public class View3dFragment extends Fragment {

  MyGLSurfaceView mGLSV;
  private ImageButton helpBtn;
  private int currentStep;

  //The "x" and "y" position of the "Show Button" on screen.
  private Point p;

  private TextView header;
  private AllSteps stepHandler;

  public void setHeader(int stepNumber) {
    String title = stepHandler.getSteps().get(stepNumber).getTitle(); // Get the image url for the instruction image
    header.setText("3D-modell: " + title); // Set the correct image using id
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_view3d, container, false);
    header = (TextView) view.findViewById(R.id.threeDHeader);

    mGLSV = (MyGLSurfaceView) view.findViewById(R.id.mGLSV);

    helpBtn = (ImageButton) view.findViewById(R.id.threeDHelpButton);
    helpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showPopup(getActivity(), p);
      }
    });

    try {
      stepHandler = new AllSteps("kritter_steps.json", getActivity());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return view;
  }

  public void findPos() {

    int[] location = new int[2];
    helpBtn = (ImageButton) getActivity().findViewById(R.id.threeDHelpButton);

    // Get the x, y location and store it in the location[] array
    // location[0] = x, location[1] = y.
    helpBtn.getLocationOnScreen(location);

    //Initialize the Point with x, and y positions
    p = new Point();
    p.x = location[0];
    p.y = location[1];
  }

  /**
   * Displays pop up and its contents
   * @param context Context
   * @param p       Point for help buttons position
   * @author @antonosterblad @ingelhag @emmaforsling
   */
  private void showPopup(final Activity context, Point p) {
    findPos();

    // Set Width and height for the popup window. Uses DIP - works on different tablets
    float popupWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
    float popupHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

    // Inflate the popup_layout.xml
    LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
    LayoutInflater layoutInflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View layout = layoutInflater.inflate(R.layout.help_popup_layout, viewGroup);

    // Creating the PopupWindow
    final PopupWindow popup = new PopupWindow(context);
    popup.setContentView(layout);
    popup.setWidth((int)popupWidth);
    popup.setHeight((int)popupHeight);
    popup.setFocusable(true);

    /* Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
     * Use DIP - works on different tablets
     */
    float OFFSET_X = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -295, getResources().getDisplayMetrics());
    float OFFSET_Y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());

    // Clear the default translucent background
    popup.setBackgroundDrawable(new BitmapDrawable());

    // Displaying the popup at the specified location, + offsets.
    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + (int)OFFSET_X, p.y + (int)OFFSET_Y);

    // Getting a reference to ARHelp button, and send to new activity when clicked
    ImageButton ARHelp = (ImageButton) layout.findViewById(R.id.ARHelp);
    ARHelp.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent arIntent = new Intent(getActivity(), ArStepsActivity.class);
        arIntent.putExtra("currentTab", 1);
        startActivity(arIntent);
      }
    });

    // Displaying the popup at the specified location, + offsets.
    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + (int)OFFSET_X, p.y + (int)OFFSET_Y);

    // Getting a reference to ARHelp button, and send to new activity when clicked
    ImageButton ARCheckComplete = (ImageButton) layout.findViewById(R.id.ARCheckComplete);
    ARCheckComplete.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent arIntent = new Intent(getActivity(), ArFindActivity.class);
        Step _currentStep = stepHandler.getSteps().get(currentStep);

        arIntent.putExtra("article", _currentStep.getCompleteModelUrl());
        arIntent.putExtra("currentTab", 1);
        arIntent.putExtra("currentStep", _currentStep.getStep());
        startActivity(arIntent);
      }
    });
  }

  /**
   * changeObject changes objects and textures depending on the current.
   * @param _currentStep is the current step in the instructions
   */
  public void changeObject(int _currentStep) {

    currentStep = _currentStep;
    // Get the object-id & the texture-id
    int objectId = getResources().getIdentifier("step_0"+currentStep, "raw", getActivity().getPackageName());
    int textureId = getResources().getIdentifier("step0"+currentStep, "drawable", getActivity().getPackageName());

    // Set the model and texture
    mGLSV.getGLRenderer().setModel(new MyGLDrawModel(getActivity(), objectId), textureId);
  }
}
