package agilec.ikeaswipe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONException;

/**
 * TODO: steps should be unchecked as start value
 */

/**
 * The layout for the step by step instructions
 *
 * @author @emmaforsling @martingrad @byggprojektledarn
 */
public class StepByStepFragment extends Fragment {

  private ImageButton completedStepBtn;
  private ImageView imgView;
  private Button checkbarButton;

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
    switch (stepNumber) {
      case 1:
        imgView.setImageResource(R.drawable.step1_scaled);
        break;
      case 2:
        imgView.setImageResource(R.drawable.step2_lowres);
        break;
      case 3:
        imgView.setImageResource(R.drawable.step3_lowres);
        break;
      case 4:
        imgView.setImageResource(R.drawable.step4_lowres);
        break;
      case 5:
        imgView.setImageResource(R.drawable.step5_lowres);
        break;
      case 6:
        imgView.setImageResource(R.drawable.step6_lowres);
        break;
      default:
        imgView.setImageResource(R.drawable.kritter_lowres);
        break;
    }
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
  * @param prevStep number for the previous step
  * @author LinneaMalcherek
  */
  private void setDefaultColorButtons(View view, boolean prevIsCompleted, int prevStep) {
    //Get view
    checkBarButtonView(prevStep, view);

    if(prevIsCompleted == true){
      checkbarButton.setBackgroundColor(getResources().getColor(R.color.greenOpacity));
    }
    else{
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
    switch (stepNumber) {
      case 1:
          checkbarButton = (Button) view.findViewById(R.id.step1);
          break;
      case 2:
          checkbarButton = (Button) view.findViewById(R.id.step2);
          break;
      case 3:
          checkbarButton = (Button) view.findViewById(R.id.step3);
          break;
      case 4:
          checkbarButton = (Button) view.findViewById(R.id.step4);
          break;
      case 5:
          checkbarButton = (Button) view.findViewById(R.id.step5);
          break;
      case 6:
          checkbarButton = (Button) view.findViewById(R.id.step6);
          break;
      default:
          checkbarButton = (Button) view.findViewById(R.id.step0);
          break;
    }
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
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.fragment_step_by_step, container, false);

    // Set listener to the view
    view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
      // If user swipe down -> up
      public void onSwipeTop() {
        if(stepNumber !=6) {
          // Increment the stepNumber
          stepNumber++;

          //Number for previous step
          prevStep = stepNumber-1;

          //To see if the step is completed
          prevIsCompleted = ((SwipeActivity) getActivity()).getCompletedStep(stepNumber-1);

          //Set opacity of background color for previous button
          setDefaultColorButtons(view, prevIsCompleted, prevStep);

          // Change the image source
          setImage(stepNumber);

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
        if(stepNumber != 0) {
          // Decrement the stepNumber
          stepNumber--;

          //Number for previous step
          prevStep = stepNumber+1;

          //To see if the step is completed
          prevIsCompleted = ((SwipeActivity) getActivity()).getCompletedStep(stepNumber+1);

          //Set opacity of background color for previous button
          setDefaultColorButtons(view, prevIsCompleted, prevStep);

          // Change the image source
          setImage(stepNumber);

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

    return view;
  }
}