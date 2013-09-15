package com.raidzero.androidium;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by raidzero on 9/14/13 12:08 AM
 */
public class Messages extends Activity {

    public static int getUnreadSmsCount(Context context) {
        String SMS_READ_COLUMN = "read";
        String UNREAD_CONDITION = SMS_READ_COLUMN + "=0";
        int count = 0;

        Uri SMS_INBOX_CONTENT_URI = Uri.parse("content://sms/inbox");

        Cursor cursor = context.getContentResolver().query(SMS_INBOX_CONTENT_URI, null, UNREAD_CONDITION, null, null);
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
