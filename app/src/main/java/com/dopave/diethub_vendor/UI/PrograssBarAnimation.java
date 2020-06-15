package com.dopave.diethub_vendor.UI;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PrograssBarAnimation extends Animation {
    Context context;
    ProgressBar bar;
    TextView textView;
    float from,to;

    public PrograssBarAnimation(Context context, ProgressBar bar, TextView textView, float from, float to) {
        this.context = context;
        this.bar = bar;
        this.textView = textView;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        bar.setProgress((int)value);
    }
}
