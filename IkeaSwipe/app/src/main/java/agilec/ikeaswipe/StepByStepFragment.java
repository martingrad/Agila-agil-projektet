package agilec.ikeaswipe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;

import static java.lang.System.*;

/**
 * Functionality and layout for Step by step
 */
public class StepByStepFragment extends Fragment {

  private AllSteps stepHandler;         // Handles all steps
  private ImageButton completedStepBtn;
  private ImageButton helpBtn;
  private TextView header;
  private ImageView imgView;
  private Button checkbarButton;

  //The "x" and "y" position of the "Show Button" on screen.
  private Point p;

  /**
   * int stepNumber is used to track the current step and update the corresponding variable
   * currentStep in the parent SwipeActivity.
   * int prevStep is used to track the previous step.
   * boolean prevIsCompleted is used to see if the step is completed or not.
   */
  private int stepNumber;
  private int prevStep;
  private boolean prevIsCompleted;

  /**
   * setImage changes the image source of imgView depending on the current step
   *
   * @param stepNumber
   * @author @emmaforsling @martingrad @byggprojektledarn
   */
  private void setImage(int stepNumber) {
    String imgUrl = stepHandler.getSteps().get(stepNumber).getImgUrl(); // Get the image url for the instruction image
    int id = getResources().getIdentifier(imgUrl, "drawable", getActivity().getPackageName()); // Get the id
    imgView.setImageResource(id); // Set the correct image using id
  }

  private void setHeader(int stepNumber) {
    String title = stepHandler.getSteps().get(stepNumber).getTitle(); // Get the image url for the instruction image
    header.setText(title); // Set the correct image using id
  }

  /**
   * Check if step is completed or not
   */
  private void loadIsCompletedButton(boolean isCompleted, View view, int stepNumber) {
    if (isCompleted == false) {
      ((ImageButton) view.findViewById(R.id.completedStepButton)).setImageResource(R.drawable.done_before);
      //To get the right button
      checkBarButtonView(stepNumber, view);
      //Change color of the button
      checkbarButton.setBackgroundColor(getResources().getColor(R.color.grey));
    } else {
      ((ImageButton) view.findViewById(R.id.completedStepButton)).setImageResource(R.drawable.done_after);
      //To get the right button
      checkBarButtonView(stepNumber, view);
      //Change color of the button
      checkbarButton.setBackgroundColor(getResources().getColor(R.color.green));
    }
  }

  /**
   * Sets the background color for the checkbar-buttons that are not the current one to a color with opacity
   *
   * @param view
   * @param prevIsCompleted check if previous step is checked
   * @param prevStep        number for the previous step
   * @author LinneaMalcherek
   */
  private void setDefaultColorButtons(View view, boolean prevIsCompleted, int prevStep) {
    //Get view
    checkBarButtonView(prevStep, view);

    if (prevIsCompleted == true) {
      checkbarButton.setBackgroundColor(getResources().getColor(R.color.greenOpacity));
    } else {
      checkbarButton.setBackgroundColor(getResources().getColor(R.color.greyOpacity));
    }
  }

  /**
   * Sets checkbarButton to the one for the current step
   *
   * @param stepNumber Number for the current step
   * @param view
   * @author LinneaMalcherek
   */
  private void checkBarButtonView(int stepNumber, View view) {
    String checkbarButtonUrl = stepHandler.getSteps().get(stepNumber).getCheckbarButtonUrl();  // Get the checkbar button url for the instruction image
    int id = getResources().getIdentifier(checkbarButtonUrl, "id", getActivity().getPackageName()); // Get the id from res
    checkbarButton = (Button) view.findViewById(id); // Set the correct Button using id
  }

