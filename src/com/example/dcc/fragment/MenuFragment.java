package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dcc.EasterEggs;
import com.example.dcc.R;
import com.example.dcc.Zork;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.OnButtonSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is the left aligned navigation bar used to allow the
 * user to transition between fragments.
 */

public class MenuFragment extends Fragment implements OnClickListener {

    //Button createButton;
    private Activity activity;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public ListView mList;
    public Button voiceButton;
    View view;
    boolean director;
    private OnButtonSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(ObjectStorage.getUser().getName().equalsIgnoreCase("brandon harmon") || ObjectStorage.getUser().getName().equalsIgnoreCase("sam bevins")
                || ObjectStorage.getUser().getName().equalsIgnoreCase("robert williams")){

            view = inflater.inflate(R.layout.menu_fragment_director,
                    container, false);
            Button searchB = (Button) view.findViewById(R.id.search);
            searchB.setOnClickListener(this);
        }else{
            view = inflater.inflate(R.layout.menu_fragment,
                    container, false);
            director = true;
        }
        activity = getActivity();
        assert view != null;

        //Get all buttons from the view
        Button newsB = (Button) view.findViewById(R.id.news);
//        Button photoB = (Button) view.findViewById(R.id.photo);
        Button reportB = (Button) view.findViewById(R.id.report);
        Button actionB = (Button) view.findViewById(R.id.action);
        Button directoryB = (Button) view.findViewById(R.id.directory);
        Button delete = (Button) view.findViewById(R.id.logout);

        voiceButton = (Button) view.findViewById(R.id.button);
        mList = (ListView) view.findViewById(R.id.list);

        //Set on click listeners
        newsB.setOnClickListener(this);
//        photoB.setOnClickListener(this);
        reportB.setOnClickListener(this);
        actionB.setOnClickListener(this);
        directoryB.setOnClickListener(this);
        delete.setOnClickListener(this);

        // Check to see if a recognition activity is present
        // if running on AVD virtual device it will give this message. The mic
        // required only works on an actual android device//
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent
                (RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            voiceButton.setOnClickListener(this);
        } else {
            voiceButton.setEnabled(false);
            voiceButton.setText("Recognizer not present");
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button){
            startVoiceRecognitionActivity();
        }else{
        listener.onMenuButtonSelected(view.getId());
        }
    }

    /**
     * Fire an intent to start the speech recognition activity.
     */
    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, 1234);
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

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
                && resultCode == -1) {//-1 equals RESULT_OK
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, matches));
            /*
            matches is the result of voice input. It is a list of what the
            user possibly said.
            Using an if statement for the keyword you want to use allows the
            use of any activity if keywords match
            it is possible to set up multiple keywords to use the same
            activity so more than one word will allow the user
            to use the activity (makes it so the user doesn't have to
            memorize words from a list)
            to use an activity from the voice input information simply use
            the following format;
            if (matches.contains("keyword here") { startActivity(new
            == Intent("name.of.manifest.ACTIVITY")
            */
            Log.e("", matches.toString());
            char izard[] = new char[matches.toString().length()];
            izard = matches.toString().toCharArray();

            if (matches.contains("news")) {
                listener.onMenuButtonSelected(R.id.news);
            }
