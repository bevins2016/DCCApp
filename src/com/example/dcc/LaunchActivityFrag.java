/**********************************************************************
 * Director's Command Center
 *
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 *
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc;

/**********************************************************************
 * LaunchActivity.java
 *
 * Gets start and end dates for the program, creates directory,
 * and has password protection for AdminActivity
 *********************************************************************/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.app.*;
import android.view.ViewGroup;
import com.example.dcc.fragment.ActionItemFrag;
import com.example.dcc.fragment.AdminSearchFragment;
import com.example.dcc.fragment.CreateActionItemFrag;
import com.example.dcc.fragment.MembersListFragment;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.search.MetaSearchFrag;
import utilities.StartDateDownloader;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dcc.admin.AdminActivity;
import com.example.dcc.surveys.CreateSurvey;
import com.example.dcc.surveys.ManageSurvey;

public class LaunchActivityFrag extends Fragment implements OnClickListener {

    // Create buttons Globally so they are available to all methods
    private Button MetaSearchButton;
    private Button CreateSurveyButton;
    private Button ManageSurveyButton;
    private Button AdminButton;
    private Button createAI;

    public static String startdate = "";
    public static String today;

    Activity activity;

    private EditText passwordBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_launch_frag,
                container, false);
        activity = getActivity();

		/* Check if directory exists, if not create it */
		/* Call functions to get start and end dates */
        setDefaults();

		/* Set up buttons */
        MetaSearchButton = (Button) view.findViewById(R.id.meta_button);
        MetaSearchButton.setOnClickListener(this);
        CreateSurveyButton = (Button) view.findViewById(R.id.survey_button);
        CreateSurveyButton.setOnClickListener(this);
        ManageSurveyButton = (Button) view.findViewById(R.id.manage_survey_button);
        ManageSurveyButton.setOnClickListener(this);
        AdminButton = (Button) view.findViewById(R.id.admin_button);
        AdminButton.setOnClickListener(this);
        createAI = (Button) view.findViewById(R.id.createaction);
        createAI.setOnClickListener(this);
        Button search = (Button) view.findViewById(R.id.meta_button);
        search.setOnClickListener(this);
        return view;
    }

    /*
     * Check if directory /dcc, and files startdate.txt and enddate.txt exist,
     * and create them if they don't. Set default start and end date search
     * parameters.
     */
    private void setDefaults() {
        today = getToday();
        startdate = setStartDate();
        FileWriter filewriter = null;
        String path = Environment.getExternalStorageDirectory().getPath();
        File f = new File(path + "/dcc");
        File checkKeyword = new File(f + "/keywords.txt");
        if (!f.exists()) {
            f.mkdir();
        }
        if (!checkKeyword.exists()) {
            try {
                checkKeyword.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            filewriter = new FileWriter(f + "/startdate.txt");
            filewriter.append(startdate);
            filewriter.flush();
            filewriter.close();
            filewriter = new FileWriter(f + "/enddate.txt");
            filewriter.append(today);
            filewriter.flush();
            filewriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /*
     * Creates a popup dialog using alertbuilder to prevent access to
     * AdminActivitywithout the password
     */
    private void passwordPopup() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View popup = factory.inflate(R.layout.admin_password_popup, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setCancelable(true)
                .setTitle("Enter your Password:")
                .setView(popup)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Enter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                passwordBox = (EditText) popup
                                        .findViewById(R.id.password_box);
                                if (isCorrect(passwordBox.getText().toString())) {
                                    Intent intent = new Intent(
                                            activity,
                                            AdminActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(activity,
                                            "Incorrect password",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

        AlertDialog alertdialog = alertBuilder.create();
        alertdialog.show();
    }

    /* Checks password */
    private boolean isCorrect(String password) {
        if (password.matches("bobbill")) {
            return true;
        }
        return false;
    }

    /* Make the buttons work */
    public void onClick(View v) {
        Intent intent = new Intent();
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer;

        switch (v.getId()) {
            case R.id.meta_button:
//                intent.setClass(activity, MetaSearch.class);
//                startActivity(intent);
//                manager = activity.getFragmentManager();
//                transaction = manager.beginTransaction();
//
//                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
//                newer = new MetaSearchFrag();
//                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);
//
//                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
//                transaction.commit();

                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                newer = new AdminSearchFragment();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.admin_button:
                passwordPopup();
                break;
            case R.id.survey_button:
                intent.setClass(activity, CreateSurvey.class);
                startActivity(intent);
                break;
            case R.id.manage_survey_button:
                intent.setClass(activity, ManageSurvey.class);
                activity.finish();
                startActivity(intent);
                break;
            case R.id.createaction:
                createActionItems();
                break;
        }
    }

    public void createActionItems(){

        Intent intent = new Intent();
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new CreateActionItemFrag();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void onBackPressed() {
        activity.finish();
    }
}