  /**
   * This function is overridden to save the current step number when the activity is recreated
   *
   * @param outState
   */
  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt("stepNumber", stepNumber);
    super.onSaveInstanceState(outState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    final View view = inflater.inflate(R.layout.fragment_step_by_step, container, false); // Inflate the layout for this fragment
    header = (TextView) view.findViewById(R.id.stepByStepHeader); // Define header id connection

    try {
      stepHandler = new AllSteps("kritter_steps.json", getActivity());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    // Set listener to the view
    view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
      // If user swipe down -> up
      public void onSwipeTop() {
        if (stepNumber != 6) {
          // Increment the stepNumber
          stepNumber++;

          //Number for previous step
          prevStep = stepNumber - 1;

          //To see if the step is completed
          prevIsCompleted = ((SwipeActivity) getActivity()).getCompletedStep(stepNumber - 1);

          //Set opacity of background color for previous button
          setDefaultColorButtons(view, prevIsCompleted, prevStep);

          // Change the image source
          setImage(stepNumber);

          // Change header
          setHeader(stepNumber);

          // Load the step completed button
          loadIsCompletedButton(((SwipeActivity) getActivity()).getCompletedStep(stepNumber), view, stepNumber);

          // Call the setStepNumber function in SwipeActivity to change the current step number
          try {
            ((SwipeActivity) getActivity()).setStepNumber(stepNumber);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }

      // If user swipe up -> down
      public void onSwipeBottom() {
        if (stepNumber != 0) {
          // Decrement the stepNumber
          stepNumber--;

          //Number for previous step
          prevStep = stepNumber + 1;

          //To see if the step is completed
          prevIsCompleted = ((SwipeActivity) getActivity()).getCompletedStep(stepNumber + 1);

          //Set opacity of background color for previous button
          setDefaultColorButtons(view, prevIsCompleted, prevStep);

          // Change the image source
          setImage(stepNumber);

          // Change header
          setHeader(stepNumber);

          // Load the step completed button
          loadIsCompletedButton(((SwipeActivity) getActivity()).getCompletedStep(stepNumber), view, stepNumber);

          // Call the setStepNumber function in SwipeActivity to change the current step number
          try {
            ((SwipeActivity) getActivity()).setStepNumber(stepNumber);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
    });

    // Find the ImageView from the .xml
    imgView = (ImageView) view.findViewById(R.id.steps);

    // Extract the id for the current step

    // Check if any step has been stored, if so - remain on the last step stored.
    if (savedInstanceState != null) {
      stepNumber = savedInstanceState.getInt("stepNumber");
    } else {
      stepNumber = getArguments().getInt("stepNumber");
    }

    // Set the image source
    setImage(stepNumber);

    completedStepBtn = (ImageButton) view.findViewById(R.id.completedStepButton);

    completedStepBtn.setOnClickListener(new View.OnClickListener() {
      /**
       * onClick function for the completedStepBtn
       * @author @emmaforsling @marcusnygren
       * @param v The view
       */
      @Override
      public void onClick(View v) {
        // Check if the current stepNumber is completed
        boolean isCompleted = ((SwipeActivity) getActivity()).getCompletedStep(stepNumber);

        // When the button is clicked, the status should be reversed
        if (isCompleted == true) {
          isCompleted = false;
        } else {
          isCompleted = true;
        }

        // Load a different button depending on if the step is completed or not
        loadIsCompletedButton(isCompleted, view, stepNumber);

        // Mark the step as done or undone
        ((SwipeActivity) getActivity()).setCompletedStep(stepNumber, isCompleted);
      }
    });

    helpBtn = (ImageButton) view.findViewById(R.id.stepByStepHelpButton);

    helpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        System.out.print("--Pop--");
        //Open popup window
        p = new Point();
        p.x = 100;
        p.y = 100;
        showPopup(getActivity(), p);
      }

    });

    return view;
  }

  // The method that displays the popup.
  private void showPopup(final Activity context, Point p) {
    System.out.print("--Poped--");
    int popupWidth = 200;
    int popupHeight = 150;

    // Inflate the popup_layout.xml
    LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
    LayoutInflater layoutInflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View layout = layoutInflater.inflate(R.layout.help_popup_layout, viewGroup);

    // Creating the PopupWindow
    final PopupWindow popup = new PopupWindow(context);
    popup.setContentView(layout);
    popup.setWidth(popupWidth);
    popup.setHeight(popupHeight);
    popup.setFocusable(true);

    // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
    int OFFSET_X = 30;
    int OFFSET_Y = 30;

    // Clear the default translucent background
    popup.setBackgroundDrawable(new BitmapDrawable());

    // Displaying the popup at the specified location, + offsets.
    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

    // Getting a reference to Close button, and close the popup when clicked.
    ImageButton close = (ImageButton) layout.findViewById(R.id.close);
    close.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        popup.dismiss();
      }
    });
  }
}