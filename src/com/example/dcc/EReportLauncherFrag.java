package com.example.dcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.*;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
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
    private String filename = "";
    private String fiftysevenFile = Environment.getExternalStorageDirectory()
            + "/enotebook/057s";
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

		/* Add student number and current date to filename */
        formatFilename();

		/* Check if files/folders exist */
        defaultCheck();

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
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings = new Intent(activity, SetDefaults.class);
                startActivity(settings);
                return true;
            case R.id.addStudent:
                Intent add = new Intent(activity, AddStudent.class);
                startActivity(add);
                return true;
        }
        return false;
    }

    /**
     * Starts the corresponding activity depending on the button clicked
     *
     * @param v
     */
    public void onClick(View v) {
        Intent intent = new Intent();
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
            case R.id.eReportButton:
                manager = activity.getFragmentManager();
                transaction = manager.beginTransaction();

                newer = new EReportFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

                transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction.commit();
                break;
            case R.id.Video057Button:
                File f = new File(Environment.getExternalStorageDirectory()
                        + "/enotebook/InternalStorage.txt");
                if (f.exists()) {
                    setUpVideo(57);
                } else {
                    setDefaultsPopup057();
                }
                break;
        }

    }

    /* Check if directories/files exits */
    private void defaultCheck() {
        File f = new File(Environment.getExternalStorageDirectory()
                + "/enotebook/");
        if (!f.exists()) {
            f.mkdir();
        }
        f = new File(Environment.getExternalStorageDirectory()
                + "/enotebook/InternalStorage.txt");
        if (!f.exists()) {
            setDefaultsPopup();
        }
        f = new File(Environment.getExternalStorageDirectory()
                + "/enotebook/057s");
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(Environment.getExternalStorageDirectory()
                + "/enotebook/edailys");
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(Environment.getExternalStorageDirectory()
                + "/enotebook/ereports");
        if (!f.exists()) {
            f.mkdirs();
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

    /* What to do when activity started for result finishes */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {//-1 is equal to RESULT_OK
                File oldFile = new File(getRealPathFromURI(data.getData()));
                moveFile(oldFile, fiftysevenFile);
                oldFile.delete();
            }
        }

    }

    /* Obtain the file path from it's Uri */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader cursorloader = new CursorLoader(activity, contentUri, proj,
                null, null, null);
        Cursor cursor = cursorloader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String returnString = cursor.getString(column_index);
        cursor.close();
        return returnString;
    }

    /*
     * Creates a popup dialog using alertbuilder to ask the user to fill out
     * their information in SetDefaults.txt if InternalStorage.txt does not exit
     */
    private void setDefaultsPopup() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View popup = factory.inflate(R.layout.default_popup, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setCancelable(true)
                .setTitle("Student Info")
                .setView(popup)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(
                                        activity, SetDefaults.class);
                                startActivity(intent);
                            }
                        });

        AlertDialog alertdialog = alertBuilder.create();
        alertdialog.show();
    }

    /*
     * Creates a popup dialog using alertbuilder to ask the user to fill out
     * their information in SetDefaults.txt if InternalStorage.txt does not exit
     */
    private void setDefaultsPopup057() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View popup = factory.inflate(R.layout.default_popup, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setCancelable(true)
                .setTitle("Student Info")
                .setView(popup)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                setUpVideo(57);
                            }
                        });

        AlertDialog alertdialog = alertBuilder.create();
        alertdialog.show();
    }

    /*
     * Format the filename of the video with the student's ID #, current date
     * and 057 report type tag
     */
    private void formatFilename() {
        String studentID = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(
                    "sdcard/eNotebook/InternalStorage.txt")));
            br.readLine(); // Skip Title
            studentID = br.readLine(); // Get Student ID#
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyy", Locale.US);

        filename = "/" + studentID + "_" + sdf.format(new Date()) + "_057";
        fiftysevenFile += filename;
    }

    /* Create file to save video to. If that file exists append a number. */
    private File createFile(String filename, int count) {
        filename += ".mp4";
        File file = new File(filename);
        Toast.makeText(activity, filename, Toast.LENGTH_LONG)
                .show();
        if (file.exists()) {
            count++;
            return createFile(filename + "(" + Integer.toString(count) + ")",
                    count);
        } else {
            return file;
        }
    }

    /* Move file from DCIM to /enotebook/057s */
    private void moveFile(File oldFile, String newFileLocation) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;

        File backupFile = createFile(fiftysevenFile, 0);
        try {
            backupFile.createNewFile();

            fis = new FileInputStream(oldFile);
            fos = new FileOutputStream(backupFile);
            in = fis.getChannel();
            out = fos.getChannel();

            long size = in.size();
            in.transferTo(0, size, out);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Throwable ignore) {
            }

            try {
                if (fos != null)
                    fos.close();
            } catch (Throwable ignore) {
            }

            try {
                if (in != null && in.isOpen())
                    in.close();
            } catch (Throwable ignore) {
            }

            try {
                if (out != null && out.isOpen())
                    out.close();
            } catch (Throwable ignore) {
            }
        }
        // TODO: upload file to youtube intent

    }

    public void onBackPressed() {
        activity.finish();
    }
}