package com.S2013.dcc;

import android.util.Log;

/**
 * Used to store any methods that are used widely across multiple classes (i.e. Logging)
 * Created by Harmon on 8/5/13.
 */
public class Utility {
    private static String LOG_NAME = "DCC.S2013";

    public synchronized static void logError(String msg){
        Log.e(LOG_NAME, msg);
    }

    public synchronized static void logStatus(String msg){
        Log.i(LOG_NAME, msg);
    }
}
