package com.raidzero.androidium;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by raidzero on 9/11/13.
 */
public class ListItem extends Activity {
    private static final String tag = "Androidium";
    private String item_name;
    private Intent launchIntent;
    private String uri;
    private String pkgName;
    private String activityName;

    // simple Constructor
    public ListItem(String name, Intent i) {
        this.item_name = name;
        this.launchIntent = i;
    }

    // uri based constructor
    public ListItem(String name, Intent i, String uri) {
        this.item_name = name;
        this.launchIntent = i;
        this.uri = uri;
    }

    // class based
    public ListItem(String name, Intent i, String pkg, String activity) {
        this.item_name = name;
        this.launchIntent = i;
        this.pkgName = pkg;
        this.activityName = activity;
    }

    public String getItemName() {
        return item_name;
    }

    public Intent getLaunchIntent() {
        Log.d(tag, this.getItemName() + ".getLaunchIntent() called");
        if (uri != null && !uri.isEmpty()) { // launch activity from uri parser
            Log.d(tag, "setting Uri to " + uri);
            launchIntent.setData(Uri.parse(uri));
        } else if (pkgName != null && activityName != null && !pkgName.isEmpty() && !activityName.isEmpty()) { // launch an activity by package/activity name
            launchIntent.setClassName(pkgName, activityName);
        }
        return launchIntent;
    }
}
