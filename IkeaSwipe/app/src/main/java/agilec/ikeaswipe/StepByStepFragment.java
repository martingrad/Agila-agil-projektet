package agilec.ikeaswipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

/**
 * The layout for the step by step instructions
 * @author @emmaforsling @martingrad @byggprojektledarn
 */
public class StepByStepFragment extends Fragment {

    private Button nextBtn, prevBtn;
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
                imgView.setImageResource(R.drawable.ic_launcher);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_by_step, container, false);

        // Find the ImageView from the .xml
        imgView = (ImageView)view.findViewById(R.id.steps);

        // Extract the id for the current step
        stepNumber = getArguments().getInt("stepNumber");

        // Set the image source
        setImage(stepNumber);

        prevBtn = (Button) view.findViewById(R.id.prevBtn);
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

                // Change the image source
                setImage(stepNumber);

                // Call the setStepNumber function in SwipeActivity to change the current step number
                ((SwipeActivity)getActivity()).setStepNumber(stepNumber);
            }
        });

        nextBtn = (Button) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick function for the nextBtn
             * @author @emmaforsling @martingrad @byggprojektledarn
             * @param v
             */
            @Override
            public void onClick(View v) {
                // TODO add if statements to check that the stepNumber is valid

                // Increment the stepNumber
                stepNumber++;

                // Change the image source
                setImage(stepNumber);

                // Call the setStepNumber function in SwipeActivity to change the current step number
                ((SwipeActivity)getActivity()).setStepNumber(stepNumber);
            }
        });

        return view;
    }
}
