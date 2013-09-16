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
import android.util.AttributeSet;
import android.view.*;
import android.widget.ScrollView;
import android.widget.TextView;

public class HomeView extends ScrollView {
    // these are where the highlight section is
    private int centerTop;
    private int centerBottom;
    private int centerSize;
    private int centerCenter;

    private int viewTop; // top of list item view
    private int viewBottom; // bottom of list item
    private int viewSize; // size of the list item

    // these are corrdinates of scrolling
    private int oldX;
    private int newX;
    private int oldY;
    private int newY;

    // these are coordinates where the screen has been touched
    private int touchX;
    private int touchY;

    private TextView view;

    public HomeView(Context context) {
        super(context);
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.oldX = oldl;
        this.newX = l;
        this.oldY = oldt;
        this.newY = t;

        //Log.i("Scrolling", "Y from [" + oldt + "] to [" + t + "]");
        TextView center = HomeActivity.center;

        int relativeCenterTop = center.getTop();
        int relativeCenterBottom = center.getBottom();
        centerSize = relativeCenterBottom - relativeCenterTop;

        // get absolute position
        int[] coords = new int[2];
        center.getLocationOnScreen(coords);

        centerTop = coords[1];
        centerBottom = centerTop + centerSize;
        centerCenter = (centerSize/2) + centerTop;

        super.onScrollChanged(l, t, oldl, oldt);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.touchX = (int) ev.getX();
        this.touchY = (int) ev.getY();

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

        int[] vCoords = new int[2];
        v.getLocationOnScreen(vCoords);

        // determine the bottom of the view
        viewTop = vCoords[1];
        viewBottom = viewTop + viewSize;

        if (centerTop == 0) { // recalculate the center position
            int[] cCoords = new int[2];

            TextView center = HomeActivity.center;
            center.getLocationOnScreen(cCoords);

            centerTop = cCoords[1];
            centerBottom = centerTop + centerSize;
            centerCenter = (centerSize/2) + centerTop;
        }

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
