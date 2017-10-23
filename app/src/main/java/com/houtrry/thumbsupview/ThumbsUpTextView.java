package com.houtrry.thumbsupview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: houtrry
 * @time: 2017/10/23
 * @desc: ${TODO}
 */

public class ThumbsUpTextView extends View {

    private static final String TAG = ThumbsUpTextView.class.getSimpleName();

    private float mTextSize = 36;
    private int mTextColor = Color.BLACK;
    private Paint mTextPaint;

    private static final int TYPE_LEFT = 0x0001;
    private static final int TYPE_RIGHT = 0x0002;
    private int mType = TYPE_LEFT;
    private int mCurrentValue = 109;
    private String mTextStr = "";
    private String mNewTextStr = "";
    private ObjectAnimator mObjectAnimator;
    @Keep
    private float progress = 0.0f;
    private int mWidth;
    private int mHeight;

    public ThumbsUpTextView(Context context) {
        this(context, null);
    }

    public ThumbsUpTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbsUpTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Keep
    public void setProgress(float progress) {
        this.progress = progress;
        ViewCompat.postInvalidateOnAnimation(this);
    }


    /**
     * @param isUp TRUE:点赞;FALSE:取消点赞
     */
    public void thumbsUp(boolean isUp) {
        mCurrentValue = mCurrentValue + (isUp ? 1 : (-1));
        mNewTextStr = String.valueOf(mCurrentValue);
        startAnimator(isUp);
    }

    private void init(Context context, AttributeSet attrs) {
        initPaint(context);
        mTextStr = String.valueOf(mCurrentValue);
    }

    private void initPaint(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (mTextPaint.measureText(mTextStr) + getPaddingLeft() + getPaddingRight() + 0.5f + (mTextStr.length() - 1) * mTextScaleX);
        }
        return result;
    }

    private Rect mTextRect = new Rect();

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            mTextPaint.getTextBounds(mTextStr, 0, mTextStr.length(), mTextRect);
            result = (int) (mTextRect.height() + getPaddingTop() + getPaddingBottom() + 0.5f);
        }
        return result;
    }


    private float mCurrentLeft = 0;
    private float mTextScaleX = 10;

    private float mTextY = 0;
    private float mNewTextY = 0;
    private void drawText(Canvas canvas) {
        int textCharArrayLength = mTextStr.length();
        int newTextCharArrayLength = mNewTextStr.length();
        mCurrentLeft = getPaddingLeft();
        int maxLength = Math.max(textCharArrayLength, newTextCharArrayLength);
        for (int i = 0; i < maxLength; i++) {
            String textAtI = getStringAt(mTextStr, i);
            String newTextAtI = getStringAt(mNewTextStr, i);
            if (textAtI.equalsIgnoreCase(newTextAtI)) {
                mTextPaint.getTextBounds(textAtI, 0, textAtI.length(), mTextRect);
                mTextY = mHeight * 0.5f + mTextRect.height()*0.5f;
                canvas.drawText(textAtI, mCurrentLeft, mTextY, mTextPaint);
            } else {
                mTextPaint.getTextBounds(textAtI, 0, textAtI.length(), mTextRect);
                mTextY = mHeight * 0.5f + mTextRect.height()*0.5f + mHeight * progress;
                canvas.drawText(textAtI, mCurrentLeft, mTextY, mTextPaint);

                mTextPaint.getTextBounds(newTextAtI, 0, newTextAtI.length(), mTextRect);
                mNewTextY = mHeight * (progress < 0? 1.5f : -0.5f) + mTextRect.height()*0.5f + mHeight * progress;
                canvas.drawText(newTextAtI, mCurrentLeft, mNewTextY, mTextPaint);
            }
            mCurrentLeft = mCurrentLeft + Math.max(mTextPaint.measureText(textAtI), mTextPaint.measureText(newTextAtI)) + mTextScaleX;
        }

    }

    private void startAnimator(boolean isUp) {
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
        mObjectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, isUp ? -1f : 1f);
        mObjectAnimator.setDuration(500);
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mTextStr = mNewTextStr;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mObjectAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        mObjectAnimator.start();
    }

    public static String getStringAt(String text, int index) {
        if (index >= text.length()) {
            return "";
        }
        return String.valueOf(text.charAt(index));
    }
}
