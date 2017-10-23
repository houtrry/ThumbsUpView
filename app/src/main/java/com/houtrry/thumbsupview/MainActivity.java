package com.houtrry.thumbsupview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.thumbsUpTextView)
    ThumbsUpTextView mThumbsUpTextView;
    @BindView(R.id.thumbsUp)
    Button mThumbsUp;
    @BindView(R.id.thumbsDown)
    Button mThumbsDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.thumbsUp, R.id.thumbsDown})
    public void click(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.thumbsUp) {
            mThumbsUpTextView.thumbsUp(true);
        } else if (viewId == R.id.thumbsDown) {
            mThumbsUpTextView.thumbsUp(false);
        }
    }
}
