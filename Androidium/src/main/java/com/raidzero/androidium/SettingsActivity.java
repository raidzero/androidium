package com.raidzero.androidium;

import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Created by raidzero on 9/11/13.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.menu.preferences);
    }
}
