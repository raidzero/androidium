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

import android.content.Intent;

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
