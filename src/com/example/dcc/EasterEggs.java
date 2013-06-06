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

//        showProgress(true);

        startActivity(new Intent(this, MainActivityFrag.class));
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
   /* @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

//            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/
}