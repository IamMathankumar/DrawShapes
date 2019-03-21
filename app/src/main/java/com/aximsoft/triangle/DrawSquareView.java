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
public class DrawSquareView extends FrameLayout implements OnDragTouchListener.OnDragActionListener {


    ImageView positionOne, positionTwo, positionThree, positionFour;
    LineView drawOne, drawTwo, drawThree, drawFour;
    View drawTriAngle;
    ConstraintLayout parentView;
    View parent;
    private float startingX = 0f, startingY = 0f;

    public DrawSquareView(Context context, View parent, int squareColor, float x, float y) {
        super(context);
        this.parent = parent;
        startingX = x;
        startingY = y;
        init(squareColor);
    }


    public DrawSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);


    }

    int circleSize = 60, lineWidthSize = 10, textSize = 15;
    int circleColor = Color.BLACK, lineColor = Color.GRAY, textColor = Color.BLACK;

    private void initView(Context context, AttributeSet attrs) {
        init(Color.GREEN);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LaneVisionDrawView,
                0, 0);
        try {

            circleSize = a.getDimensionPixelSize(R.styleable.LaneVisionDrawView_tri_circleSize, getResources().getDimensionPixelOffset(R.dimen.drawCircleWidth));
            lineWidthSize = a.getDimensionPixelSize(R.styleable.LaneVisionDrawView_tri_lineWidth, getResources().getDimensionPixelOffset(R.dimen.drawLineWidth));
            textSize = a.getDimensionPixelSize(R.styleable.LaneVisionDrawView_tri_angleTextSize, getResources().getDimensionPixelOffset(R.dimen.drawTextSize));
            circleColor = a.getColor(R.styleable.LaneVisionDrawView_tri_circleColor, ContextCompat.getColor(getContext(), R.color.orange));
            lineColor = a.getColor(R.styleable.LaneVisionDrawView_tri_lineColor, ContextCompat.getColor(getContext(), R.color.yellow));
            textColor = a.getColor(R.styleable.LaneVisionDrawView_tri_angleTextColor, ContextCompat.getColor(getContext(), R.color.yellow));


            setColorsAndSize();
        } finally {
            a.recycle();
        }
    }


    private void setColorsAndSize() {
        setCircleSize(circleSize);
        setLineSize(lineWidthSize);
        setLineColor(lineColor);
        setCircleImageTintColor(circleColor);
    }


    public void setCircleImageResourceID(int resourceID) {
        positionOne.setImageResource(resourceID);
        positionTwo.setImageResource(resourceID);
        positionThree.setImageResource(resourceID);
        positionFour.setImageResource(resourceID);
        requestLayout();
    }

    public void setCircleImageTintColor(int circleColor) {
        //   ImageViewCompat.setImageTintList(positionOne, ColorStateList.valueOf(circleColor));
        //    ImageViewCompat.setImageTintList(positionTwo, ColorStateList.valueOf(circleColor));
        //    ImageViewCompat.setImageTintList(positionThree, ColorStateList.valueOf(circleColor));
        requestLayout();
    }


    public void setLineColorResource(int resource) {
        drawTwo.setPaintColorResource(resource);
        drawOne.setPaintColorResource(resource);
        requestLayout();
    }


    public void setLineColor(int circleColor) {
        drawTwo.setPaintColor(circleColor);
        drawOne.setPaintColor(circleColor);
        drawThree.setPaintColor(circleColor);
        drawFour.setPaintColor(circleColor);
        //   drawTriAngle.setPaintColor(circleColor);
        requestLayout();
    }


    public void setCircleImageTintResource(int colorResource) {
        ImageViewCompat.setImageTintList(positionOne, ColorStateList.valueOf(ContextCompat.getColor(getContext(), colorResource)));
        ImageViewCompat.setImageTintList(positionTwo, ColorStateList.valueOf(ContextCompat.getColor(getContext(), colorResource)));
        ImageViewCompat.setImageTintList(positionThree, ColorStateList.valueOf(ContextCompat.getColor(getContext(), colorResource)));
        requestLayout();
    }


    public void setCircleImageBitmap(Bitmap bitmap) {
        positionOne.setImageBitmap(bitmap);
        positionTwo.setImageBitmap(bitmap);
        positionThree.setImageBitmap(bitmap);
        requestLayout();

    }

    public void setCircleImageDrawable(Drawable imageDrawable) {
        if (null != imageDrawable) {
            positionOne.setImageDrawable(imageDrawable);
            positionTwo.setImageDrawable(imageDrawable);
            positionThree.setImageDrawable(imageDrawable);
            requestLayout();
        }
    }


    public void setLineSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = lineWidthSize;
        drawOne.setLineSize(pixelSize);
        drawTwo.setLineSize(pixelSize);
        drawThree.setLineSize(pixelSize);
        drawFour.setLineSize(pixelSize);
        requestLayout();
    }


    public void setCircleSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = circleSize;
        positionOne.getLayoutParams().width = pixelSize;
        positionTwo.getLayoutParams().width = pixelSize;
        positionThree.getLayoutParams().width = pixelSize;
        positionFour.getLayoutParams().width = pixelSize;


        positionOne.getLayoutParams().height = pixelSize;
        positionTwo.getLayoutParams().height = pixelSize;
        positionThree.getLayoutParams().height = pixelSize;
        positionFour.getLayoutParams().height = pixelSize;
        requestLayout();
    }

    OnDragTouchListener onDragTouchListenerOne;
    OnDragTouchListener onDragTouchListenerTwo;
    OnDragTouchListener onDragTouchListenerThree;
    OnDragTouchListener onDragTouchListenerFour;

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
        circleColor = ContextCompat.getColor(getContext(), R.color.orange);
        textColor = ContextCompat.getColor(getContext(), R.color.yellow);

        this.lineColor = lineColor;
        LayoutInflater.from(getContext()).inflate(R.layout.draw_square_view, this);
        positionOne = findViewById(R.id.positionOne);
        positionThree = findViewById(R.id.positionThree);
        positionFour = findViewById(R.id.positionFour);
        positionTwo = findViewById(R.id.positionTwo);
        parentView = findViewById(R.id.parentView);
        drawOne = findViewById(R.id.drawOne);
        drawTwo = findViewById(R.id.drawTwo);
        drawThree = findViewById(R.id.drawThree);
        drawFour = findViewById(R.id.drawFour);

        drawTriAngle = findViewById(R.id.drawTriAngle);


        onDragTouchListenerOne = new OnDragTouchListener(positionOne, parent, this);
        onDragTouchListenerTwo = new OnDragTouchListener(positionTwo, this.parent, this);
        onDragTouchListenerThree = new OnDragTouchListener(positionThree, this.parent, this);
        onDragTouchListenerFour = new OnDragTouchListener(positionFour, this.parent, this);

        onDragTouchListenerParent = new OnDragTouchListener(drawTriAngle, this.parent, this);

        positionOne.setOnTouchListener(onDragTouchListenerOne);
        positionTwo.setOnTouchListener(onDragTouchListenerTwo);
        positionThree.setOnTouchListener(onDragTouchListenerThree);
        positionFour.setOnTouchListener(onDragTouchListenerFour);
        // showAngle.setOnTouchListener(onDragTouchListenerAngle);
        drawTriAngle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                onDragTouchListenerOne.onTouch(positionOne, event);
                onDragTouchListenerTwo.onTouch(positionTwo, event);
                onDragTouchListenerThree.onTouch(positionThree, event);
                onDragTouchListenerFour.onTouch(positionFour, event);

                onDragTouchListenerParent.onTouch(drawTriAngle, event);
                draw();
                return true;
            }
        });    // drawTwo.draw();
        //  drawOne.draw();
        setColorsAndSize();
        positionOne.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                positionOne.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                setViewSize(startingX, startingY);
                draw();
                refresh();
            }
        });

    }

