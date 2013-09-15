package com.raidzero.androidium;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by raidzero on 9/14/13 5:40 PM
 */
public class ActivityBrowser extends Activity {
    private static final String tag = "Androidium";
    ArrayList<String> actNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        LinearLayout layout = (LinearLayout) findViewById(R.id.act_llayout); // where items go

        String pkgName = getIntent().getStringExtra("pkgname"); // retrieve the package name we are looking for activities in
        Log.d(tag, "pkgname from intent: " + pkgName);

        // get activities for this package
        actNames = getActivitiesForPackage(pkgName);
        Collections.sort(actNames);

        Log.d(tag, "actNames.size: " + actNames.size());

        for (String actName : actNames) {
            TextView tv = (TextView) View.inflate(this, R.layout.list_item, null);
            tv.setTextSize(15);
            tv.setShadowLayer(0, 0, 0, 0); // no shadow
            tv.setText(actName);

            // add listener for actName
            final TextView newv = tv;
            TextView.OnClickListener clickListener = new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the name that was clicked
                    String name = newv.getText().toString();
                    Log.d(tag, "name clicked: " + name);

                    // pass this name back to MainActivity
                    Intent data = new Intent();
                    data.setData(Uri.parse(name));
                    setResult(RESULT_OK, data);

                    // close this activity
                    finish();
                }
            };

            tv.setOnClickListener(clickListener);
            layout.addView(tv);
        }
    }

    public ArrayList<String> getActivitiesForPackage(String packageName) {

        int count = 0;

        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            ActivityInfo[] activities = pkgInfo.activities;
            for (ActivityInfo aInfo : activities) {
                Log.i("ACTINFO", packageName + ": " + aInfo.name);
                actNames.add(aInfo.name);
                count++;
            }
        } catch (Exception e) {
            Log.d("ActivityBrowser", "Failure");
        }

        Log.d(tag, "Retuning " + count + " activities for pkg: " + packageName);
        return actNames;
    }
}
