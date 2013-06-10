package com.example.dcc.helpers;

import android.app.Fragment;

/**
 * Created by Harmon on 6/7/13.
 */
public interface OnButtonSelectedListener {
        public void onMenuButtonSelected(int buttonId);
        public void launchFragment(Fragment fragment);
        public void stackFragment(Fragment fragment);
}
