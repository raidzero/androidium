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

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListItem extends HomeActivity {
    private String item_name;
    private String pkgName;
    private String activityName;
    private Boolean visible = true;
    private Drawable mainIcon;

    public ListItem(String name, String pkg, String activity, Drawable icon) {
        this.item_name = name;
        this.pkgName = pkg;
        this.activityName = activity;
        this.mainIcon = icon;
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

    public Drawable getMainIcon() {
        return mainIcon;
    }


    public TextView getTextView(Context context, boolean missedNumbersAvailable) {
        TextView retView = (TextView) View.inflate(context, R.layout.list_item, null);
        if (item_name.equals("phone") && missedCalls > 0 && missedNumbersAvailable) {
            // add missed calls in superscript
            retView.setText(Html.fromHtml("phone <sup><small>" + missedCalls + "</small></sup>"));
        } else if (item_name.equals("messages") && unreadSMS > 0 && missedNumbersAvailable) {
            // add unread SMS in superscript
            retView.setText(Html.fromHtml("messages <sup><small>" + unreadSMS + "</small></sup>"));
        } else {
            retView.setText(item_name);
        }
        return retView;
    }

    public RelativeLayout getComplexView(Context context, boolean missedNumbersAvailable) {
        RelativeLayout complexView = (RelativeLayout) View.inflate(context, R.layout.complex_list_item, null);

        // loop through its children
        int numChildren = complexView.getChildCount();
        for (int i=0; i<numChildren; i++) {
            View childView = complexView.getChildAt(i);

            // is it a text view?
            if (childView instanceof TextView) {
                if (item_name.equals("phone") && missedCalls > 0 && missedNumbersAvailable) {
                    // add missed calls in superscript
                    ((TextView) childView).setText(Html.fromHtml("phone <sup><small>" + missedCalls + "</small></sup>"));
                } else if (item_name.equals("messages") && unreadSMS > 0 && missedNumbersAvailable) {
                    // add unread SMS in superscript
                    ((TextView) childView).setText(Html.fromHtml("messages <sup><small>" + unreadSMS + "</small></sup>"));
                } else {
                    ((TextView) childView).setText(item_name);
                }
                ((TextView) childView).setText(item_name);
            }

            // imageview?
            if (childView instanceof ImageView) {
                ((ImageView) childView).setImageDrawable(mainIcon);
                childView.setVisibility(View.GONE);
            }
        }

        return complexView;
    }

    public Intent getLaunchIntent() {
        logWrapper(this.getItemName() + ".getLaunchIntent() called");

        Intent launchIntent = new Intent();
        launchIntent.setClassName(pkgName, activityName);
        return launchIntent;
    }
}