//            else if (matches.contains("gallery")) {
//                listener.onMenuButtonSelected(R.id.photo);
//            }
//            else if (matches.contains("photo")) {
//                listener.onMenuButtonSelected(R.id.photo);
//            }
            else if (matches.contains("report")) {
                listener.onMenuButtonSelected(R.id.report);
            }
            else if (matches.contains("ereport")) {
                listener.onMenuButtonSelected(R.id.report);
            }
            else if (matches.contains("action")) {
                listener.onMenuButtonSelected(R.id.action);
            }
            else if (matches.contains("action item")) {
                listener.onMenuButtonSelected(R.id.action);
            }
            else if (matches.contains("item")) {
                listener.onMenuButtonSelected(R.id.action);
            }
            else if (matches.contains("directory")) {
                listener.onMenuButtonSelected(R.id.directory);
            }
            else if (matches.contains("email")) {
                listener.onMenuButtonSelected(R.id.directory);
            }
            else if (matches.contains("contacts")) {
                listener.onMenuButtonSelected(R.id.directory);
            }
            else if (matches.contains("close")) {
                activity.finish();
            }
            else if (matches.contains("stop")) {
                activity.finish();
            }
            else if (matches.contains("finish")) {
                activity.finish();
            }
            else if (matches.contains("create") && director) {
                listener.onMenuButtonSelected(R.id.createaction);
            }
            else if (matches.contains("post") && director) {
                listener.onMenuButtonSelected(R.id.createaction);
            }
            else if (matches.contains("search") && director) {
                listener.onMenuButtonSelected(R.id.search);
            }
            else if (matches.contains("free")) {
                EasterEggs.sweeet.start();
            }
            else if (matches.contains("tacos")) {
                EasterEggs.sweeet.start();
            }
            else if (matches.contains("give me a taco")) {
                EasterEggs.sweeet.start();
            }
            else if (matches.contains("I wish I could talk to ghosts")) {
                EasterEggs.sweeet.start();
            }
            else if (matches.contains("I have a mustache too")) {
                EasterEggs.speakItalian.start();
            }
            else if (matches.contains("spaghetti")) {
                EasterEggs.speakItalian.start();
            }
            else if (matches.contains("I have a glandular problem")) {
                EasterEggs.glandular.start();
            }
            else if (matches.contains("tuba")) {
                EasterEggs.glandular.start();
            }
            else if (matches.contains("crap")) {
                EasterEggs.doh.start();
            }
            else if (matches.contains("whoops")) {
                EasterEggs.doh.start();
            }
            else if (matches.contains("zork")) {
                Fragment newer = new Zork();
                listener.launchFragment(newer);
            }
            else if (matches.contains("dark")) {
                Fragment newer = new Zork();
                listener.launchFragment(newer);
            }
            else if (matches.contains("dork")) {
                Fragment newer = new Zork();
                listener.launchFragment(newer);
            }
            else if (matches.contains("york")) {
                Fragment newer = new Zork();
                listener.launchFragment(newer);
            }
            else if (matches.contains("let's play a game")) {
                Fragment newer = new Zork();
                listener.launchFragment(newer);
            }
            else if (matches.contains("button to take down the discovery lab server")) {
               EasterEggs.yet.start();
            }
            else if (matches.contains("break server")) {
                EasterEggs.yet.start();
            }
//            else if (izard.) {
//                EasterEggs.yet.start();
//            }
            else if (matches.contains("server off line")) {
                EasterEggs.yet.start();
            }
            else if (matches.contains("do you need a button to take the server off line")) {
                EasterEggs.yet.start();
            }
            else if (matches.contains("I'm tired")) {
                EasterEggs.hacking.start();
            }
            else if (matches.contains("I have been programming all day")) {
                EasterEggs.hacking.start();
            }
            else if (matches.contains("any key")) {
                EasterEggs.hacking.start();
            }
            else if (matches.contains("where's the any key")) {
                EasterEggs.hacking.start();
            }
            else if (matches.contains("denial")) {
                EasterEggs.grief.start();
            }
            else if (matches.contains("what are the five stages of grief")) {
                EasterEggs.grief.start();
            }
            else if (matches.contains("the fastest recovery")) {
                EasterEggs.grief.start();
            }
            else if (matches.contains("I won the lottery")) {
                EasterEggs.makeBelieve.start();
            }
            else if (matches.contains("will I win the lottery")) {
                EasterEggs.makeBelieve.start();
            }
            else if (matches.contains("I won the lottery")) {
                EasterEggs.makeBelieve.start();
            }
            else if (matches.contains("Can I get free food")) {
                EasterEggs.makeBelieve.start();
            }
        }
    }
}