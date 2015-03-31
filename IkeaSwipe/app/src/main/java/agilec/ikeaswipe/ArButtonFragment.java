package agilec.ikeaswipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * @user @ingelhag @antonosterblad
 * Simple layout to start a new AR activity when clicking on a button.
 * TODO design
 */
public class ArButtonFragment extends Fragment {

    private Button ArButton;

    /**
     * @user @ingelhag @antonosterblad
     * Create a view with fragment_ar_button.
     * Sets onClickListener on the button.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ar_button, container, false);
        ArButton = (Button) view.findViewById(R.id.Ar_button_all);
        ArButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @user @ingelhag @antonosterblad
             * Start activity ArViewFindAll.
             * @param v
             */
            // TODO refactor class names to define differences between activities and fragments.
            @Override
            public void onClick(View v) {
                Log.i("ArButtonClick", "Pushed it!");
                Intent ArIntent = new Intent(getActivity(), ArViewFindAll.class);
                startActivity(ArIntent);
            }
        });
        return view;
    }


}
