package com.raidzero.androidium;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by raidzero on 9/23/13 3:24 PM
 */
public class NameShortcut extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("");
        alert.setMessage(getResources().getString(R.string.name_shortcut));

        final String launchShortcut = getIntent().getStringExtra("shortcut"); // retrieve the package name we are looking for activities in

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                Intent data = new Intent();
                data.setData(Uri.parse(value + " " + launchShortcut));
                setResult(RESULT_OK, data);
                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = "";
                Intent data = new Intent();
                data.setData(Uri.parse(value));
                setResult(RESULT_OK, data);
                finish();
            }
        });

        alert.show();
    }
}
