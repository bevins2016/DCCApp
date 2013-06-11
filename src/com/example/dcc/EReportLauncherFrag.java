package com.example.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dcc.helpers.OnButtonSelectedListener;

/**
 * eNoteBook Activity Main menu for the application. Has various buttons to
 * access different parts of the application
 *
 * @author Ashutosh Gupta
 * @author Chris Crowell <crowelch@mail.uc.edu>
 */

public class EReportLauncherFrag extends Fragment implements OnClickListener {
    private Button eDailyButton; // eDaily Button
    private Button eReportButton; // eReport Button
    private Button fiftysevenButton; // 0:57 Button
    Activity activity;
    private OnButtonSelectedListener listener;
    /**
     * Attaches buttons to corresponding widgets in the .xml file and sets up a
     * listener (the class itself)
     *
     * @param savedInstanceState
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launcher,
                container, false);
        activity = getActivity();

        eDailyButton = (Button) view.findViewById(R.id.eDailyButton);
        eDailyButton.setOnClickListener(this);
        eReportButton = (Button) view.findViewById(R.id.eReportButton);
        eReportButton.setOnClickListener(this);
        fiftysevenButton = (Button) view.findViewById(R.id.Video057Button);
        fiftysevenButton.setOnClickListener(this);

        return view;
    }

    /**
     * Starts the corresponding activity depending on the button clicked
     *
     * @param v
     */
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.eDailyButton:
                listener.launchFragment(new EDailyActivityFrag());
                break;
        }

    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof OnButtonSelectedListener){
            listener = (OnButtonSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() +
                    "must implement MyListFragment.OnButtonSelectedListener");
        }
    }
}