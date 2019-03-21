/*
 * Created by Mathankumar K On 1/9/19 11:54 AM
 * Copyright (c) Aximsoft 2019.
 * All rights reserved.
 */

package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressWarnings("unused")
@SuppressLint("ClickableViewAccessibility")
public class DrawLineView extends FrameLayout implements OnDragTouchListener.OnDragActionListener {

    ImageView positionOne;
    ImageView positionThree;
    LineView drawOne;
    View drawTriAngle;
    ConstraintLayout parentView;
    View parent;
    private float startingX = 0f, startingY = 0f;

    public DrawLineView(Context context, View parent, int lineColor, float x, float y) {
        super(context);
        this.parent = parent;
        this.startingX = x;
        this.startingY = y;
        init(lineColor);
    }


    public DrawLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    int circleSize = 60, lineWidthSize = 10, textSize = 15;
    int circleColor = Color.BLACK, lineColor = Color.GRAY, textColor = Color.BLACK;

    private void initView(Context context, AttributeSet attrs) {
        init(Color.YELLOW);
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

            setColorsAndSize();

          /*  if (circleDrawable == null) {
                setCircleImageTintColor(circleColor);
            }
            setCircleImageDrawable(circleDrawable);*/
        } finally {
            a.recycle();
        }
    }

    boolean viewTouched = false;

    public void viewTouched(boolean aBoolean) {
        if (aBoolean) {
            viewTouched = true;
            setCircleImageResourceID(R.drawable.circle_triangle_view);
        } else {
            viewTouched = false;
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!viewTouched)
                        setCircleImageResourceID(R.drawable.circle_transperant_view);

                }
            }, 2000);
        }

    }

    public void setCircleImageResourceID(int resourceID) {
        positionOne.setImageResource(resourceID);
        positionThree.setImageResource(resourceID);
        requestLayout();
    }

    public void setCircleImageTintColor() {
        //   ImageViewCompat.setImageTintList(positionOne, ColorStateList.valueOf(circleColor));
        //    ImageViewCompat.setImageTintList(positionTwo, ColorStateList.valueOf(circleColor));
        //    ImageViewCompat.setImageTintList(positionThree, ColorStateList.valueOf(circleColor));
        requestLayout();
    }


    public void setLineColorResource(int resource) {
        drawOne.setPaintColorResource(resource);
        requestLayout();
    }


    public void setLineColor(int circleColor) {
        drawOne.setPaintColor(circleColor);
        //   drawTriAngle.setPaintColor(circleColor);
        requestLayout();
    }


    public void setCircleImageTintResource(int colorResource) {
        ImageViewCompat.setImageTintList(positionOne, ColorStateList.valueOf(ContextCompat.getColor(getContext(), colorResource)));
        ImageViewCompat.setImageTintList(positionThree, ColorStateList.valueOf(ContextCompat.getColor(getContext(), colorResource)));
        requestLayout();
    }


    public void setCircleImageBitmap(Bitmap bitmap) {
        positionOne.setImageBitmap(bitmap);
        positionThree.setImageBitmap(bitmap);
        requestLayout();

    }

    public void setCircleImageDrawable(Drawable imageDrawable) {
        if (null != imageDrawable) {
            positionOne.setImageDrawable(imageDrawable);
            positionThree.setImageDrawable(imageDrawable);
            requestLayout();
        }
    }


    public void setLineSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = lineWidthSize;
        drawOne.setLineSize(pixelSize);
        requestLayout();
    }


    public void setCircleSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = circleSize;
        positionOne.getLayoutParams().width = pixelSize;
        positionThree.getLayoutParams().width = pixelSize;

        positionOne.getLayoutParams().height = pixelSize;
        positionThree.getLayoutParams().height = pixelSize;

        requestLayout();
    }

    OnDragTouchListener onDragTouchListenerOne;
    OnDragTouchListener onDragTouchListenerTwo;
    OnDragTouchListener onDragTouchListenerThree;
    OnDragTouchListener onDragTouchListenerAngle;
    OnDragTouchListener onDragTouchListenerParent;

    public static RectF calculateRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(), location[1] + view.getMeasuredHeight());
    }

    private void init(int lineColor) {
        circleSize = getResources().getDimensionPixelOffset(R.dimen.drawCircleWidth);
        lineWidthSize = getResources().getDimensionPixelOffset(R.dimen.drawLineWidth);
        textSize = getResources().getDimensionPixelOffset(R.dimen.drawTextSize);
        circleColor = ContextCompat.getColor(getContext(),R.color.orange);
        this.lineColor = lineColor;
        textColor = ContextCompat.getColor(getContext(),R.color.yellow);

        LayoutInflater.from(getContext()).inflate(R.layout.draw_line_view, this);
        positionOne = findViewById(R.id.positionOne);
        positionThree = findViewById(R.id.positionThree);
        parentView = findViewById(R.id.parentView);
        drawOne = findViewById(R.id.drawOne);
        drawTriAngle = findViewById(R.id.drawTriAngle);

        setColorsAndSize();


        onDragTouchListenerOne = new OnDragTouchListener(positionOne, parent, this);
        onDragTouchListenerThree = new OnDragTouchListener(positionThree, this.parent, this);
        onDragTouchListenerParent = new OnDragTouchListener(drawTriAngle, this.parent, this);

        positionOne.setOnTouchListener(onDragTouchListenerOne);
        positionThree.setOnTouchListener(onDragTouchListenerThree);
        // showAngle.setOnTouchListener(onDragTouchListenerAngle);

        drawTriAngle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                onDragTouchListenerOne.onTouch(positionOne, event);
                onDragTouchListenerThree.onTouch(positionThree, event);
                onDragTouchListenerParent.onTouch(drawTriAngle, event);
                draw();
                requestLayout();
                invalidate();
                return true;
            }
        });

        positionOne.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                positionOne.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setViewSize(startingX, startingY);
                draw();
                refresh();
                setColorsAndSize();
            }
        });

    }

    int addValue = 60;
    int addValueY = 20;

    public void setViewSize(float x, float y) {
        positionOne.setX(x - addValue);
        positionOne.setY(y - addValueY);
        positionThree.setX(x + addValue);
        positionThree.setY(y - addValueY);
    }

    public void movingView(MotionEvent event) {
        positionThree.setX(event.getX());
        positionThree.setY(event.getY());
        onDragging(positionThree);
        draw();
        refresh();
    }



    private void setColorsAndSize() {
        setCircleSize(circleSize);
        setLineSize(lineWidthSize);
        setLineColor(lineColor);
        setCircleImageTintColor();
    }


    private void draw() {

        drawOne.drawLine(positionOne.getX() + positionOne.getWidth() / 2,
                positionOne.getY() + positionOne.getHeight() / 2,
                positionThree.getX() + positionThree.getWidth() / 2,
                positionThree.getY() + positionThree.getHeight() / 2);
        rect();
    }


    void rect() {
        float minX = Math.abs(positionOne.getX()) > Math.abs(positionThree.getX()) ?
                Math.abs(positionThree.getX()) : Math.abs(positionOne.getX());
        float maxX = Math.abs(positionOne.getX()) > Math.abs(positionThree.getX()) ?
                Math.abs(positionOne.getX()) : Math.abs(positionThree.getX());

        float minY = Math.abs(positionOne.getY()) > Math.abs(positionThree.getY()) ?
                Math.abs(positionThree.getY()) : Math.abs(positionOne.getY());
        float maxY = Math.abs(positionOne.getY()) > Math.abs(positionThree.getY()) ?
                Math.abs(positionOne.getY()) : Math.abs(positionThree.getY());


        float width = (maxX + positionOne.getWidth()) - minX;
        float height = (maxY + positionOne.getHeight()) - minY;

        drawTriAngle.getLayoutParams().width = (int) (width);
        drawTriAngle.getLayoutParams().height = (int) (height);

        drawTriAngle.setX(minX);
        drawTriAngle.setY(minY);

        drawTriAngle.requestLayout();
        requestLayout();
        Log.d("LineView", "LineView MinX " + minX);
        Log.d("LineView", "LineView MaxX " + maxX);
        Log.d("LineView", "LineView MinY " + minY);
        Log.d("LineView", "LineView MaxY " + maxY);
        Log.d("LineView", "LineView width " + width);
        Log.d("LineView", "LineView height " + height);

        Log.d("LineView", "LineView rectX " + drawTriAngle.getX());
        Log.d("LineView", "LineView rectY " + drawTriAngle.getY());
        System.out.println("LineView ");

    }

    @Override
    public void onDragStart(View view) {
        viewTouched(true);
        draw();
    }

    @Override
    public void onDragging(View view) {
        draw();
        onDragTouchListenerParent.updateBounds();
    }

    @Override
    public void onDraggingTouchEvent(View view, MotionEvent event) {
        //  onDragTouchListenerParent.onTouch(drawTriAngle, event);
        //  draw();
    }

    @Override
    public void onDragEnd(View view) {
        draw();
        requestLayout();
        invalidate();

        refresh();
        viewTouched(false);
    }

    void refresh() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                draw();
                requestLayout();
                invalidate();
            }
        }, 100);
    }


}

