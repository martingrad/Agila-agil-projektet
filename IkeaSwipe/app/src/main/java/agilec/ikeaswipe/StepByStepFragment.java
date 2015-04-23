package agilec.ikeaswipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.ImageView;

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

  /**
   * int stepNumber is used to track the current step and update the corresponding variable
   * currentStep in the parent SwipeActivity.
   */
  private int stepNumber;

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
        imgView.setImageResource(R.drawable.step2_scaled);
        break;
      case 3:
        imgView.setImageResource(R.drawable.step3_scaled);
        break;
      case 4:
        imgView.setImageResource(R.drawable.step4_scaled);
        break;
      case 5:
        imgView.setImageResource(R.drawable.step5_scaled);
        break;
      case 6:
        imgView.setImageResource(R.drawable.step6_scaled);
        break;
      default:
        imgView.setImageResource(R.drawable.kritter_not_vector);
        break;
    }
  }

  /**
   * Check if step is completed or not
   */
  private void loadIsCompletedButton(boolean temp, View view) {
    if (temp == true) {
      ((ImageButton) view.findViewById(R.id.completedStepButton)).setImageResource(R.drawable.done_before);
    } else {
      ((ImageButton) view.findViewById(R.id.completedStepButton)).setImageResource(R.drawable.done_after);
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

          // Change the image source
          setImage(stepNumber);

          // Load the step completed button
          loadIsCompletedButton(((SwipeActivity) getActivity()).getCompletedStep(stepNumber), view);

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

          // Change the image source
          setImage(stepNumber);

          // Load the step completed button
          loadIsCompletedButton(((SwipeActivity) getActivity()).getCompletedStep(stepNumber), view);

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
        loadIsCompletedButton(isCompleted, view);

        // Mark the step as done or undone
        ((SwipeActivity) getActivity()).setCompletedStep(stepNumber, isCompleted);
      }
    });

    return view;
  }
}