package com.example.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dcc.helpers.ObjectStorage;

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
        eReportButton.setVisibility(View.INVISIBLE);

        return view;
    }

    /* Create menu */
    public boolean onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
	/* What to do when items are selected from the menu */
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    /**
     * Starts the corresponding activity depending on the button clicked
     *
     * @param v
     */
    public void onClick(View v) {
        FragmentManager manager;
        FragmentTransaction transaction;
        Fragment newer;

        switch (v.getId()) {
            case R.id.eDailyButton:
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                newer = new EDailyActivityFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
//            case R.id.eReportButton:
//            case R.id.Video057Button:
        }

    }

    /*
     * Start Android camera in video mode with max duration
     *
     * @param duration
     */
    private void setUpVideo(int duration) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration);// set length
        // of
        // recording
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// set video quality
        // to high
        startActivityForResult(intent, 1);
    }
}