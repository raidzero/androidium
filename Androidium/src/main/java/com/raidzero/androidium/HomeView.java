/*******************************************************************************
 *
 * * Copyright 9/15/2013, raidzero
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
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class HomeView extends ScrollView {

    TextView center; // this is the center highlight

    // these are where the highlight section is
    private int centerTop;
    private int centerBottom;
    private int centerCenter;

    // these are where the target child view is
    private int viewTop;
    private int viewBottom;
    private int viewSize;

    // new calls and sms counts
    private int missedCalls, unreadSMS;

    private boolean viewCentered; // is target child view in highlight

    // these are coordinates where the screen has been touched
    private int touchX;
    private int touchY;

    // necessary constructors
    public HomeView(Context context) {
        super(context);
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onDraw(Canvas canvas) {
        center = HomeActivity.center;
        int centerSize = center.getBottom() - center.getTop();

        // get absolute position
        int[] coords = new int[2];
        center.getLocationOnScreen(coords);

        centerTop = coords[1];
        centerBottom = centerTop + centerSize;
        centerCenter = (centerSize/2) + centerTop;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //Log.i("Scrolling", "Y from [" + oldt + "] to [" + t + "]");

        ScrollView sv = this;

        LinearLayout svChild_layout = (LinearLayout) sv.getChildAt(0);

        int llChild_count = svChild_layout.getChildCount();

        // loop through children
        for (int i =0; i< llChild_count; i++) {
            TextView child = (TextView) svChild_layout.getChildAt(i);
            String childName = child.getText().toString();
            if (childName.isEmpty()) {
                continue;
            }

            viewSize = child.getBottom() - child.getTop();

            int[] vCoords = new int[2];
            child.getLocationOnScreen(vCoords);

            // determine the bottom of the view
            viewTop = vCoords[1];
            viewBottom = viewTop + viewSize;

            if (viewTop >= centerTop && viewBottom <= centerBottom) {
                viewCentered = true;
                child.setTypeface(null, Typeface.BOLD);
            } else {
                viewCentered = false;
                child.setTypeface(null, Typeface.NORMAL);
            }
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.touchX = (int) ev.getX();
        this.touchY = (int) ev.getY();

        /*
        if (touchY >= centerTop && touchY <= centerBottom) {
            Log.i("TOUCH", "touched in center");
        }
        */

        //Log.i("HOMEVIEW", "Screen touched at [" + touchX + ", " + touchY + "]");
        return super.onInterceptTouchEvent(ev);
    }


    public Boolean isVewHighlighted(TextView v) {
        if (v == null) {
            return false;
        }

        ScrollView sv = this;

        // get view's position on screen
        int relativeViewTop = v.getTop();
        int relativeViewBottom = v.getBottom();
        viewSize = relativeViewBottom - relativeViewTop;

        /*
        Log.i("RELATIVEVIEW CENTER", "Top: " + relativeViewTop +
              " Bottom: " + relativeViewBottom +
              " Center: " + relativeViewCenter);
        */

        int[] vCoords = new int[2];
        v.getLocationOnScreen(vCoords);

        // determine the bottom of the view
        viewTop = vCoords[1];
        viewBottom = viewTop + viewSize;

        Log.i("CENTER", "Top: " + centerTop +
                " Bottom: " + centerBottom +
                " Center: " + centerCenter);

        if (viewTop >= centerTop && viewBottom <= centerBottom) {
            //Log.i("Scrolling", v.getText().toString() + " has entered the center!");
            return true;
        }
        else {
            // scroll so that the touched view is in the highlight! fuck yeah
            int scrollDest = relativeViewTop - centerTop;
            sv.smoothScrollTo(0, scrollDest);
        }

        return false;
    }
}
