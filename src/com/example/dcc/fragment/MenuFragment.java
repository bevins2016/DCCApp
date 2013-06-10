package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dcc.CustomizedListViewFrag;
import com.example.dcc.EReportLauncherFrag;
import com.example.dcc.EasterEggs;
import com.example.dcc.LaunchActivityFrag;
import com.example.dcc.R;
import com.example.dcc.Zork;
import com.example.dcc.helpers.ObjectStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is the left aligned navigation bar used to allow the
 * user to transition between fragments.
 */
public class MenuFragment extends Fragment implements OnClickListener{

    private ToggleButton toggleSound;
    //Button createButton;
    private Activity activity;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public ListView mList;
    public Button voiceButton;
    View view;
    boolean director;

    SoundPool soundPool;
    int sound;

    String login;
    // TTS object
    public TextToSpeech myTTS;
    // status check code
    public int MY_DATA_CHECK_CODE = 0;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

/*        if(!ObjectStorage.getUser().getName().equalsIgnoreCase("sam bevins") || !ObjectStorage.getUser().getName().equalsIgnoreCase("brandon harmon")){

            view = inflater.inflate(R.layout.menu_fragment,
                    container, false);
        }else{

        login = ObjectStorage.getUser().getName();*/
        if(ObjectStorage.getUser().getName().equalsIgnoreCase("brandon harmon") || ObjectStorage.getUser().getName().equalsIgnoreCase("sam bevins")){

            view = inflater.inflate(R.layout.menu_fragment_director,
                    container, false);
            Button searchB = (Button) view.findViewById(R.id.search);
            searchB.setOnClickListener(this);
        }else{
            view = inflater.inflate(R.layout.menu_fragment,
                    container, false);


            director = true;
        }

//        mp = MediaPlayer.create(activity, R.raw.sweet);
//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                // TODO Auto-generated method stub
//                mp.release();
//            }
//
//        });

        activity = getActivity();
        assert view != null;

        //Get all buttons from the view
        Button newsB = (Button) view.findViewById(R.id.news);
        Button photoB = (Button) view.findViewById(R.id.photo);
        Button reportB = (Button) view.findViewById(R.id.report);
        Button actionB = (Button) view.findViewById(R.id.action);
        Button directoryB = (Button) view.findViewById(R.id.directory);
        Button delete = (Button) view.findViewById(R.id.logout);



        voiceButton = (Button) view.findViewById(R.id.button);
        mList = (ListView) view.findViewById(R.id.list);

        // createButton = (Button) view.findViewById(R.id.createaction);
        // createButton.setOnClickListener(this);



        //Set on click listeners
        newsB.setOnClickListener(this);
        photoB.setOnClickListener(this);
        reportB.setOnClickListener(this);
        actionB.setOnClickListener(this);
        directoryB.setOnClickListener(this);
        delete.setOnClickListener(this);
        //searchB.setOnClickListener(this);



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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.news:
                news();
//                mp.start();
//                EasterEggs.glandular.start();
                break;
            case R.id.photo:
                photo();
                break;
            case R.id.report:
                report();
                break;
            case R.id.search:
                search();
                break;
            case R.id.action:
                action();
                break;
            case R.id.directory:
                directory();
                break;
            case R.id.logout:
                SharedPreferences mPreferences;
                mPreferences = activity.getSharedPreferences("CurrentUser", 0);
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.putString("UserName", "");
                editor.putString("PassWord", "");
                editor.commit();

