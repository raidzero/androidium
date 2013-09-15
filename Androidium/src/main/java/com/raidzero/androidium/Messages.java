/*******************************************************************************
 *
 * * Copyright 9/14/2013, raidzero
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * *     http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 *
 ******************************************************************************/

package com.raidzero.androidium;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
