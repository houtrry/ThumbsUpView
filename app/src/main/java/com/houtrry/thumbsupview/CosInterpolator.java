package com.houtrry.thumbsupview;


import android.view.animation.Interpolator;

/**
 * @author: houtrry
 * @time: 2017/10/24 19:05
 * @version: $Rev$
 * @desc: ${TODO}
 */

public class CosInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        return (float) (1.0 + 0.4f * Math.sin(input*2*Math.PI + Math.PI));
    }
}
