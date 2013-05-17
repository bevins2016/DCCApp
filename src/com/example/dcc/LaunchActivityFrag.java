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
import com.example.dcc.search.MetaSearch;
import com.example.dcc.surveys.CreateSurvey;
import com.example.dcc.surveys.ManageSurvey;

public class LaunchActivityFrag extends Fragment implements OnClickListener {

    // Create buttons Globally so they are available to all methods
    Button newsB;
    Button loginB;
    Button calB;
    Button photoB;
    Button reportB;
    Button actionB;
    private Button searchB;
    private Button MetaSearchButton;
    private Button CreateSurveyButton;
    private Button ManageSurveyButton;
    private Button AdminButton;

    public static String startdate = "";
    public static String today;

    final String studentListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keyfile.txt";
    final String teamListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/projects.txt";

    Activity activity;

    private EditText passwordBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_launch,
                container, false);
        activity = getActivity();

		/* Check if directory exists, if not create it */
		/* Call functions to get start and end dates */
        setDefaults();
        newsB = (Button) view.findViewById(R.id.news);
        calB = (Button) view.findViewById(R.id.calendar);
        photoB = (Button) view.findViewById(R.id.photo);
        reportB = (Button) view.findViewById(R.id.report);
        actionB = (Button) view.findViewById(R.id.action);
        searchB = (Button) view.findViewById(R.id.search);

		/* Set up buttons */
        MetaSearchButton = (Button) view.findViewById(R.id.meta_button);
        MetaSearchButton.setOnClickListener(this);
        CreateSurveyButton = (Button) view.findViewById(R.id.survey_button);
        CreateSurveyButton.setOnClickListener(this);
        ManageSurveyButton = (Button) view.findViewById(R.id.manage_survey_button);
        ManageSurveyButton.setOnClickListener(this);
        AdminButton = (Button) view.findViewById(R.id.admin_button);
        AdminButton.setOnClickListener(this);
        newsB.setOnClickListener(this);
        calB.setOnClickListener(this);
        photoB.setOnClickListener(this);
        reportB.setOnClickListener(this);
        actionB.setOnClickListener(this);
        searchB.setOnClickListener(this);
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
		/*
		 * These are the old button handlers.
		 * 
		 * if (v.getId() == R.id.meta_button) {
			intent.setClass(LaunchActivity.this, MetaSearch.class);
			startActivity(intent);
		} else if (v.getId() == R.id.admin_button)
			passwordPopup();
		else if (v.getId() == R.id.survey_button) {
			intent.setClass(LaunchActivity.this, CreateSurvey.class);
			startActivity(intent);
		} else if (v.getId() == R.id.manage_survey_button) {
			intent.setClass(LaunchActivity.this, ManageSurvey.class);
			finish();
			startActivity(intent);
		}*/
        switch (v.getId()) {
            case R.id.news:
//                getActivity().startActivity(new Intent(getActivity(), AndroidRssReader.class));
                FragmentManager manager = activity.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                Fragment newer = new AndroidRssReaderFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.calendar:
                break;
            case R.id.photo:
//                activity.startActivity(new Intent(activity, com.example.dcc.CustomizedListViewFrag.class));
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                newer = new CustomizedListViewFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.report:
//                activity.startActivity(new Intent(activity, EReportLauncher.class));
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                newer = new EReportLauncherFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.search:
//                activity.startActivity(new Intent(activity, LaunchActivityFrag.class));
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                newer = new LaunchActivityFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.action:
//			activity.startActivity(new Intent(activity, ActionItemFrag.class));
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                newer = new ActionItemFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.directory:
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                newer = new MembersListFragment();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.meta_button:
//                intent.setClass(activity, MetaSearch.class);
//                startActivity(intent);
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
                newer = new MetaSearchFrag();
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
        }
    }

    public void onBackPressed() {
        activity.finish();
    }
}