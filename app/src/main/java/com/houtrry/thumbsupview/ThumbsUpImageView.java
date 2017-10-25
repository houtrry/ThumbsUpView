package com.houtrry.thumbsupview;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
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
    private float mShiningTranslationY = 20;
    private long mAnimatorDuration = 300;

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
     * @param isUp         true: 点赞; false: 取消点赞.
     * @param needAnimator true: 需要动画; false: 不需要动画
     */
    public void thumbs(boolean isUp, boolean needAnimator) {
        if (needAnimator) {
            changeImageWithAnimator(isUp);
        } else {
            changeImageNoAnimator(isUp);
        }
    }

    private void changeImageWithAnimator(final boolean isUp) {

        if (isUp) {
            mIvShiningView.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(mAnimatorDuration)
                    .setStartDelay((long) (mAnimatorDuration*0.3f))
                    .setInterpolator(new LinearOutSlowInInterpolator());
        } else {
            mIvShiningView.animate().translationY(mShiningTranslationY).alpha(0f)
                    .setStartDelay((long) (mAnimatorDuration*0.3f))
                    .scaleX(0f).scaleY(0f).setDuration(mAnimatorDuration).setInterpolator(new LinearOutSlowInInterpolator());
        }



        // 在 0% 处开始
        Keyframe scaleXKeyframe1 = Keyframe.ofFloat(0, 1.0f);
        Keyframe scaleXKeyframe2 = Keyframe.ofFloat(0.2f, 0.6f);
        // 时间经过 50% 的时候，动画完成度 100%
        Keyframe scaleXKeyframe3 = Keyframe.ofFloat(0.5f, 1.0f);
        // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
        Keyframe scaleXKeyframe4 = Keyframe.ofFloat(0.8f, 1.4f);
        Keyframe scaleXKeyframe5 = Keyframe.ofFloat(1, 1.0f);
        PropertyValuesHolder likeObjectAnimatorScaleX = PropertyValuesHolder.ofKeyframe("scaleX", scaleXKeyframe1, scaleXKeyframe2, scaleXKeyframe3, scaleXKeyframe4, scaleXKeyframe5);


        // 在 0% 处开始
        Keyframe scaleYKeyframe1 = Keyframe.ofFloat(0, 1.0f);
        Keyframe scaleYKeyframe2 = Keyframe.ofFloat(0.2f, 0.6f);
        // 时间经过 50% 的时候，动画完成度 100%
        Keyframe scaleYKeyframe3 = Keyframe.ofFloat(0.5f, 1.0f);
        // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
        Keyframe scaleYKeyframe4 = Keyframe.ofFloat(0.8f, 1.4f);
        Keyframe scaleYKeyframe5 = Keyframe.ofFloat(1, 1.0f);
        PropertyValuesHolder likeObjectAnimatorScaleY = PropertyValuesHolder.ofKeyframe("scaleY", scaleYKeyframe1, scaleYKeyframe2, scaleYKeyframe3, scaleYKeyframe4, scaleYKeyframe5);

//        PropertyValuesHolder likeObjectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.6f, 1.2f, 1.0f);

        ObjectAnimator likeObjectAnimatorAlpha = ObjectAnimator.ofFloat(mIvLikeView, "alpha", 1.0f, 0.0f, 1.0f);
        likeObjectAnimatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if (value == 0f) {
                    Log.d(TAG, "onAnimationUpdate: 天啊撸, 赶紧换图片哇~");
                }
                float animatedFraction = valueAnimator.getAnimatedFraction();
                if (animatedFraction == 0.5f) {
                    Log.d(TAG, "onAnimationUpdate: 天啊撸, 你还在等啥~");
                    mIvLikeView.setSelected(isUp);
                }
            }
        });
//        likeObjectAnimatorAlpha.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                super.onAnimationRepeat(animation);
//                Log.d(TAG, "onAnimationUpdate: 天啊撸, 现在是个好机会~");
//                mIvLikeView.setSelected(isUp);
//            }
//        });
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvLikeView, likeObjectAnimatorScaleX, likeObjectAnimatorScaleY);

        AnimatorSet animatorSetLike = new AnimatorSet ();
        animatorSetLike.play(animator).with(likeObjectAnimatorAlpha);
        animatorSetLike.start();
    }

    private void changeImageNoAnimator(boolean isUp) {
        mIvShiningView.setTranslationY(isUp ? 0 : mShiningTranslationY);
        mIvShiningView.setAlpha(isUp ? 1.0f : 0f);
        mIvLikeView.setSelected(isUp);
    }
}
