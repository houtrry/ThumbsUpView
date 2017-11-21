package com.houtrry.thumbsupview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
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
    /**
     * 动画时shining在y轴上移动的距离
     */
    private float mShiningTranslationY = 20;
    /**
     * 动画时长
     */
    private long mAnimatorDuration = 300;
    /**
     * 当前的状态
     */
    private boolean mThumbsUp = false;
    /**
     * 图片放大缩小的偏差
     * 由1.0f减小mZoomOffset()比如0.2这么多, 变成最小的时候1-mZoomOffset(0.8), 最大的时候1+mZoomOffset(1.2)
     */
    private float mZoomOffset = 0.2f;

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
        initAttrs(context, attrs);
        initChildViews(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbsUpImageView);
        mShiningTranslationY = typedArray.getDimensionPixelSize(R.styleable.ThumbsUpImageView_shining_translation_y, 45);
        mAnimatorDuration = typedArray.getInt(R.styleable.ThumbsUpImageView_animation_duration, 1000);
        mThumbsUp = typedArray.getBoolean(R.styleable.ThumbsUpImageView_default_status, false);
        mZoomOffset = typedArray.getFloat(R.styleable.ThumbsUpImageView_zoom_offset, 0.2f);
        typedArray.recycle();
    }

    private void initChildViews(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_thumbsup, this, true);
        mIvLikeView = view.findViewById(R.id.iv_like);
        mIvShiningView = view.findViewById(R.id.iv_shining);
        changeImageNoAnimator();
    }

    /**
     * @param isUp         true: 点赞; false: 取消点赞.
     * @param needAnimator true: 需要动画; false: 不需要动画
     */
    public void thumbs(boolean isUp, boolean needAnimator) {
        mThumbsUp = isUp;
        if (needAnimator) {
            changeImageWithAnimator();
        } else {
            changeImageNoAnimator();
        }
    }

    /**
     * 返回当前的状态
     *
     * @return
     */
    public boolean isThumbsUp() {
        return mThumbsUp;
    }

    private void changeImageWithAnimator() {
        if (mThumbsUp) {
            mIvShiningView.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(mAnimatorDuration)
                    .setStartDelay((long) (mAnimatorDuration * 0.3f))
                    .setInterpolator(new LinearOutSlowInInterpolator());
        } else {
            mIvShiningView.animate().translationY(mShiningTranslationY).alpha(0f)
                    .setStartDelay((long) (mAnimatorDuration * 0.3f))
                    .scaleX(0f).scaleY(0f).setDuration(mAnimatorDuration).setInterpolator(new LinearOutSlowInInterpolator());
        }

        // 在 0% 处开始
        Keyframe scaleXKeyframe1 = Keyframe.ofFloat(0, 1.0f);
        Keyframe scaleXKeyframe2 = Keyframe.ofFloat(0.2f, 1 - mZoomOffset);
        // 时间经过 50% 的时候，动画完成度 100%
        Keyframe scaleXKeyframe3 = Keyframe.ofFloat(0.5f, 1.0f);
        // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
        Keyframe scaleXKeyframe4 = Keyframe.ofFloat(0.8f, 1 + mZoomOffset);
        Keyframe scaleXKeyframe5 = Keyframe.ofFloat(1, 1.0f);
        PropertyValuesHolder likeObjectAnimatorScaleX = PropertyValuesHolder.ofKeyframe("scaleX", scaleXKeyframe1, scaleXKeyframe2, scaleXKeyframe3, scaleXKeyframe4, scaleXKeyframe5);


        // 在 0% 处开始
        Keyframe scaleYKeyframe1 = Keyframe.ofFloat(0, 1.0f);
        Keyframe scaleYKeyframe2 = Keyframe.ofFloat(0.2f, 1 - mZoomOffset);
        // 时间经过 50% 的时候，动画完成度 100%
        Keyframe scaleYKeyframe3 = Keyframe.ofFloat(0.5f, 1.0f);
        // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
        Keyframe scaleYKeyframe4 = Keyframe.ofFloat(0.8f, 1 + mZoomOffset);
        Keyframe scaleYKeyframe5 = Keyframe.ofFloat(1, 1.0f);
        PropertyValuesHolder likeObjectAnimatorScaleY = PropertyValuesHolder.ofKeyframe("scaleY", scaleYKeyframe1, scaleYKeyframe2, scaleYKeyframe3, scaleYKeyframe4, scaleYKeyframe5);

        ObjectAnimator likeObjectAnimatorAlpha = ObjectAnimator.ofFloat(mIvLikeView, "alpha", 1.0f, 0.5f);
        likeObjectAnimatorAlpha.setRepeatCount(1);
        likeObjectAnimatorAlpha.setRepeatMode(ValueAnimator.REVERSE);
        likeObjectAnimatorAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                Log.d(TAG, "onAnimationUpdate onAnimationRepeat: ");
                mIvLikeView.setSelected(mThumbsUp);
            }
        });
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvLikeView, likeObjectAnimatorScaleX, likeObjectAnimatorScaleY);

        AnimatorSet animatorSetLike = new AnimatorSet();
        animatorSetLike.play(animator).with(likeObjectAnimatorAlpha);
        Log.d(TAG, "changeImageWithAnimator: mAnimatorDuration: " + mAnimatorDuration);
        animatorSetLike.setDuration(mAnimatorDuration);
        animatorSetLike.start();
    }

    private void changeImageNoAnimator() {
        mIvShiningView.setTranslationY(mThumbsUp ? 0 : mShiningTranslationY);
        mIvShiningView.setAlpha(mThumbsUp ? 1.0f : 0f);
        mIvLikeView.setSelected(mThumbsUp);
    }
}
