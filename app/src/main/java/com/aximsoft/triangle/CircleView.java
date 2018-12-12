package com.aximsoft.triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

public class CircleView extends View implements View.OnTouchListener, OnDragPinchTouchListener.OnDragActionListener {

    private static final String COLOR_HEX = "#00000000";
    private static final String COLOR_HEX_Stroke = "#E74300";

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private final Paint drawPaint;
    private float size = 15;
    OnDragPinchTouchListener listener;
    float x, y = 0f;


    View parent;
    private int w;
    private int h;

    long currentMillis = 0;
    boolean multiTOuch = false;

    public CircleView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        drawPaint = new Paint();
        init();
    }


    public void setSize(float size) {
        this.size = size;
    }



    public void setListener() {
        this.parent = (View) getParent();
        this.listener = new OnDragPinchTouchListener(this, parent, this);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // this.w = w;
        //  this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    void init() {
        setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        drawPaint.setColor(ContextCompat.getColor(getContext(),R.color.yellow));
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setAntiAlias(true);
        setOnMeasureCallback();

    }

    void setStrokeSize(int size){
        drawPaint.setStrokeWidth(size);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);


        getLayoutParams().width = (int) (size * mScaleFactor) * 2 + 10;
        getLayoutParams().height = (int) (size * mScaleFactor) * 2 + 10;
        requestLayout();
        x = getX();
        y = getY();
        canvas.save();
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, size * mScaleFactor, drawPaint);
        canvas.restore();


    }

    private void setOnMeasureCallback() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                size = 130;
                setListener();
            }
        });
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // Get the index of the pointer associated with the action.
        int action = MotionEventCompat.getActionMasked(motionEvent);
        if (action == MotionEvent.ACTION_POINTER_DOWN)
            multiTOuch = true;

        Log.d("CircleView", "CircleView The action is " + actionToString(action));
        if (motionEvent.getPointerCount() > 1) {
            Log.d("CircleView", "CircleView MultiTouch event");
            mScaleGestureDetector.onTouchEvent(motionEvent);
            listener.updateBounds();
        } else {
            if (!multiTOuch && currentMillis == 0) {
                currentMillis = System.currentTimeMillis();
            } else if (!multiTOuch && System.currentTimeMillis() - currentMillis > 100) {
                listener.setMultiTouch(false);
            }
            listener.onTouch(view, motionEvent);
        }

        if (action == MotionEvent.ACTION_UP) {
            multiTOuch = false;
            listener.setMultiTouch(true);
            currentMillis = 0;

        }
        return true;
    }

    @Override
    public void onDragStart(View view) {
        requestLayout();
        invalidate();
    }

    @Override
    public void onDragging(View view) {
        Log.d("CircleView", "CircleViewDrag onDragging");

    }

    @Override
    public void onDraggingTouchEvent(View view, MotionEvent event) {

    }

    @Override
    public void onDragEnd(View view) {
        Log.d("CircleView", "CircleViewDrag onDragEnd");
        requestLayout();
        invalidate();
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 3.0f));
            invalidate();
            System.out.println("Helllooooo :" + mScaleFactor);
            return true;
        }
    }

    // Given an action int, returns a string description
    public static String actionToString(int action) {
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                return "Down";
            case MotionEvent.ACTION_MOVE:
                return "Move";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "Pointer Down";
            case MotionEvent.ACTION_UP:
                return "Up";
            case MotionEvent.ACTION_POINTER_UP:
                return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE:
                return "Outside";
            case MotionEvent.ACTION_CANCEL:
                return "Cancel";
        }
        return "";
    }
}