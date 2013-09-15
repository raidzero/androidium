package com.raidzero.androidium;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {


    static final String[] ITEMS = new String[] { "phone", "people", "messages",
            "e-mail", "weather", "applications", "calculator", "camera",
            "pictures", "music", "finances", "internet", "calendar", "play" };

    private boolean missedNumbers_enabled;
    private static final Boolean debug = false;

    static final String tag = "androidium";

    private static final int pkg_request_code = 1;
    private static final int act_request_code = 2;

    int missedCalls, unreadSMS;

    final ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    final ArrayList<String> itemsDisplayed = new ArrayList<String>();

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_scroll);

        layout = (LinearLayout) findViewById(R.id.llayout);

        // set up list items with their launch intents
        listItems.add(new ListItem("phone", "com.android.dialer", "com.android.dialer.DialtactsActivity"));
        listItems.add(new ListItem("people", "com.android.contacts", "com.android.contacts.activities.PeopleActivity"));
        listItems.add(new ListItem("messages", "com.android.mms", "com.android.mms.ui.ConversationList"));
        listItems.add(new ListItem("e-mail", "com.google.android.gm", "com.google.android.gm.GmailActivity"));
        listItems.add(new ListItem("weather", "com.levelup.beautifulwidgets", "com.levelup.beautifulwidgets.full.activities.ForecastActivityFull"));
        listItems.add(new ListItem("applications", "com.raidzero.androidium", "com.raidzero.androidium.AppDrawer"));
        listItems.add(new ListItem("calculator", "com.android.calculator2", "com.android.calculator2.Calculator"));
        listItems.add(new ListItem("camera", "com.android.gallery3d", "com.android.camera.CameraActivity"));
        listItems.add(new ListItem("pictures", "com.android.gallery3d", "com.android.gallery3d.app.Gallery"));
        listItems.add(new ListItem("music", "github.daneren2005.dsub", "github.daneren2005.dsub.activity.MainActivity"));
        listItems.add(new ListItem("finances", "com.chase.sig.android", "com.chase.sig.android.activity.HomeActivity"));
        listItems.add(new ListItem("internet", "com.android.browser", "com.android.browser.BrowserActivity"));
        listItems.add(new ListItem("calendar", "com.android.calendar", "com.android.calendar.AllInOneActivity"));
        listItems.add(new ListItem("play", "com.android.vending", "com.google.android.finsky.activities.MainActivity"));

        updateView();
    }

    private void updateView() {
        logWrapper("updateView() called.");

        int i = 0; // this will be how we ID the list items

        updatedMissedItems();
        loadPrefs();

        logWrapper("Found " + listItems.size() + " home screen items");

        // iterate over ArrayList, item is final so it can be accessed in click listener inner class
        for (final ListItem item : listItems) {
            final String itemName = item.getItemName();
            final Boolean itemVisible = item.isVisible();

            logWrapper("Working on item " + itemName);
            logWrapper("item " + itemName + " visible: " + itemVisible);

            // skip invisible or already shown things
            if (!itemVisible || itemsDisplayed.contains(itemName)) {
                continue;
            }

            // make a new textview from our list_item template
            TextView tv = (TextView) View.inflate(this, R.layout.list_item, null);

            // set the ID to what we put in the ListItem
            tv.setId(i);

            if (itemName.equals("phone") && missedCalls > 0 && missedNumbers_enabled) {
                // add missed calls in superscript
                tv.setText(Html.fromHtml("phone <sup><small>" + missedCalls + "</small></sup>"));
            }
            else if (itemName.equals("messages") && unreadSMS > 0 && missedNumbers_enabled) {
                // add unread SMS in superscript
                tv.setText(Html.fromHtml("messages <sup><small>" + unreadSMS + "</small></sup>"));
            }
            else {
                logWrapper("Displaying item " + item.getItemName());
                tv.setText(itemName);
            }

            itemsDisplayed.add(itemName);
            i++; // increment good old iterator

            // add touch listener
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateTouch(v);
                    int id = v.getId();
                    logWrapper("ID clicked: " + id);
                    final Intent i = item.getLaunchIntent();
                    logWrapper("Recieved launch intent for " + itemName);
                    try {
                        if (!itemName.equals("applications")) {
                            i.setAction(Intent.ACTION_MAIN);
                            i.addCategory(Intent.CATEGORY_LAUNCHER);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        } else {
                             // do not run the app drawer in the main thread
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
            };

            tv.setOnClickListener(clickListener);
            layout.addView(tv);
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
        v.startAnimation(touchEffectOut);
        Animation touchEffectIn = AnimationUtils.loadAnimation(this, R.anim.item_touch_fadein);
        v.startAnimation(touchEffectIn);
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
        return; // do nothing, we are already home, cant go any further
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
