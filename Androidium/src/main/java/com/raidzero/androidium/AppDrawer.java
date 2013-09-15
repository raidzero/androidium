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

/*
 * Big thanks to CommonsWare for Launchalot.java, where most of this comes from
 * https://github.com/commonsguy/cw-omnibus/blob/master/Introspection/Launchalot/src/com/commonsware/android/launchalot/Launchalot.java
*/

package com.raidzero.androidium;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.*;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Collections;
import java.util.List;

public class AppDrawer extends Activity {
    private static final String tag = "Androidium";
    AppAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);

        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);

        main.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);

        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(pm));

        GridView gridView = (GridView) findViewById(R.id.app_grid_view);

        adapter = new AppAdapter(pm, launchables);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ResolveInfo launchable = adapter.getItem(position);
                ActivityInfo activity=launchable.activityInfo;
                ComponentName name=new ComponentName(activity.applicationInfo.packageName, activity.name);
                Intent i=new Intent(Intent.ACTION_MAIN);

                i.addCategory(Intent.CATEGORY_LAUNCHER);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                i.setComponent(name);

                startActivity(i);
            }
        });
    }

    class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm = null;

        AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
            super(AppDrawer.this, R.layout.app_drawer_entry, apps);
            this.pm = pm;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = newView(parent);
            }

            bindView(position, convertView);

            return(convertView);
        }

        private View newView(ViewGroup parent) {
            return(getLayoutInflater().inflate(R.layout.app_drawer_entry, parent, false));
        }

        private void bindView(int position, View v) {
            TextView label = (TextView) v.findViewById(R.id.app_drawer_entry_name);

            label.setText(getItem(position).loadLabel(pm)); // set the text label

            ImageView icon = (ImageView) v.findViewById(R.id.app_drawer_entry_icon);

            icon.setImageDrawable(getItem(position).loadIcon(pm)); // load the icon
        }
    }
}