float two = 2f;
    private void draw() {
        drawOne.drawLine(positionOne.getX() + positionOne.getWidth() / two,
                positionOne.getY() + positionOne.getHeight() / two,
                positionTwo.getX() + positionTwo.getWidth() / two,
                positionTwo.getY() + positionTwo.getHeight() / two);

        drawTwo.drawLine(positionOne.getX() + positionOne.getWidth() / two,
                positionOne.getY() + positionOne.getHeight() / two,
                positionThree.getX() + positionThree.getWidth() / two,
                positionThree.getY() + positionThree.getHeight() / two);

        drawThree.drawLine(positionTwo.getX() + positionTwo.getWidth() / two,
                positionTwo.getY() + positionTwo.getHeight() / two,
                positionFour.getX() + positionFour.getWidth() / two,
                positionFour.getY() + positionFour.getHeight() / two);

        drawFour.drawLine(positionFour.getX() + positionFour.getWidth() / two,
                positionFour.getY() + positionFour.getHeight() / two,
                positionThree.getX() + positionThree.getWidth() / two,
                positionThree.getY() + positionThree.getHeight() / two);
        rect();


    }


    void rect() {
        float minX1 = Math.abs(positionOne.getX()) > Math.abs(positionThree.getX()) ?
                Math.abs(positionThree.getX()) : Math.abs(positionOne.getX());
        float minX2 = Math.abs(positionTwo.getX()) > Math.abs(positionFour.getX()) ?
                Math.abs(positionFour.getX()) : Math.abs(positionTwo.getX());


        float maxX1 = Math.abs(positionOne.getX()) > Math.abs(positionThree.getX()) ?
                Math.abs(positionOne.getX()) : Math.abs(positionThree.getX());
        float maxX2 = Math.abs(positionTwo.getX()) > Math.abs(positionFour.getX()) ?
                Math.abs(positionTwo.getX()) : Math.abs(positionFour.getX());


        float minY1 = Math.abs(positionOne.getY()) > Math.abs(positionThree.getY()) ?
                Math.abs(positionThree.getY()) : Math.abs(positionOne.getY());
        float minY2 = Math.abs(positionTwo.getY()) > Math.abs(positionFour.getY()) ?
                Math.abs(positionFour.getY()) : Math.abs(positionTwo.getY());


        float maxY1 = Math.abs(positionOne.getY()) > Math.abs(positionThree.getY()) ?
                Math.abs(positionOne.getY()) : Math.abs(positionThree.getY());
        float maxY2 = Math.abs(positionTwo.getY()) > Math.abs(positionFour.getY()) ?
                Math.abs(positionTwo.getY()) : Math.abs(positionFour.getY());


        float minX = minX1 > minX2 ? minX2 : minX1;
        float maxX = maxX1 > maxX2 ? maxX1 : maxX2;
        float minY = minY1 > minY2 ? minY2 : minY1;
        float maxY = maxY1 > maxY2 ? maxY1 : maxY2;

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

    int addValue = 20;

    public void movingView(MotionEvent event) {
        positionTwo.setX(event.getX());
        positionTwo.setY(event.getY());
        onDragging(positionTwo);
        draw();
        refresh();
    }

    public void setViewSize(float x, float y) {
        positionTwo.setX(x - addValue);
        positionTwo.setY(y - addValue);

        positionOne.setX(x - addValue);
        positionOne.setY(y + addValue);


        positionFour.setX(x + addValue);
        positionFour.setY(y - addValue);

        positionThree.setX(x + addValue);
        positionThree.setY(y + addValue);
    }


    @Override
    public void onDragStart(View view) {

        viewTouched(true);
        draw();
    }

    @Override
    public void onDragging(View view) {
        float x = view.getX();
        float y = view.getY();
        int id = view.getId();

        if (id == R.id.positionOne) {
            positionTwo.setX(x);
            positionThree.setY(y);
        } else if (id == R.id.positionTwo) {
            positionOne.setX(x);
            positionFour.setY(y);
        } else if (id == R.id.positionThree) {
            positionFour.setX(x);
            positionOne.setY(y);
        } else if (id == R.id.positionFour) {
            positionThree.setX(x);
            positionTwo.setY(y);
        }
        draw();

    }

    @Override
    public void onDraggingTouchEvent(View view, MotionEvent event) {
        if (view.getId() == R.id.positionOne) {
            onDragTouchListenerParent.onTouch(drawTriAngle, event);


        }
        draw();
    }

    @Override
    public void onDragEnd(View view) {
        draw();
        viewTouched(false);
        refresh();
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

