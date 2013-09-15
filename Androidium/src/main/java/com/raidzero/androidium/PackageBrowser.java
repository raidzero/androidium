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
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageBrowser extends Activity {

    private static final String tag = "Androidium";
    private static ArrayList<String> pkgNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // get packages
        pkgNames = getPackageNames();
        Collections.sort(pkgNames);

        LinearLayout layout = (LinearLayout) findViewById(R.id.act_llayout); // where items go

        for (String pkgName : pkgNames) {
            TextView tv = (TextView) View.inflate(this, R.layout.list_item, null);
            tv.setTextSize(15);
            tv.setShadowLayer(0, 0, 0, 0); // no shadow
            tv.setText(pkgName);

            // add listener for pkgName
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


    public ArrayList<String> getPackageNames() {
        /*List<PackageInfo> pInfos = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES); // grab all activities

        for (PackageInfo pInfo : pInfos) { // loop through them
            ActivityInfo[] aInfos = pInfo.activities;
            if (aInfos != null) {
                for (ActivityInfo activityInfo : aInfos) {
                    String pkgName = activityInfo.packageName;
                    if (pkgNames.contains(pkgName)) { // if we havent already collected it
                        continue;
                    }
                    //Log.i("PKGNAME", "Adding pkg to list: " + pkgName );
                    pkgNames.add(pkgName);
                }
            }
        }*/
        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : apps) {
            //Log.d(tag, "package name: " + appInfo.packageName);
            pkgNames.add(appInfo.packageName);
        }
        return pkgNames;
    }
}
