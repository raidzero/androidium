package com.raidzero.androidium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class HomeActivity extends Activity {

    static final String[] ITEMS = new String[] { "phone", "people", "messages",
            "e-mail", "weather", "applications", "calculator", "camera",
            "pictures", "music", "finances", "internet", "calendar", "play" };

    // set up list items with their launch intents
    static final ListItem[] listItems = new ListItem[] {
            new ListItem("phone", new Intent(), "com.android.dialer", "com.android.dialer.DialtactsActivity"),
            new ListItem("people", new Intent(), "com.android.contacts", "com.android.contacts.activities.PeopleActivity"),
            new ListItem("messaging", new Intent(), "com.android.mms", "com.android.mms.ui.ConversationList"),
            new ListItem("e-mail", new Intent(), "com.google.android.gm", "com.google.android.gm.GmailActivity"),
            new ListItem("weather", new Intent(), "com.levelup.beautifulwidgets", "com.levelup.beautifulwidgets.full.activities.ForecastActivityFull"),
            new ListItem("applications", new Intent(), "com.raidzero.androidium", "com.raidzero.androidium.AppDrawer"),
            new ListItem("calculator", new Intent(), "com.android.calculator2", "com.android.calculator2.Calculator"),
            new ListItem("camera", new Intent(), "com.android.gallery3d", "com.android.camera.CameraActivity"),
            new ListItem("pictures", new Intent(), "com.android.gallery3d", "com.android.gallery3d.app.Gallery"),
            new ListItem("music", new Intent(), "github.daneren2005.dsub", "github.daneren2005.dsub.activity.MainActivity"),
            new ListItem("finances", new Intent(), "com.chase.sig.android", "com.chase.sig.android.activity.HomeActivity"),
            new ListItem("internet", new Intent(), "com.android.browser", "com.android.browser.BrowserActivity"),
            new ListItem("calendar", new Intent(), "com.android.calendar", "com.android.calendar.AllInOneActivity"),
            new ListItem("play", new Intent(), "com.android.vending", "com.google.android.finsky.activities.MainActivity")
    };

    static final String tag = "androidium";

    private static final int pkg_request_code = 1;
    private static final int act_request_code = 2;

    TextView center;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_scroll);

        LinearLayout layout = (LinearLayout) findViewById(R.id.llayout); // where items go
        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.rLayout); // where the items container goes

        center = (TextView) findViewById(R.id.centerOfScreen);

        rLayout.removeView(findViewById(R.id.centerOfScreen));
        //rLayout.addView(center);

        int missedCalls = Calls.getMissedCallCount(this);
        int unreadSMS = Messages.getUnreadSmsCount(this);

        /*
        // add some padding so that the topmost item can be centered on screen
        for (int i = 0; i< 4; i++) {
            TextView tv = (TextView) View.inflate(this, R.layout.list_item, null);
            layout.addView(tv);
        }
        */

        for (int i=0; i<listItems.length; i++)
        {
            TextView tv = (TextView) View.inflate(this, R.layout.list_item, null);

            // set the ID to what we put in the ListItem
            tv.setId(i);

            if (listItems[i].getItemName().equals("phone") && missedCalls > 0) {
                // add missed calls in superscript
                tv.setText(Html.fromHtml("phone <sup><small>" + missedCalls + "</small></sup>"));
            }
            else if (listItems[i].getItemName().equals("messages") && unreadSMS > 0) {
                // add unread SMS in superscript
                tv.setText(Html.fromHtml("messages <sup><small>" + unreadSMS + "</small></sup>"));
            }
            else
            {
                tv.setText(listItems[i].getItemName());
            }

            // add touch listener
            final TextView centerBar = center;

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    int[] centerCoords = new int[2];
                    int[] vCoords = new int[2];

                    center.getLocationInWindow(centerCoords);
                    v.getLocationInWindow(vCoords);

                    int vTop = v.getTop();
                    int vBottom = v.getBottom();

                    Log.d(tag, "vTop: " + vTop);
                    Log.d(tag, "vBottom: " + vBottom);

                    Log.d(tag, "centerTop: " + centerCoords[1]);
                    Log.d(tag, "centerBottom: " + (centerCoords[1] + (center.getBottom() - center.getTop())));

                    //Log.d(tag, "Absolute centerCoords[x]: " + centerCoords[0] + " centerCoords[y]: " + centerCoords[1]);
                    Log.d(tag, "Absolute vCoords[x]: " + vCoords[0] + " vCoords[y]: " + vCoords[1]);
                    Log.d(tag, "centerRange: " + (center.getBottom() - center.getTop()));
                    */

                    animateTouch(v);
                    int id = v.getId();
                    Log.d(tag, "ID clicked: " + id);
                    Intent i = listItems[id].getLaunchIntent();
                    Log.d(tag, "Recieved launch intent for " + listItems[id].getItemName());
                    try {
                        if (!listItems[id].getItemName().equals("applications")) {
                            i.setAction(Intent.ACTION_MAIN);
                            i.addCategory(Intent.CATEGORY_LAUNCHER);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
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

        /*
        // bottom padding
        for (int i = 0; i< 4; i++) {
            TextView tv = (TextView) View.inflate(this, R.layout.list_item, null);
            layout.addView(tv);
        }
        */
;
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
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return; // do nothing, we are already home, cant go any further
    }
}
