package com.bye.hey.coordinator;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by master on 22/7/17.
 */

public class CustomNestedScrollview extends NestedScrollView {
    public CustomNestedScrollview(Context context) {
        super(context);
    }

    public CustomNestedScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }


}
