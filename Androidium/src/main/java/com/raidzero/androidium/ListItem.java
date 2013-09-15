package com.raidzero.androidium;

import android.content.Intent;

/**
 * Created by raidzero on 9/11/13. Updated 9/5/13 12:38 PM
 */
public class ListItem extends HomeActivity {
    private String item_name;
    private String pkgName;
    private String activityName;
    private Boolean visible = true;

    public ListItem(String name, String pkg, String activity) {
        this.item_name = name;
        this.pkgName = pkg;
        this.activityName = activity;
    }

    public void setVisible(Boolean b)
    {
        visible = b;
        logWrapper(this.getItemName() + " visible: " + visible );
    }

    public Boolean isVisible()
    {
        return visible;
    }

    public String getItemName() {
        return item_name;
    }

    public Intent getLaunchIntent() {
        logWrapper(this.getItemName() + ".getLaunchIntent() called");

        Intent launchIntent = new Intent();
        launchIntent.setClassName(pkgName, activityName);
        return launchIntent;
    }
}
