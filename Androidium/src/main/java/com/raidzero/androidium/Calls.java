package com.raidzero.androidium;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

/**
 * Created by raidzero on 9/12/13.
 */
public class Calls extends Activity {
    public static int getMissedCallCount (Context context)  {
        int count = 0; // feel sorry for you if have over 2 billion missed calls :)

        String[] projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
        String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + "=1";
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where, null, null);

        if (cursor != null) {
            try {
                count = cursor.getCount();
            } finally {
                cursor.close();
            }
        }
        return count;
    }
}
