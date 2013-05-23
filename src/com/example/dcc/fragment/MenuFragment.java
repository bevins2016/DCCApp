package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.example.dcc.CustomizedListViewFrag;
import com.example.dcc.EReportLauncherFrag;
import com.example.dcc.LaunchActivityFrag;
import com.example.dcc.R;
import com.example.dcc.fragment.ActionItemFrag;
import com.example.dcc.fragment.MembersListFragment;
import com.example.dcc.fragment.NewsListFragment;
import com.example.dcc.helpers.ObjectStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuFragment extends Fragment implements OnClickListener, TextToSpeech.OnInitListener{

    private Button newsB;
    //private Button calB;
    private Button photoB;
    private Button reportB;
    private Button actionB;
    private Button directoryB;
    private Button searchB;
    private ToggleButton toggleSound;

    private Activity activity;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public ListView mList;
    public Button voiceButton;

    // TTS object
    public TextToSpeech myTTS;
    // status check code
    public int MY_DATA_CHECK_CODE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment,
                container, false);
        activity = getActivity();
        assert view != null;
        newsB = (Button) view.findViewById(R.id.news);
        //calB = (Button) view.findViewById(R.id.calendar);
        photoB = (Button) view.findViewById(R.id.photo);
        reportB = (Button) view.findViewById(R.id.report);
        actionB = (Button) view.findViewById(R.id.action);
        directoryB = (Button) view.findViewById(R.id.directory);
        searchB = (Button) view.findViewById(R.id.search);
        toggleSound = (ToggleButton) view.findViewById(R.id.toggle);
        voiceButton = (Button) view.findViewById(R.id.button);
        mList = (ListView) view.findViewById(R.id.list);


        // check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        newsB.setOnClickListener(this);
        //calB.setOnClickListener(this);
        photoB.setOnClickListener(this);
        reportB.setOnClickListener(this);
        actionB.setOnClickListener(this);
        directoryB.setOnClickListener(this);
        searchB.setOnClickListener(this);
        toggleSound.setOnClickListener(this);

        // Check to see if a recognition activity is present
        // if running on AVD virtual device it will give this message. The mic
        // required only works on an actual android device//
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            voiceButton.setOnClickListener(this);
        } else {
            voiceButton.setEnabled(false);
            voiceButton.setText("Recognizer not present");
        }

        return view;
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.news:
//                getActivity().startActivity(new Intent(getActivity(), AndroidRssReader.class));
                news();
                break;
            case R.id.photo:
//                activity.startActivity(new Intent(activity, com.example.dcc.CustomizedListViewFrag.class));
                photo();
                break;
            case R.id.report:
//                activity.startActivity(new Intent(activity, EReportLauncher.class));
                report();
                break;
            case R.id.search:
//                activity.startActivity(new Intent(activity, LaunchActivityFrag.class));
                search();
                break;
            case R.id.action:
//			activity.startActivity(new Intent(activity, ActionItemFrag.class));
                action();
                break;
            case R.id.directory:
                directory();;
                break;
            case R.id.toggle:
                toggle();
                break;
            case R.id.button:
                speakWords("Speak Now");
                startVoiceRecognitionActivity(); // call for voice recognition
                // activity
                break;
        }

    }

    public void news(){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
<<<<<<< HEAD
        manager = activity.getFragmentManager();
        transaction = manager.beginTransaction();
=======
>>>>>>> af979473e0a1844d901ff9dc16ec7798f6934b45

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new NewsListFragment();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void photo(){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new CustomizedListViewFrag();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void report(){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new EReportLauncherFrag();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void search(){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new LaunchActivityFrag();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void action(){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new ActionItemFrag();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void directory(){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new MembersListFragment();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }
    public void toggle(){
        if(toggleSound.isChecked()){
            speakWords("Access Granted. Welcome ");
        }else{
            speakWords("Voice Prompts Off");
        }
    }

    // speak the user text
    private void speakWords(String speech) {

        // speak straight away
        if (myTTS != null) {
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
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
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
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

            if (matches.contains("news")) {
                news();
            }
            if (matches.contains("gallery")) {
                photo();
            }
            if (matches.contains("photo")) {
                photo();
            }
            if (matches.contains("report")) {
                report();
            }
            if (matches.contains("ereport")) {
                report();
            }
            if (matches.contains("search")) {
                search();
            }
            if (matches.contains("action")) {
                action();
            }
            if (matches.contains("action item")) {
                action();
            }
            if (matches.contains("item")) {
                action();
            }
            if (matches.contains("directory")) {
                directory();
            }
            if (matches.contains("email")) {
                directory();
            }
            if (matches.contains("contacts")) {
                directory();
            }
            if (matches.contains("close")) {
                activity.finish();
            }
            if (matches.contains("stop")) {
                activity.finish();
            }
            if (matches.contains("finish")) {
                activity.finish();
            }
            if (matches.contains("toggle")) {
                toggle();
            }
            if (matches.contains("switch")) {
                toggle();
            }
            if (matches.contains("speak")) {
                toggle();
            }

        }


        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(activity, this);
            } else {
                // no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // setup TTS
    public void onInit(int initStatus) {

        // check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(activity, "Sorry! Text To Speech failed...",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }
}
