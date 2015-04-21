package agilec.ikeaswipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import org.json.JSONException;

/*
    TODO: prevBtn should not be visible at start position
    TODO: Should not be possible to increase or decrease step number
    TODO: refactor to use array or database instead of case statements, more dynamic
 */

/**
 * The layout for the step by step instructions
 * @author @emmaforsling @martingrad @byggprojektledarn
 */
public class StepByStepFragment extends Fragment {

    private Button nextBtn, prevBtn, completedStepBtn;
    private ImageView imgView;

    /**
     * int stepNumber is used to track the current step and update the corresponding variable
     * currentStep in the parent SwipeActivity.
     */
    private int stepNumber;

    /**
     * setImage changes the image source of imgView depending on the current step
     * @author @emmaforsling @martingrad @byggprojektledarn
     * @param stepNumber
     */
    private void setImage(int stepNumber){
        switch (stepNumber){
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
     * Function to enable or disable the prevBtn
     */
    private void checkButtonPrev(){
        if(stepNumber == 0) {
            prevBtn.setEnabled(false);
        } else {
            prevBtn.setEnabled(true);
        }
    }

    /**
     * Function to enable or disable the nextBtn
     */
    private void checkButtonNext(){
        if(stepNumber == 6){
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }
    }

    /**
     * Check if step is completed or not
     */
    private void loadIsCompletedButton(boolean temp, View view){
        if(temp == true){
            ((Button) view.findViewById(R.id.completedStepButton)).setText("Undone");
        }else{
            ((Button) view.findViewById(R.id.completedStepButton)).setText("Done");
        }
    }

    /**
     * This function is overridden to save the current step number when the activity is recreated
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

        // Find the ImageView from the .xml
        imgView = (ImageView)view.findViewById(R.id.steps);

        // Extract the id for the current step

        // Check if any step has been stored, if so - remain on the last step stored.
        if(savedInstanceState != null) {
            stepNumber = savedInstanceState.getInt("stepNumber");
        } else {
            stepNumber = getArguments().getInt("stepNumber");
        }


        // Set the image source
        setImage(stepNumber);

        prevBtn = (Button) view.findViewById(R.id.prevBtn);

        // Check if the prevBtn should be enabled or not
        checkButtonPrev();

        prevBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick function for the prevBtn
             * @author @emmaforsling @martingrad @byggprojektledaren
             * @param v
             */
            @Override
            public void onClick(View v) {
                // TODO add if statements to check that the stepNumber is valid

                // Decrement the stepNumber
                stepNumber--;

                // Check if the nextBtn and prevBtn should be enabled or not
                checkButtonPrev();
                checkButtonNext();

                // Change the image source
                setImage(stepNumber);

                // Load the step completed button
                loadIsCompletedButton(((SwipeActivity)getActivity()).getCompletedStep(stepNumber), view);

                // Call the setStepNumber function in SwipeActivity to change the current step number
                try {
                    ((SwipeActivity)getActivity()).setStepNumber(stepNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        nextBtn = (Button) view.findViewById(R.id.nextBtn);

        // Check if the nextBtn should be enabled or not
        checkButtonNext();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick function for the nextBtn
             * @author @emmaforsling @martingrad @byggprojektledarn
             * @param v
             */
            @Override
            public void onClick(View v) {

                // Increment the stepNumber
                stepNumber++;

                // Check if the nextBtn and prevBtn should be enabled or not
                checkButtonNext();
                checkButtonPrev();

                // Change the image source
                setImage(stepNumber);

                // Load the step completed button
                loadIsCompletedButton(((SwipeActivity)getActivity()).getCompletedStep(stepNumber), view);

                // Call the setStepNumber function in SwipeActivity to change the current step number
                try {
                    ((SwipeActivity)getActivity()).setStepNumber(stepNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        completedStepBtn = (Button) view.findViewById(R.id.completedStepButton);

        completedStepBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick function for the completedStepBtn
             * @author @emmaforsling @marcusnygren
             * @param v The view
             */
            @Override
            public void onClick(View v) {
                // Check if the current stepNumber is completed
                boolean isCompleted = ((SwipeActivity)getActivity()).getCompletedStep(stepNumber);

                // When the button is clicked, the status should be reversed
                if(isCompleted == true){
                    isCompleted = false;
                } else {
                    isCompleted = true;
                }

                // Load a different button depending on if the step is completed or not
                loadIsCompletedButton(isCompleted, view);

                // Mark the step as done or undone
                ((SwipeActivity)getActivity()).setCompletedStep(stepNumber, isCompleted);
            }
        });



        return view;
    }
}
