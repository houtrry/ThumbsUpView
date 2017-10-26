package com.houtrry.thumbsupview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author houtrry
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.thumbsUpTextView)
    ThumbsUpTextView mThumbsUpTextView;
    @BindView(R.id.thumbsUp)
    Button mThumbsUp;
    @BindView(R.id.thumbsDown)
    Button mThumbsDown;
    @BindView(R.id.thumbsImageView)
    ThumbsUpImageView mThumbsImageView;
    @BindView(R.id.thumbsUpTextViewLike)
    ThumbsUpTextView mThumbsUpTextViewLike;
    @BindView(R.id.ll_like_image)
    LinearLayout mLlLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    private boolean isUp = false;
    @OnClick({R.id.thumbsUp, R.id.thumbsDown, R.id.ll_like_image})
    public void click(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.thumbsUp) {
            mThumbsUpTextView.thumbsUp(true);
        } else if (viewId == R.id.thumbsDown) {
            mThumbsUpTextView.thumbsUp(false);
        } else if (viewId == R.id.ll_like_image) {
            isUp = !isUp;
            mThumbsImageView.thumbs(isUp, true);
            mThumbsUpTextViewLike.thumbsUp(isUp);
        }
    }
}
