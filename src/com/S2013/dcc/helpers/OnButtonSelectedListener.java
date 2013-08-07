package com.S2013.dcc.helpers;

import android.app.Fragment;

/**
 * Class implemented by an activity in order to pass button actions back to the
 * calling activity.
 *
 * Created by Harmon on 6/7/13.
 */
public interface OnButtonSelectedListener {
        public void onMenuButtonSelected(int buttonId);
        public void launchFragment(Fragment fragment);
}
