
package com.S2013.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.S2013.dcc.fragment.AdminSearchFragment;
import com.S2013.dcc.fragment.CreateActionItemFrag;
import com.S2013.dcc.helpers.OnButtonSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import utilities.StartDateDownloader;

/**
 * Displays a list of options specifically for Director's usage
 */
public class LaunchFragment extends Fragment implements OnClickListener {

    public static String startdate = "";
    public static String today;

    private OnButtonSelectedListener listener;

    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_launch_frag,
                container, false);
        activity = getActivity();

		/* Check if directory exists, if not create it */
		/* Call functions to get start and end dates */
        //setDefaults();

		/* Set up buttons */
        Button metaSearchButton = (Button) view.findViewById(R.id.meta_button);
        metaSearchButton.setOnClickListener(this);
        Button createSurveyButton = (Button) view.findViewById(R.id.survey_button);
        createSurveyButton.setOnClickListener(this);
        Button manageSurveyButton = (Button) view.findViewById(R.id.manage_survey_button);
        manageSurveyButton.setOnClickListener(this);
        Button adminButton = (Button) view.findViewById(R.id.admin_button);
        adminButton.setOnClickListener(this);
        Button createAI = (Button) view.findViewById(R.id.createaction);
        createAI.setOnClickListener(this);
        Button search = (Button) view.findViewById(R.id.meta_button);
        search.setOnClickListener(this);
        return view;
    }


    /* Get today's date and format it to YYYY-MM-DD */
    private String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    /* Set the start date of the program by getting the data from the server */
    private String setStartDate() {
        String programStart = "";
        StartDateDownloader getDate = new StartDateDownloader();
        getDate.execute();
        try {
            programStart = getDate.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return programStart;
    }


    /* Make the buttons work */
    public void onClick(View v) {
        Intent intent = new Intent();
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment newer;

        switch (v.getId()) {
            case R.id.meta_button:
                newer = new AdminSearchFragment();
                listener.launchFragment(newer);
                break;
            case R.id.createaction:
                createActionItems();
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

    public void createActionItems(){
        Fragment newer = new CreateActionItemFrag();
        listener.launchFragment(newer);
    }
    public void onBackPressed() {
        activity.finish();
    }
}
