package agilec.ikeaswipe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Emma on 15-04-14.
 */
public class StepByStepFragment extends Fragment {

    private Button nextBtn, prevBtn;

    private ImageView currentStepImg;

    private ImageView imgView;

    private int stepNumber = 1;



    // nine patch ikoner att kolla upp!!!

    private void setImage(int stepNumber){
        System.out.println("setImage(" + stepNumber + ")" );
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


        imgView = (ImageView)view.findViewById(R.id.steps);
        //txtView.setText("hej");

        // Extract the id for the current step
        stepNumber = getArguments().getInt("stepNumber");
        System.out.println("getArguments().getInt('stepNumber') = " + stepNumber);

        setImage(stepNumber);

        //Bundle n = getArguments();
        //int blubb = n.getInt("stepNumber");
        //System.out.println(" " + value);

        prevBtn = (Button) view.findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * @user @ingelhag @antonosterblad
             * Start activity ArFindAllActivity.
             * @param v
             */
            // TODO refactor class names to define differences between activities and fragments.
            @Override
            public void onClick(View v) {
                Log.i("prevBtn", "Pushed it!");
                //Intent ArIntent = new Intent(getActivity(), ArFindAllActivity.class);
                //startActivity(ArIntent);
                stepNumber--;
                setImage(stepNumber);
                System.out.println("Nu anropar vi setStep med " + stepNumber);
                ((SwipeActivity)getActivity()).setStep(stepNumber);
            }
        });

        nextBtn = (Button) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * @user @ingelhag @antonosterblad
             * Start activity ArFindAllActivity.
             * @param v
             */
            // TODO refactor class names to define differences between activities and fragments.
            @Override
            public void onClick(View v) {
                Log.i("nextBtn", "Pushed it!");

                //Intent ArIntent = new Intent(getActivity(), ArFindAllActivity.class);
                //startActivity(ArIntent);
                stepNumber++;
                setImage(stepNumber);
                System.out.println("Nu anropar vi setStep med " + stepNumber);
                ((SwipeActivity)getActivity()).setStep(stepNumber);
            }
        });

        return view;
    }
}
