package com.houtrry.thumbsupview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: houtrry
 * @time: 2017/10/23
 * @desc:
 */

public class ThumbsUpView extends View {

    private static final String TAG = ThumbsUpView.class.getSimpleName();

    public ThumbsUpView(Context context) {
        this(context, null);
    }

    public ThumbsUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbsUpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initPaint(context);
    }

    private void initPaint(Context context) {

    }
}
