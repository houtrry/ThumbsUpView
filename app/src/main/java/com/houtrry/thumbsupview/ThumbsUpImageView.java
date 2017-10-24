package com.houtrry.thumbsupview;

import android.content.Context;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
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

public class ThumbsUpImageView extends FrameLayout {

    private static final String TAG = ThumbsUpImageView.class.getSimpleName();
    private ImageView mIvLikeView;
    private ImageView mIvShiningView;

    public ThumbsUpImageView(Context context) {
        this(context, null);
    }

    public ThumbsUpImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbsUpImageView(Context context, AttributeSet attrs, int defStyle) {
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
        if (needAnimator) {
            changeImageWithAnimator(isUp);
        } else {
            changeImageNoAnimator(isUp);
        }
    }

    private void changeImageWithAnimator(boolean isUp) {
        mIvLikeView.setScaleX(0.6f);
        mIvLikeView.setScaleY(0.6f);
        mIvLikeView.animate().setInterpolator(new CosInterpolator())
                .setDuration(1000)
                .scaleX(1.0f)
                .scaleY(1.0f);

        if (isUp) {
            mIvShiningView.animate().translationY(0)
                    .scaleX(1.0f).scaleY(1.0f).setDuration(1000).setInterpolator(new LinearOutSlowInInterpolator());
        } else {
            mIvShiningView.animate().translationY(20)
                    .scaleX(0f).scaleY(0f).setDuration(1000).setInterpolator(new LinearOutSlowInInterpolator());
        }
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
//        scaleAnimation.setDuration(1000);
//        scaleAnimation.setInterpolator(new OvershootInterpolator(2));
//        mIvLikeView.startAnimation(scaleAnimation);


//        AnimationSet animationSet = new AnimationSet(true);
//        // 在 0% 处开始
//        Keyframe keyframe1 = Keyframe.ofFloat(0, 0);
//// 时间经过 50% 的时候，动画完成度 100%
//        Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100);
//// 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
//        Keyframe keyframe3 = Keyframe.ofFloat(1, 80);
//        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("scaleX", keyframe1, keyframe2, keyframe3);
//
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvLikeView, holder);
//
//        animator.start();
    }

    private void changeImageNoAnimator(boolean isUp) {
        mIvShiningView.setVisibility(isUp?VISIBLE:INVISIBLE);
        mIvLikeView.setImageResource(isUp?R.mipmap.ic_messages_like_selected:R.mipmap.ic_messages_like_unselected);
    }
}
