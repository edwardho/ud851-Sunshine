package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

// COMPLETE (9) Create a class called SunshineSyncUtils
public class SunshineSyncUtils {
    //  COMPLETE (10) Create a public static void method called startImmediateSync
    synchronized public static void startImmediateSync(@NonNull Context context) {

        //  COMPLETE (11) Within that method, start the SunshineSyncIntentService
        Intent intentToSync = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSync);
    }
}
