/*
 * Created by Mathankumar K On 1/9/19 11:54 AM
 * Copyright (c) Aximsoft 2019.
 * All rights reserved.
 */

package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

@SuppressLint("ClickableViewAccessibility")
public class DrawCircleView extends FrameLayout {


    CircleView drawCircle;
    ConstraintLayout parentView;
    View parent;

    public DrawCircleView(Context context, View parent, int circleColor, float x, float y) {
        super(context);
        this.parent = parent;
        init(circleColor, x, y);
    }


    public DrawCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);


    }

    int circleSize = 60, lineWidthSize = 10, textSize = 15;
    int circleColor = Color.BLACK, lineColor = Color.GRAY, textColor = Color.BLACK;

    private void initView(Context context, AttributeSet attrs) {
        init(Color.GREEN, 0, 0);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LaneVisionDrawView,
                0, 0);
        try {
            circleSize = a.getDimensionPixelSize(R.styleable.LaneVisionDrawView_tri_circleSize, getResources().getDimensionPixelOffset(R.dimen.drawCircleWidth));
            lineWidthSize = a.getDimensionPixelSize(R.styleable.LaneVisionDrawView_tri_lineWidth, getResources().getDimensionPixelOffset(R.dimen.drawLineWidth));
            textSize = a.getDimensionPixelSize(R.styleable.LaneVisionDrawView_tri_angleTextSize, getResources().getDimensionPixelOffset(R.dimen.drawTextSize));
            circleColor = a.getColor(R.styleable.LaneVisionDrawView_tri_circleColor, ContextCompat.getColor(getContext(),R.color.orange));
            lineColor = a.getColor(R.styleable.LaneVisionDrawView_tri_lineColor, ContextCompat.getColor(getContext(),R.color.yellow));
            textColor = a.getColor(R.styleable.LaneVisionDrawView_tri_angleTextColor, ContextCompat.getColor(getContext(),R.color.yellow));
            drawCircle.setStrokeSize(lineWidthSize);
        } finally {
            a.recycle();
        }
    }


    @SuppressWarnings("unused")
    public static RectF calculateRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(), location[1] + view.getMeasuredHeight());
    }

    private void init(int circleColor, final float x, final float y) {
        lineWidthSize = getResources().getDimensionPixelOffset(R.dimen.drawLineWidth);
        this.circleColor = ContextCompat.getColor(getContext(), R.color.orange);
        lineColor = ContextCompat.getColor(getContext(),R.color.yellow);
        textColor = ContextCompat.getColor(getContext(),R.color.yellow);
        LayoutInflater.from(getContext()).inflate(R.layout.draw_circle_view, this);
        parentView = findViewById(R.id.parentView);
        drawCircle = findViewById(R.id.drawCircle);
        drawCircle.setStrokeSize(lineWidthSize);
        drawCircle.setCircleColor(circleColor);
        drawCircle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawCircle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                drawCircle.setXy(x, y);
            }
        });
    }

    public void movingView(MotionEvent event) {
        drawCircle.movingView(event);

    }

    public void draw() {
    }


}

