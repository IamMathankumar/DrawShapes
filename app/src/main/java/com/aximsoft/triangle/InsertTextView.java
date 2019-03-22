/*
 * Created by Mathankumar K On 1/9/19 11:54 AM
 * Copyright (c) Aximsoft 2019.
 * All rights reserved.
 */

package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class InsertTextView extends FrameLayout implements OnDragPinchTouchListener.OnDragActionListener, DialogAddText.AddTextListener, View.OnTouchListener {

    TextView positionOne;
    ConstraintLayout parentView;
    View parent;



    private ScaleGestureDetector mScaleGestureDetector;

    OnDragPinchTouchListener listener;


    long currentMillis = 0;
    boolean multiTOuch = false;


    public InsertTextView(Context context, View parent, int color) {
        super(context);
        this.parent = parent;
        init(color);
    }
    public InsertTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(Color.GREEN);


    }


    private void init(int color) {
        LayoutInflater.from(getContext()).inflate(R.layout.draw_insert_text_view, this);
        positionOne = findViewById(R.id.positionOne);
        parentView = findViewById(R.id.parentView);

        listener = new OnDragPinchTouchListener(positionOne, parent, this);
        positionOne.setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        positionOne.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                positionOne.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        positionOne.setTextColor(color);
        DialogAddText cdd = new DialogAddText(getContext(), this, "");
        if (null != cdd.getWindow())
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();

    }


    @Override
    public void onDragStart(View view) {

    }

    @Override
    public void onDragging(View view) {

    }

    @Override
    public void onDraggingTouchEvent(View view, MotionEvent event) {
        //  onDragTouchListenerParent.onTouch(drawTriAngle, event);
        //  draw();
    }

    @Override
    public void onDragEnd(View view) {


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // Get the index of the pointer associated with the action.
        int action = motionEvent.getActionMasked();
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

            if (!multiTOuch && System.currentTimeMillis() - currentMillis < 120) {
                System.out.println("Difference : " + (System.currentTimeMillis() - currentMillis));
                DialogAddText cdd = new DialogAddText(getContext(), this, positionOne.getText().toString());
                if (null != cdd.getWindow())
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
            multiTOuch = false;
            listener.setMultiTouch(true);
            currentMillis = 0;
        }
        return true;
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

    @Override
    public void onTextChange(String text) {
        positionOne.setText(text);
    }

    @Override
    public void onAdd(String text) {
        positionOne.setText(text);
        if(null!=insertTextViewListener)
            insertTextViewListener.dialogClosed();
    }

    @Override
    public void onCancel(String text) {
        positionOne.setText(text);
        if (null != insertTextViewListener) {
            if (text.isEmpty()) {
                insertTextViewListener.removeView();
                insertTextViewListener.dialogClosed();
            }
        }
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float size = positionOne.getTextSize();
            Log.d("TextSizeStart", String.valueOf(size));

            float factor = scaleGestureDetector.getScaleFactor();
            factor = Math.max(0.1f,
                    Math.min(factor, 1.5f));
            Log.d("Factor", String.valueOf(factor));


            float product = size * factor;
            if (product < 100) {
                Log.d("TextSize", String.valueOf(product));
                positionOne.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);

                size = positionOne.getTextSize();
                Log.d("TextSizeEnd", String.valueOf(size));
            }
            return true;
        }
    }

    InsertTextViewListener insertTextViewListener;

    public void setTextViewListener(InsertTextViewListener listener) {
        this.insertTextViewListener = listener;
    }

    interface InsertTextViewListener {

        void removeView();

        void dialogClosed();
    }

}

