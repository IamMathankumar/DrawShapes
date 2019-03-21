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
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
@SuppressLint("ClickableViewAccessibility")
public class DrawTriangleView extends FrameLayout implements OnDragTouchListener.OnDragActionListener {

    ImageView positionOne;
    ImageView positionTwo, positionThree;
    LineView drawOne, drawTwo;
    View drawTriAngle;
    ConstraintLayout parentView;
    TextView showAngle;
    View parent;
    ArcView arcView;


    public DrawTriangleView(Context context, View parent, int triangleColor, MotionEvent event) {
        super(context);
        this.parent = parent;
        init(triangleColor, event.getX(), event.getY());
    }


    public DrawTriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);


    }

    int circleSize = 60, lineWidthSize = 10, textSize = 10;
    int circleColor = Color.BLACK, lineColor = Color.GRAY, textColor = Color.BLACK;

    private void initView(Context context, AttributeSet attrs) {
        init(Color.GREEN, 0f, 0f);
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

            Drawable circleDrawable = a.getDrawable(R.styleable.LaneVisionDrawView_tri_circleDrawable);

            setColorsAndSize();
          /*  if (circleDrawable == null) {
                setCircleImageTintColor(circleColor);
            }
            setCircleImageDrawable(circleDrawable);*/
        } finally {
            a.recycle();
        }
    }

    public void setCircleImageResourceID(int resourceID) {
        positionOne.setImageResource(resourceID);
        positionTwo.setImageResource(resourceID);
        positionThree.setImageResource(resourceID);
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


    public void setArcColorResource(int resource) {
        arcView.setPaintColorResource(resource);
        requestLayout();
    }


    public void setLineColor(int circleColor) {
        drawTwo.setPaintColor(circleColor);
        drawOne.setPaintColor(circleColor);
        //   drawTriAngle.setPaintColor(circleColor);
        requestLayout();
    }

    public void setArcColor(int circleColor) {
        arcView.setPaintColor(circleColor);
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

    public void setTextColor(int resource) {
        showAngle.setTextColor(resource);
        requestLayout();
    }


    public void setTextSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = textSize;
        showAngle.setTextSize(pixelSize);
        requestLayout();
    }

    public void setLineSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = lineWidthSize;
        drawOne.setLineSize(pixelSize);
        drawTwo.setLineSize(pixelSize);
        arcView.setLineSize(pixelSize);
        requestLayout();
    }

    public void setArcSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = lineWidthSize;
        arcView.setLineSize(pixelSize);
        requestLayout();
    }


    public void setCircleSize(int pixelSize) {
        if (pixelSize < 0) pixelSize = circleSize;
        positionOne.getLayoutParams().width = pixelSize;
        positionTwo.getLayoutParams().width = pixelSize;
        positionThree.getLayoutParams().width = pixelSize;

        positionOne.getLayoutParams().height = pixelSize;
        positionTwo.getLayoutParams().height = pixelSize;
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

    private void init(final int lineColor, final float x, final float y) {
        circleSize = getResources().getDimensionPixelOffset(R.dimen.drawCircleWidth);
        lineWidthSize = getResources().getDimensionPixelOffset(R.dimen.drawLineWidth);
        textSize = getResources().getDimensionPixelOffset(R.dimen.drawTextSize);
        circleColor = ContextCompat.getColor(getContext(),R.color.orange);
        this.lineColor = lineColor;
        textColor = lineColor;


        LayoutInflater.from(getContext()).inflate(R.layout.draw_triangle_view, this);
        positionOne = findViewById(R.id.positionOne);
        positionThree = findViewById(R.id.positionThree);
        positionTwo = findViewById(R.id.positionTwo);
        parentView = findViewById(R.id.parentView);
        drawOne = findViewById(R.id.drawOne);
        drawTwo = findViewById(R.id.drawTwo);
        showAngle = findViewById(R.id.showAngle);
        drawTriAngle = findViewById(R.id.drawTriAngle);
        arcView = findViewById(R.id.drawArc);
        drawOne.setPaintColor(lineColor);
        drawTwo.setPaintColor(lineColor);
        drawOne.setPaintColor(lineColor);
        arcView.setPaintColor(lineColor);
        setColorsAndSize();
        setTextColor(lineColor);

        onDragTouchListenerOne = new OnDragTouchListener(positionOne, parent, this);
        onDragTouchListenerTwo = new OnDragTouchListener(positionTwo, this.parent, this);
        onDragTouchListenerThree = new OnDragTouchListener(positionThree, this.parent, this);
        onDragTouchListenerAngle = new OnDragTouchListener(showAngle, this.parent, this);
        onDragTouchListenerParent = new OnDragTouchListener(drawTriAngle, this.parent, this);

        positionOne.setOnTouchListener(onDragTouchListenerOne);
        positionTwo.setOnTouchListener(onDragTouchListenerTwo);
        positionThree.setOnTouchListener(onDragTouchListenerThree);
        // showAngle.setOnTouchListener(onDragTouchListenerAngle);

        drawTriAngle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                movePosition(event);
                return true;
            }
        });    // drawTwo.draw();

        positionOne.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                positionOne.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setColorsAndSize();
                setColor(lineColor);
                setViewSize(x, y);
                draw();
            }
        });

    }

    int viewSize = 100;
    int addValue = 200;

    public void setViewSize(float x, float y) {


        positionOne.setX(x);
        positionOne.setY(y);

        positionTwo.setX(x);
        positionTwo.setY(y - addValue);


        positionThree.setX(x + addValue);
        positionThree.setY(y);

        showAngle.setX(positionOne.getX() - 20);
        showAngle.setY(positionOne.getY() - 20);

       /* float xx = x + ((positionOne.getX() - positionThree.getX()) / 2);
        float yy = y - ((positionOne.getY() - positionTwo.getY()) / 2);


        drawTriAngle.setX(x - addValue);
        drawTriAngle.setY(y - addValue);*/

        draw();
    }

    public void movePosition(MotionEvent event) {
        onDragTouchListenerOne.onTouch(positionOne, event);
        onDragTouchListenerTwo.onTouch(positionTwo, event);
        onDragTouchListenerThree.onTouch(positionThree, event);
        onDragTouchListenerParent.onTouch(drawTriAngle, event);
        draw();

    }

    private void setColorsAndSize(){
        //  drawOne.draw();
        setCircleSize(circleSize);
        setLineSize(lineWidthSize);
        setTextSize(textSize);
        setTextColor(textColor);
        setCircleImageTintColor(circleColor);
    }

    public void setColor(int color) {
        this.lineColor = color;
        drawOne.setPaintColor(lineColor);
        drawTwo.setPaintColor(lineColor);
        drawOne.setPaintColor(lineColor);
        arcView.setPaintColor(lineColor);
        setColorsAndSize();
        setTextColor(lineColor);
    }

    private void draw() {
        drawOne.drawLine(positionOne.getX() + positionOne.getWidth() / 2,
                positionOne.getY() + positionOne.getHeight() / 2,
                positionTwo.getX() + positionTwo.getWidth() / 2,
                positionTwo.getY() + positionTwo.getHeight() / 2);

        drawTwo.drawLine(positionOne.getX() + positionOne.getWidth() / 2,
                positionOne.getY() + positionOne.getHeight() / 2,
                positionThree.getX() + positionThree.getWidth() / 2,
                positionThree.getY() + positionThree.getHeight() / 2);

     /*   drawTriAngle.drawTriAngle(positionOne.getX() + positionOne.getWidth() / 2,
                positionOne.getY() + positionOne.getHeight() / 2,
                positionTwo.getX() + positionTwo.getWidth() / 2,
                positionTwo.getY() + positionTwo.getHeight() / 2,
                positionThree.getX() + positionThree.getWidth() / 2,
                positionThree.getY() + positionThree.getHeight() / 2);  */
        PointF A1 = new PointF(positionOne.getX() + positionOne.getWidth() / 2, positionOne.getY() + positionOne.getHeight() / 2);
        PointF A2 = new PointF(positionThree.getX() + positionThree.getWidth() / 2, positionThree.getY() + positionThree.getHeight() / 2);


        PointF B1 = new PointF(positionOne.getX() + positionOne.getWidth() / 2, positionOne.getY() + positionOne.getHeight() / 2);
        PointF B2 = new PointF(positionTwo.getX() + positionTwo.getWidth() / 2, positionTwo.getY() + positionTwo.getHeight() / 2);

        float calculatedAngle = angleBetween2Lines(A1, A2, B1, B2);
        String value = "";
        if (calculatedAngle > 180) {
            value = "" + (int) (360 - calculatedAngle);
        } else {
            value = value + (int) calculatedAngle;
        }
        showAngle.setText(value);
        RectF oneRect = calculateRectOnScreen(positionOne);
        RectF twoRect = calculateRectOnScreen(positionTwo);
        RectF threeRect = calculateRectOnScreen(positionThree);

        float height = Math.abs(oneRect.bottom - threeRect.top) > Math.abs(oneRect.bottom - twoRect.top) ?
                Math.abs(oneRect.bottom - threeRect.top) : Math.abs(oneRect.bottom - twoRect.top);
        float width = Math.abs(oneRect.left - threeRect.right) > Math.abs(oneRect.left - twoRect.right) ?
                Math.abs(oneRect.left - threeRect.right) : Math.abs(oneRect.left - twoRect.right);
        drawTriAngle.getLayoutParams().width = (int) (width);
        drawTriAngle.getLayoutParams().height = (int) (height);


        float centerX = (positionOne.getX() + positionTwo.getX() + positionThree.getX()) / 3;
        float centerY = (positionOne.getY() + positionTwo.getY() + positionThree.getY()) / 3;
        drawTriAngle.setX(centerX - drawTriAngle.getWidth() / 2);
        drawTriAngle.setY(centerY - drawTriAngle.getHeight() / 2);

        float x1 = positionOne.getX() + positionOne.getWidth() / 2;
        float y1 = positionOne.getY() + positionOne.getHeight() / 2;
        float x2 = positionTwo.getX() + positionTwo.getWidth() / 2;
        float y2 = positionTwo.getY() + positionTwo.getHeight() / 2;
        float x3 = positionThree.getX() + positionThree.getWidth() / 2;
        float y3 = positionThree.getY() + positionThree.getHeight() / 2;

        float distanceOne = (float) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
        float distanceTwo = (float) (Math.sqrt((Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2))));

        float defValue = 100;
        float startX = (1 - defValue / distanceOne) * x1 + (defValue / distanceOne) * x2;
        float startY = (1 - defValue / distanceOne) * y1 + (defValue / distanceOne) * y2;
        float stopX = (1 - defValue / distanceTwo) * x1 + (defValue / distanceTwo) * x3;
        float stopY = (1 - defValue / distanceTwo) * y1 + (defValue / distanceTwo) * y3;
        arcView.drawArc(startX, startY, stopX,
                stopY, calculatedAngle);
        //  System.out.println("Distance Y: " + distanceOne);
        // System.out.println("Distance Z: " + distanceTwo);


    }

    public static float angleBetween2Lines(PointF A1, PointF A2, PointF B1, PointF B2) {
        float angle1 = (float) Math.atan2(A2.y - A1.y, A1.x - A2.x);
        float angle2 = (float) Math.atan2(B2.y - B1.y, B1.x - B2.x);
        float calculatedAngle = (float) Math.toDegrees(angle1 - angle2);
        if (calculatedAngle < 0) calculatedAngle += 360;
        return calculatedAngle;
    }


    @Override
    public void onDragStart(View view) {
        draw();
    }

    @Override
    public void onDragging(View view) {
        draw();
    }

    @Override
    public void onDraggingTouchEvent(View view, MotionEvent event) {
        onDragTouchListenerAngle.onTouch(showAngle, event);
        if (view.getId() == R.id.positionOne) {
            onDragTouchListenerParent.onTouch(drawTriAngle, event);
        }
        draw();
    }

    @Override
    public void onDragEnd(View view) {
        draw();
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

