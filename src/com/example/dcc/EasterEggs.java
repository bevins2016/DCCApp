package com.example.dcc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by Sam on 5/31/13.
 */




public class EasterEggs extends Activity {


    public static MediaPlayer sweeet;
    public static MediaPlayer giveYouUp;
    public static MediaPlayer speakItalian;
    public static MediaPlayer glandular;
    public static MediaPlayer doh;
    public static MediaPlayer fourPeters;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        sweeet = MediaPlayer.create(getApplicationContext(), R.raw.sweet_edit);
        speakItalian = MediaPlayer.create(getApplicationContext(), R.raw.italian);
        glandular = MediaPlayer.create(getApplicationContext(), R.raw.glandular_first);
        doh = MediaPlayer.create(getApplicationContext(), R.raw.doh);
        fourPeters = MediaPlayer.create(getApplicationContext(), R.raw.four_peters);


//        showProgress(true);

        startActivity(new Intent(this, MainActivityFrag.class));
        finish();
    }

}