package com.example.dcc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Sample code that invokes the speech recognition intent API.
 */
public class VoiceRecognition extends Activity implements OnClickListener,
        OnInitListener {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public ListView mList;
    public Button speakButton;
    // TTS object
    public TextToSpeech myTTS;
    // status check code
    public int MY_DATA_CHECK_CODE = 0;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle voiceinput) {
        super.onCreate(voiceinput);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.menu_fragment);

        // check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        // Get display items for later interaction

        voiceinputbuttons();

        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener(this);
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
    }

    // setup TTS
    public void onInit(int initStatus) {

        // check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle the click on the start recognition button.
     */

    public void onClick(View v) {
        startVoiceRecognitionActivity();
    }

    public void informationmenu() {
        speakWords("information screen");
        startActivity(new Intent("android.intent.action.INFOSCREEN"));
    }

    public void voicemenu() {
        speakWords("voice recognition menu");
        startActivity(new Intent("android.intent.action.RECOGNITIONMENU"));
    }

    public void mainmenu() {
        speakWords("main menu");
        startActivity(new Intent("android.intent.action.MENU"));
    }

    public void voicerecog() {
        speakWords("speak now");
        startActivity(new Intent("android.intent.action.SPEAK"));
    }

    public void voiceinputbuttons() {
        speakButton = (Button) findViewById(R.id.button);
        mList = (ListView) findViewById(R.id.list);
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
                && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches));
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")

            if (matches.contains("information")) {
                informationmenu();
            }
            if (matches.contains("info screen")) {
                informationmenu();
            }
            if (matches.contains("info")) {
                informationmenu();
            }
            if (matches.contains("about")) {
                informationmenu();
            }

            if (matches.contains("home")) {
                mainmenu();
            }
            if (matches.contains("menu")) {
                mainmenu();
            }
            if (matches.contains("home screen")) {
                mainmenu();
            }
            if (matches.contains("speak")) {
                voicerecog();
            }
            if (matches.contains("close")) {
                finish();
            }
            if (matches.contains("stop")) {
                finish();
            }
            if (matches.contains("finish")) {
                finish();
            }
            if (matches.contains("voice")) {
                voicemenu();
            }
            if (matches.contains("recognition")) {
                voicemenu();
            }
            if (matches.contains("voice recognition")) {
                voicemenu();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    // speak the user text
    public void speakWords(String speech) {

        // speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

}