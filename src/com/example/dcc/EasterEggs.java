package com.example.dcc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by Sam on 5/31/13.
 */

public class EasterEggs extends Activity {

    //declare MediaPlayer objects
    public static MediaPlayer sweeet;
    public static MediaPlayer giveYouUp;
    public static MediaPlayer speakItalian;
    public static MediaPlayer glandular;
    public static MediaPlayer doh;
    public static MediaPlayer fourPeters;
    public static MediaPlayer hacking;
    public static MediaPlayer makeBelieve;
    public static MediaPlayer grief;
    public static MediaPlayer woohoo;
    public static MediaPlayer yet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //initialize MediaPlayer objects
        sweeet = MediaPlayer.create(getApplicationContext(), R.raw.sweet_edit);
        speakItalian = MediaPlayer.create(getApplicationContext(), R.raw.italian);
        glandular = MediaPlayer.create(getApplicationContext(), R.raw.glandular_first);
        doh = MediaPlayer.create(getApplicationContext(), R.raw.doh);
        fourPeters = MediaPlayer.create(getApplicationContext(), R.raw.four_peters);
        hacking = MediaPlayer.create(getApplicationContext(), R.raw.hacking);
        makeBelieve = MediaPlayer.create(getApplicationContext(), R.raw.makebelieve);
        grief = MediaPlayer.create(getApplicationContext(), R.raw.stagesofgrief);
        woohoo = MediaPlayer.create(getApplicationContext(), R.raw.woohoo);
        yet = MediaPlayer.create(getApplicationContext(), R.raw.yet);

        //this class is autoloaded and then is automatically switched to the main.
        startActivity(new Intent(this, MainActivityFrag.class));
        finish();
    }

}