                Toast.makeText(activity, "Login data cleared",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.createaction:
                FragmentManager manager2 = activity.getFragmentManager();
                FragmentTransaction transaction2 = manager2.beginTransaction();

                Fragment newerq = new CreateActionItemFrag();
                ObjectStorage.setFragment(R.id.fragmentcontainerright, newerq);

                transaction2.replace(R.id.fragmentcontainerright,
                        ObjectStorage.getFragment(R.id.fragmentcontainerright));
                transaction2.commit();
                break;
            case R.id.button:
//                speakWords("Speak Now");
                startVoiceRecognitionActivity(); // call for voice recognition
                break;
        }

    }

    /**
     * Method that launches the fragment into the right container;
     * @param newer
     */
    private void launchFragment(Fragment newer){
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();
    }

    public void news(){
        launchFragment(new NewsListFragment());
    }

    public void photo(){
        Fragment newer = new CustomizedListViewFrag();
        launchFragment(newer);
    }
    public void report(){
        Fragment newer = new EReportLauncherFrag();
        launchFragment(newer);
    }
    public void search(){
        Fragment newer = new LaunchActivityFrag();
        launchFragment(newer);
    }
    public void action(){
        Fragment newer = new ActionItemFrag();
        launchFragment(newer);
    }
    public void directory(){
        Fragment newer = new MembersListFragment();
        launchFragment(newer);
    }
    public void searchReport(){
        Fragment newer = new AdminSearchFragment();
        launchFragment(newer);
    }
    public void toggle(){
//        if(toggleSound.isChecked()){
//            speakWords("Access Granted. Welcome ");
//        }else{
//            speakWords("Voice Prompts Off");
//        }
    }
    public void createActionItems(){

        Fragment newer = new CreateActionItemFrag();
        launchFragment(newer);
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
            else if (matches.contains("gallery")) {
                photo();
            }
            else if (matches.contains("photo")) {
                photo();
            }
            else if (matches.contains("report")) {
                report();
            }
            else if (matches.contains("ereport")) {
                report();
            }
            else if (matches.contains("action")) {
                action();
            }
            else if (matches.contains("action item")) {
                action();
            }
            else if (matches.contains("item")) {
                action();
            }
            else if (matches.contains("directory")) {
                directory();
            }
            else if (matches.contains("email")) {
                directory();
            }
            else if (matches.contains("contacts")) {
                directory();
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
            else if (matches.contains("toggle")) {
                toggle();
            }
            else if (matches.contains("switch")) {
                toggle();
            }
            else if (matches.contains("create") && director) {
                createActionItems();
            }
            else if (matches.contains("post") && director) {
                createActionItems();
            }
            else if (matches.contains("search") && director) {
                searchReport();
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
            else if (matches.contains("oh crap")) {
                EasterEggs.doh.start();
            }
            else if (matches.contains("whoops")) {
                EasterEggs.doh.start();
            }

            else if (matches.contains("zork")) {
                Fragment newer = new Zork();
                launchFragment(newer);
            }
            else if (matches.contains("dark")) {
                Fragment newer = new Zork();
                launchFragment(newer);
            }
            else if (matches.contains("dork")) {
                Fragment newer = new Zork();
                launchFragment(newer);
            }
            else if (matches.contains("york")) {
                Fragment newer = new Zork();
                launchFragment(newer);
            }
            else if (matches.contains("let's play a game")) {
                Fragment newer = new Zork();
                launchFragment(newer);
            }
            else if (matches.contains("button to take down the discovery lab server")) {
               EasterEggs.yet.start();
            }
            else if (matches.contains("break server")) {
                EasterEggs.yet.start();
            }
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

//        if (requestCode == MY_DATA_CHECK_CODE) {
//            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
//                // the user has the necessary data - create the TTS
//                myTTS = new TextToSpeech(activity, this);
//            } else {
//                // no data - install it now
//                Intent installTTSIntent = new Intent();
//                installTTSIntent
//                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//                startActivity(installTTSIntent);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    // setup TTS
//    public void onInit(int initStatus) {
//
//        // check for successful instantiation
//        if (initStatus == TextToSpeech.SUCCESS) {
//            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
//                myTTS.setLanguage(Locale.US);
//        } else if (initStatus == TextToSpeech.ERROR) {
//            Toast.makeText(activity, "Sorry! Text To Speech failed...",
//                    Toast.LENGTH_LONG).show();
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //myTTS.shutdown();
//    }
    }
}
