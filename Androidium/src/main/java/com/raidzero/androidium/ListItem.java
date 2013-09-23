/*******************************************************************************
 *
 * * Copyright 9/11/2013, raidzero
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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    public TextView getTextView(Context context, boolean missedNumbersAvailable) {
        TextView retView = (TextView) View.inflate(context, R.layout.list_item, null);
        retView.setText(item_name);
        return retView;
    }

    public String toString() {
        return item_name + " " + pkgName + " " + activityName;
    }

    public RelativeLayout getComplexView(Context context, boolean missedNumbersAvailable) {
        RelativeLayout complexView = (RelativeLayout) View.inflate(context, R.layout.complex_list_item, null);

        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgName, activityName));
        ResolveInfo ri = pm.resolveActivity(intent, 0);

        // loop through its children
        int numChildren = complexView.getChildCount();
        for (int i=0; i<numChildren; i++) {
            View childView = complexView.getChildAt(i);

            // is it a text view?
            if (childView instanceof TextView) {
                ((TextView) childView).setText(item_name);
            }

            // imageview?
            if (childView instanceof ImageView) {
                // get icon that matches package name
                try {
                    Drawable appIcon = ri.loadIcon(pm);
                    ((ImageView) childView).setImageDrawable(appIcon);
                } catch (Exception e) {
                    // dont do anything, leave it blank
                    logWrapper("icon not set: " + e.getMessage());
                }

                childView.setVisibility(View.GONE);
            }
        }

        return complexView;
    }

    public Intent getLaunchIntent() {
        logWrapper(this.getItemName() + ".getLaunchIntent() called");
        Intent launchIntent = new Intent();
        launchIntent.setClassName(pkgName, activityName);
        logWrapper("getLaunchIntent() returning " + pkgName + "." + activityName);
        return launchIntent;
    }
}
