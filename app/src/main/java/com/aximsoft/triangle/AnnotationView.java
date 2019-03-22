/*
 * Created by Mathankumar K On 1/9/19 11:54 AM
 * Copyright (c) Aximsoft 2019.
 * All rights reserved.
 */

package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

@SuppressLint("ClickableViewAccessibility")

public class AnnotationView extends FrameLayout implements View.OnTouchListener {
    ConstraintLayout parentView;
    FreeDrawView freeDraw;
    boolean TextViewDialogInFront = false;
    private final String TAG = "AnnotationView";

    AnnotationEnum lastSelection = AnnotationEnum.FREE_DRAW;
    int color = Color.GREEN;

    public AnnotationView(@NonNull Context context) {
        super(context);
        init();

    }

    public AnnotationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnnotationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.draw_annotation_view, this);
        parentView = findViewById(R.id.annotationParentView);
        // inside onCreate(), needs "implements View.OnDragListener"
        parentView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("prefs", "X=" + String.valueOf(event.getX()) + " / Y=" + String.valueOf(event.getY()));
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "Down");
            addViewInOnTouch(event);
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.e(TAG, "Move");
            dragViewInOnTouch(event);
            return true;

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.e(TAG, "Up");
            return true;
        }


        return false;
    }


    public void changeFreeDrawColor(int color) {
        if (freeDraw != null)
            freeDraw.setPaintColor(color);
    }

    private void onAddView(View view) {
        freeDrawDisable();
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        parentView.addView(view, layoutParams);
        view.bringToFront();
        if (view != freeDraw)
            enableButtons();
    }


    public void onAddTriAngle(int color) {
        freeDrawDisable();
        this.color = color;
        lastSelection = AnnotationEnum.TRIANGLE;
    }

    public void onAddSquare(int color) {
        freeDrawDisable();
        this.color = color;
        lastSelection = AnnotationEnum.SQUARE;
    }

    public void onAddCircle(int color) {
        freeDrawDisable();
        this.color = color;
        lastSelection = AnnotationEnum.CIRCLE;
    }

    public void onAddLine(int color) {
        freeDrawDisable();
        this.color = color;
        lastSelection = AnnotationEnum.LINE;
    }


    public void onAddFreeDraw(int color) {
        this.color = color;
        lastSelection = AnnotationEnum.FREE_DRAW;
        onAddFreeDraw();
    }


    public void onAddText(int color) {
        freeDrawDisable();
        this.color = color;
        lastSelection = AnnotationEnum.TEXT;

    }


    public void onAddTriAngle(MotionEvent event) {
        DrawTriangleView triangleView = new DrawTriangleView(getContext(), parentView, color, event);
        triangleView.setColor(color);
        onAddView(triangleView);
    }

    public void onAddSquare(MotionEvent event) {
        DrawSquareView squareView = new DrawSquareView(getContext(), parentView, color, event.getX(), event.getY());
        onAddView(squareView);
    }

    public void onAddCircle(MotionEvent event) {
        DrawCircleView circleView = new DrawCircleView(getContext(), parentView, color, event.getX(), event.getY());
        onAddView(circleView);
    }

    public void onAddLine(MotionEvent event) {
        DrawLineView circleView = new DrawLineView(getContext(), parentView, color, event.getX(), event.getY());
        onAddView(circleView);
    }


    public void onAddFreeDraw() {
        checkFreeDrawProcessed();
        freeDraw = new FreeDrawView(getContext(), new FreeDrawView.FreeDrawListener() {
            @Override
            public void freeDrawStarted() {
                enableButtons();
            }
        }, color);
        onAddView(freeDraw);
        freeDrawEnable();
    }


    public void onAddText(MotionEvent event) {
        freeDrawDisable();
        if (!TextViewDialogInFront) {
            final InsertTextView textView = new InsertTextView(getContext(), parentView, color);
            onAddView(textView);
            textView.setTextViewListener(new InsertTextView.InsertTextViewListener() {
                @Override
                public void removeView() {
                    parentView.removeView(textView);
                    if (parentView.getChildCount() == 0) {
                        disableButtons();
                    }
                }

                @Override
                public void dialogClosed() {
                    TextViewDialogInFront = false;
                    freeDrawDisable();

                }
            });
            TextViewDialogInFront = true;

        }
    }

    public int getTotalCount() {
        return parentView.getChildCount();
    }

    public void undo() {
        freeDrawDisable();
        if (parentView.getChildCount() > 0)
            parentView.removeViewAt(parentView.getChildCount() - 1);
        else
            disableButtons();
    }

    public void clearAll() {
        freeDrawDisable();
        parentView.removeAllViews();
        freeDraw = null;
        disableButtons();
    }


    public void freeDrawDisable() {
        if (null != freeDraw) {
            freeDraw.setFreeDrawEnabled(false);
            freeDraw.setFocusable(false);

        }
    }

    public void freeDrawEnable() {
        if (null != freeDraw)
            freeDraw.setFreeDrawEnabled(true);


    }


    public void checkFreeDrawProcessed() {
        if (parentView != null && null != freeDraw && !freeDraw.isFreeDrawProcessed()) {
            parentView.removeView(freeDraw);
        } else if (parentView != null && null != freeDraw)
            freeDrawDisable();
    }

    private void disableButtons() {
        if (null != annotationViewListener) {
            annotationViewListener.disableButtons();
        }
    }


    private void enableButtons() {

        if (null != annotationViewListener) {
            annotationViewListener.enableButtons();
        }
    }

    AnnotationViewListener annotationViewListener;

    public void setAnnotationViewListener(AnnotationViewListener annotationViewListener) {
        this.annotationViewListener = annotationViewListener;
    }


    public interface AnnotationViewListener {
        void enableButtons();

        void disableButtons();
    }


    public void addViewInOnTouch(MotionEvent event) {
        switch (lastSelection) {
            case LINE:
                onAddLine(event);
                break;
            case CIRCLE:
                onAddCircle(event);
                break;
            case SQUARE:
                onAddSquare(event);
                break;
            case TRIANGLE:
                onAddTriAngle(event);
                break;
            case TEXT:
                onAddText(event);
                break;

            default:
                break;
        }
    }


    public void dragViewInOnTouch(MotionEvent event) {
        switch (lastSelection) {
            case LINE:
                if (parentView.getChildCount() > 0)
                    if (parentView.getChildAt(parentView.getChildCount() - 1) instanceof DrawLineView) {
                        ((DrawLineView) parentView.getChildAt(parentView.getChildCount() - 1)).movingView(event);
                    }
                break;
            case CIRCLE:
                if (parentView.getChildAt(parentView.getChildCount() - 1) instanceof DrawCircleView) {
                    ((DrawCircleView) parentView.getChildAt(parentView.getChildCount() - 1)).movingView(event.getX(),event.getY());
                }
                break;
            case SQUARE:
                if (parentView.getChildAt(parentView.getChildCount() - 1) instanceof DrawSquareView) {
                    ((DrawSquareView) parentView.getChildAt(parentView.getChildCount() - 1)).movingView(event);
                }
                break;
            case TRIANGLE:

                break;
            default:
                break;
        }
    }
}

