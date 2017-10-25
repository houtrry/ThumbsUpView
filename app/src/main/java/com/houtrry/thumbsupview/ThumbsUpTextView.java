package com.houtrry.thumbsupview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author: houtrry
 * @time: 2017/10/23
 * @desc: ${TODO}
 */

public class ThumbsUpTextView extends View {

    private static final String TAG = ThumbsUpTextView.class.getSimpleName();

    /**
     * 文字的大小
     */
    private float mTextSize = 45;
    /**
     * 文字的颜色
     */
    private int mTextColor = Color.BLACK;
    /**
     * 文字间的间隔大小
     */
    private float mTextScaleX = 10;
    private Paint mTextPaint;

    /**
     * 数字的对齐方式, 左对齐
     */
    private static final int TYPE_LEFT = 0x0001;
    /**
     * 数字的对齐方式, 右对齐
     */
    private static final int TYPE_RIGHT = 0x0002;
    /**
     * 数字的对齐方式, 默认左对齐
     */
    private int mAlignType = TYPE_RIGHT;
    private int mCurrentValue = 109;
    private String mTextStr = "";
    private String mNewTextStr = "";
    /**
     * 动画时长, 默认400ms
     */
    private long mAnimatorDuration = 400;
    private ObjectAnimator mObjectAnimator;
    private TimeInterpolator mTimeInterpolator = new LinearOutSlowInInterpolator();

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
     * 点赞或取消点赞
     *
     * @param isUp TRUE:点赞;FALSE:取消点赞
     */
    public void thumbsUp(boolean isUp) {
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            Log.d(TAG, "thumbsUp: now, the animator is running, please wait a moment");
            return;
        }
        mCurrentValue = mCurrentValue + (isUp ? 1 : (-1));
        mNewTextStr = String.valueOf(mCurrentValue);
        startAnimator(isUp);
    }

    /**
     * 设置文字大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 设置文字颜色
     *
     * @param textColor 文字颜色
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 设置文字对齐方式
     *
     * @param alignType 文字对齐方式
     */
    public void setAlignType(int alignType) {
        mAlignType = alignType;
        checkAlignType();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 设置当前的值
     *
     * @param currentValue
     */
    public void setCurrentValue(int currentValue) {
        if (currentValue != mCurrentValue) {
            boolean isUp = currentValue > mCurrentValue;
            mCurrentValue = currentValue;
            mNewTextStr = String.valueOf(mCurrentValue);
            startAnimator(isUp);
        }
    }

    /**
     * 设置动画的时长
     *
     * @param animatorDuration 动画的时长
     */
    public void setAnimatorDuration(long animatorDuration) {
        mAnimatorDuration = animatorDuration;
        if (mObjectAnimator != null) {
            mObjectAnimator.setDuration(mAnimatorDuration);
        }
    }

    /**
     * 设置动画的加速模式
     *
     * @param timeInterpolator
     */
    public void setTimeInterpolator(TimeInterpolator timeInterpolator) {
        mTimeInterpolator = timeInterpolator;
        if (mObjectAnimator != null) {
            mObjectAnimator.setInterpolator(mTimeInterpolator);
        }
    }

    /**
     * 设置文字间的间隔
     *
     * @param textScaleX 文字间的间隔
     */
    public void setTextScaleX(float textScaleX) {
        mTextScaleX = textScaleX;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 获取当前的值
     *
     * @return 当前的值
     */
    public int getCurrentValue() {
        return mCurrentValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ----->");
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
//        mTextPaint.setColor(mTextColor);
        drawText(canvas);
//        mTextPaint.setColor(Color.RED);
//        mTextPaint.setStrokeWidth(2);
//        canvas.drawLine(0, mHeight*0.5f, mWidth, mHeight*0.5f, mTextPaint);
    }

    private int measureWidth(int widthMeasureSpec) {
        Log.d(TAG, "measureWidth: ");
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (mTextPaint.measureText(mTextStr) + getPaddingLeft() + getPaddingRight() + 0.5f + (mTextStr.length() - 1) * mTextScaleX);
        }
        Log.d(TAG, "measureWidth: result: "+result);
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

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint(context);
        mTextStr = String.valueOf(mCurrentValue);
    }

    private void initPaint(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbsUpTextView);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.ThumbsUpTextView_text_size, 45);
        mTextColor = typedArray.getColor(R.styleable.ThumbsUpTextView_text_color, Color.BLACK);
        mCurrentValue = typedArray.getInteger(R.styleable.ThumbsUpTextView_default_value, 109);
        mAlignType = typedArray.getInt(R.styleable.ThumbsUpTextView_align_type, TYPE_LEFT);
        mTextScaleX = typedArray.getDimensionPixelSize(R.styleable.ThumbsUpTextView_text_scale_x, 0);
        mAnimatorDuration = typedArray.getInteger(R.styleable.ThumbsUpTextView_animator_duration, 400);
        typedArray.recycle();
        mTextStr = String.valueOf(mCurrentValue);
        checkAlignType();
    }

    private float mTextY = 0;
    private float mNewTextY = 0;

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        String textStr;
        String newTextStr;
        if (mAlignType == TYPE_LEFT) {
            textStr = mTextStr;
            newTextStr = mNewTextStr;
        } else {
            //如果是右对齐, 先反转文字
            textStr = reverseString(mTextStr);
            newTextStr = reverseString(mNewTextStr);
        }

        int textCharArrayLength = textStr.length();
        int newTextCharArrayLength = newTextStr.length();
        int maxLength = Math.max(textCharArrayLength, newTextCharArrayLength);
        canvas.save();
        if (mAlignType == TYPE_LEFT) {
            canvas.translate(getPaddingLeft(), 0);
        } else {
            canvas.translate(mWidth, 0);
            canvas.translate(-getPaddingRight(), 0);
        }
        String textAtI;
        String newTextAtI;
        float textWidth;
        for (int i = 0; i < maxLength; i++) {
            textAtI = getStringAt(textStr, i);
            newTextAtI = getStringAt(newTextStr, i);
            textWidth = Math.max(mTextPaint.measureText(textAtI), mTextPaint.measureText(newTextAtI)) + mTextScaleX;
            if (mAlignType == TYPE_RIGHT) {
                canvas.translate(textWidth * (-1), 0);
            }

            if (textAtI.equalsIgnoreCase(newTextAtI)) {
                //如果两个数字的第i个位置上数字相同, 则, 这个位置的Y值不需要变化, 新的数字也不需要画出
                mTextPaint.getTextBounds(textAtI, 0, textAtI.length(), mTextRect);
                mTextY = mHeight * 0.5f + mTextRect.height() * 0.5f;
                canvas.drawText(textAtI, 0, mTextY, mTextPaint);
                mTextPaint.setAlpha(255);
            } else {
                mTextPaint.getTextBounds(textAtI, 0, textAtI.length(), mTextRect);
                mTextY = mHeight * 0.5f + mTextRect.height() * 0.5f + mHeight * progress;
                mTextPaint.setAlpha((int) ((1f- Math.abs(progress))*255));
                canvas.drawText(textAtI, 0, mTextY, mTextPaint);

                mTextPaint.getTextBounds(newTextAtI, 0, newTextAtI.length(), mTextRect);
                mNewTextY = mHeight * (progress < 0 ? 1.5f : -0.5f) + mTextRect.height() * 0.5f + mHeight * progress;
                mTextPaint.setAlpha((int) (Math.abs(progress)*255));
                canvas.drawText(newTextAtI, 0, mNewTextY, mTextPaint);
            }
            if (mAlignType == TYPE_LEFT) {
                canvas.translate(textWidth, 0);
            }
        }
        canvas.restore();
    }

    /**
     * 开启动画
     * @param isUp true:点赞动画;false:取消点赞的动画.
     */
    private void startAnimator(boolean isUp) {
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
        mObjectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, isUp ? -1f : 1f);
        mObjectAnimator.setDuration(mAnimatorDuration);
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mTextStr = mNewTextStr;
                progress = 0;
                mTextPaint.setAlpha((int) ((1f- Math.abs(progress))*255));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mObjectAnimator.setInterpolator(mTimeInterpolator);
        mObjectAnimator.start();
    }

    /**
     * 检查对齐类型是否是TYPE_LEFT或者TYPE_RIGHT, 如果不是, 抛出异常
     */
    private void checkAlignType() {
        if (mAlignType != TYPE_LEFT && mAlignType != TYPE_RIGHT) {
            throw new IllegalArgumentException("the type is only support TYPE_LEFT and TYPE_RIGHT");
        }
    }

    /**
     * 获取字符串中指定位置的单个字符串
     * @param text 源字符串
     * @param index 位置
     * @return 指定位置的单个字符串
     */
    public static String getStringAt(String text, int index) {
        if (index >= text.length()) {
            return "";
        }
        return String.valueOf(text.charAt(index));
    }

    /**
     * 反转字符串
     * @param text 源字符串
     * @return 反转后的字符串
     */
    public static String reverseString(String text) {
        StringBuilder sb = new StringBuilder(text);
        return sb.reverse().toString();
    }
}
