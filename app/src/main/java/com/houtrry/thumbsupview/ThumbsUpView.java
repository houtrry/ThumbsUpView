package com.houtrry.thumbsupview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author: houtrry
 * @time: 2017/10/23
 * @desc:
 */

public class ThumbsUpView extends FrameLayout {

    private static final String TAG = ThumbsUpView.class.getSimpleName();
    private ImageView mIvLikeView;
    private ImageView mIvShiningView;

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
        initChildViews(context);
    }

    private void initChildViews(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_thumbsup, this, true);
        mIvLikeView = view.findViewById(R.id.iv_like);
        mIvShiningView = view.findViewById(R.id.iv_shining);
    }

    /**
     *
     * @param isUp true: 点赞; false: 取消点赞.
     * @param needAnimator true: 需要动画; false: 不需要动画
     */
    public void thumbs(boolean isUp, boolean needAnimator) {

    }

    private void changeImageNoAnimator() {

    }
}
