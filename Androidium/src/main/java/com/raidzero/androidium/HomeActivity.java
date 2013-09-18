/*******************************************************************************
 *
 * * Copyright 9/10/2013, raidzero
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    static final String tag = "androidium";

    private boolean missedNumbers_enabled;
    private static final Boolean debug = false;

    private static final int pkg_request_code = 1;
    private static final int act_request_code = 2;

    int missedCalls, unreadSMS;

    final ArrayList<ListItem> listItems = new ArrayList<ListItem>();

    private LinearLayout layout;
    private HomeView homeView;
    public static RelativeLayout center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logWrapper("onCreate...");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_scroll);

        homeView = (HomeView) findViewById(R.id.home_view);

        homeView.setOverScrollMode(View.OVER_SCROLL_NEVER); // no overscrolling, just looks weird

        layout = (LinearLayout) findViewById(R.id.llayout);
        center = (RelativeLayout) findViewById(R.id.center_of_screen);


        // set up list items with their launch intents
        listItems.add(new ListItem("google", "com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.SearchActivity", getResources().getDrawable(R.drawable.google)));
        listItems.add(new ListItem("phone", "com.android.dialer", "com.android.dialer.DialtactsActivity", getResources().getDrawable(R.drawable.phone)));
        listItems.add(new ListItem("people", "com.android.contacts", "com.android.contacts.activities.PeopleActivity", getResources().getDrawable(R.drawable.people)));
        listItems.add(new ListItem("messages", "com.android.mms", "com.android.mms.ui.ConversationList", getResources().getDrawable(R.drawable.messages)));
        listItems.add(new ListItem("e-mail", "com.google.android.gm", "com.google.android.gm.GmailActivity", getResources().getDrawable(R.drawable.email)));
        listItems.add(new ListItem("weather", "com.levelup.beautifulwidgets", "com.levelup.beautifulwidgets.full.activities.ForecastActivityFull", getResources().getDrawable(R.drawable.weather)));
        listItems.add(new ListItem("applications", "com.raidzero.androidium", "com.raidzero.androidium.AppDrawer", getResources().getDrawable(R.drawable.applications)));
        listItems.add(new ListItem("calculator", "com.android.calculator2", "com.android.calculator2.Calculator", getResources().getDrawable(R.drawable.calculator)));
        listItems.add(new ListItem("camera", "com.android.gallery3d", "com.android.camera.CameraActivity", getResources().getDrawable(R.drawable.camera)));
        listItems.add(new ListItem("pictures", "com.android.gallery3d", "com.android.gallery3d.app.Gallery", getResources().getDrawable(R.drawable.pictures)));
        listItems.add(new ListItem("music", "github.daneren2005.dsub", "github.daneren2005.dsub.activity.MainActivity", getResources().getDrawable(R.drawable.music)));
        listItems.add(new ListItem("finances", "com.chase.sig.android", "com.chase.sig.android.activity.HomeActivity", getResources().getDrawable(R.drawable.finances)));
        listItems.add(new ListItem("internet", "com.android.browser", "com.android.browser.BrowserActivity", getResources().getDrawable(R.drawable.internet)));
        listItems.add(new ListItem("calendar", "com.android.calendar", "com.android.calendar.AllInOneActivity", getResources().getDrawable(R.drawable.calendar)));
        listItems.add(new ListItem("play", "com.android.vending", "com.google.android.finsky.activities.MainActivity", getResources().getDrawable(R.drawable.play)));

        updateView();
    }

    private void updateView() {
        logWrapper("updateView() called.");

        updatedMissedItems();
        loadPrefs();

        logWrapper("Found " + listItems.size() + " home screen items");

        for (int p=0; p<4; p++) {
            TextView pad = (TextView) View.inflate(this, R.layout.list_item, null);
            pad.setBackground(null);
            pad.setTextSize(42);
            layout.addView(pad);
        }

        // iterate over ArrayList, item is final so it can be accessed in click listener inner class
        for (final ListItem item : listItems) {
            final String itemName = item.getItemName();
            final Boolean itemVisible = item.isVisible();

            logWrapper("Working on item " + itemName);
            logWrapper("item " + itemName + " visible: " + itemVisible);

            // skip invisible items
            if (!itemVisible) {
                continue;
            }

            // get a TextView from the item
            //TextView tv = item.getTextView(this, missedNumbers_enabled);

            RelativeLayout cv = item.getComplexView(this, missedNumbers_enabled);
            layout.addView(cv);


            // add touch listener
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (homeView.isViewHighlighted(v)) {

                        animateTouch(v);
                        final Intent i = item.getLaunchIntent();
                        logWrapper("Recieved launch intent for " + itemName);
                        try {
                            if (!itemName.equals("applications")) {
                                i.setAction(Intent.ACTION_MAIN);
                                i.addCategory(Intent.CATEGORY_LAUNCHER);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            } else {
                                 // do not run the app drawer in the main thread, I think :)
                                new Thread(new Runnable() {
                                    public void run() {
                                        startActivity(i);
                                    }
                                });
                            }

                            startActivity(i);
                        } catch (Exception e) {
                            // tell the user what happened
                            showToast(e.getMessage());
                        }
                    }
                }
            };

            cv.setOnClickListener(clickListener);


        }
        for (int p=0; p<4; p++) {
            TextView pad = (TextView) View.inflate(this, R.layout.list_item, null);
            pad.setBackground(null);
            pad.setTextSize(42);
            layout.addView(pad);
        }
    }

    public void setItemVisible(String name, Boolean visible) {
        for (ListItem item : listItems) {
            if (item.getItemName().equals(name)) {
                item.setVisible(visible);
            }
        }
    }

    public void loadPrefs() {
        logWrapper("loadPrefs() started.");

        // get preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        missedNumbers_enabled = prefs.getBoolean("enable_missed_numbers", true);
        setItemVisible("phone", prefs.getBoolean("enable_phone", true));
        setItemVisible("people", prefs.getBoolean("enable_people", true));
        setItemVisible("messages", prefs.getBoolean("enable_messages", true));
        setItemVisible("e-mail", prefs.getBoolean("enable_email", true));
        setItemVisible("applications", prefs.getBoolean("enable_applications", true));
        setItemVisible("calculator", prefs.getBoolean("enable_calculator", true));
        setItemVisible("camera", prefs.getBoolean("enable_camera", true));
        setItemVisible("pictures", prefs.getBoolean("enable_pictures", true));
        setItemVisible("internet", prefs.getBoolean("enable_internet", true));
        setItemVisible("calendar", prefs.getBoolean("enable_calendar", true));
        setItemVisible("play", prefs.getBoolean("enable_play", true));

        logWrapper("loadPrefs() finished.");
    }

    public void updatedMissedItems() {
        missedCalls = Calls.getMissedCallCount(this);
        unreadSMS = Messages.getUnreadSmsCount(this);
        logWrapper("Missed calls: " + missedCalls);
        logWrapper("New SMS: " + unreadSMS);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    // animation for a view
    public void animateTouch(View v) {
        Animation touchEffectOut = AnimationUtils.loadAnimation(this, R.anim.item_touch_fadeout);
        if (touchEffectOut != null) {
            v.startAnimation(touchEffectOut);
        }
        Animation touchEffectIn = AnimationUtils.loadAnimation(this, R.anim.item_touch_fadein);
        if (touchEffectIn != null) {
            v.startAnimation(touchEffectIn);
        }
    }

    // open menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // open settings menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_android_settings:
                intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                return true;
            case R.id.action_manage_apps:
                intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                startActivity(intent);
                return true;
            case R.id.action_wallpaper:
                intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(intent);
                return true;
            case R.id.action_add:
                intent = new Intent(this, PackageBrowser.class);
                startActivityForResult(intent, pkg_request_code);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case pkg_request_code: // handle a package name sent back
                if (resultCode == RESULT_OK) {
                    String pkgName = data.getData().toString();
                    Toast.makeText(this, pkgName,
                            Toast.LENGTH_SHORT).show();

                    // now launch the activity browser for this package name
                    Intent i = new Intent(this, ActivityBrowser.class);
                    i.putExtra("pkgname", pkgName);
                    startActivityForResult(i, act_request_code);
                }
                break;
            case act_request_code: // handle an activity name sent back
                if (resultCode == RESULT_OK) {
                    String actName = data.getData().toString();
                    Toast.makeText(this, actName,
                            Toast.LENGTH_SHORT).show();
                }
                // TODO: actually add this to the list of stuff
                break;
        }
    }

    protected void logWrapper(String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    @Override
    public void onBackPressed() {
        updateView();
        // do nothing, we are already home, cant go any further
    }

    @Override public void onPause() {
        super.onResume();
        loadPrefs();
    }

    @Override public void onResume() {
        super.onResume();
        loadPrefs();
    }

    @Override
    public void onRestart() {
        super.onResume();
        loadPrefs();
    }

    @Override
    public void onStart() {
        super.onResume();
        loadPrefs();
    }
}
