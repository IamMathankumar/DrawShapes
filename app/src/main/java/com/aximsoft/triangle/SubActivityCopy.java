package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class SubActivityCopy extends AppCompatActivity {
    ConstraintLayout parentView;
    private int _xDelta;
    private int _yDelta;
    DrawTriangleView triangleView;
    DrawLineView lineView,lineView2;
    DrawSquareView squareView;
    DrawCircleView circleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity_sub);
        parentView = findViewById(R.id.parentView);
        triangleView = new DrawTriangleView(this, parentView);
         lineView = new DrawLineView(this, parentView);
         lineView2 = new DrawLineView(this, parentView);

         squareView = new DrawSquareView(this, parentView);
         circleView = new DrawCircleView(this, parentView);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        triangleView.setLayoutParams(layoutParams);
        parentView.addView(triangleView, layoutParams);

        squareView.setLayoutParams(layoutParams);
        parentView.addView(squareView, layoutParams);

        circleView.setLayoutParams(layoutParams);
        parentView.addView(circleView, layoutParams);
        lineView.setLayoutParams(layoutParams);
        parentView.addView(lineView, layoutParams);
        lineView2.setLayoutParams(layoutParams);
        parentView.addView(lineView2, layoutParams);
        final FreeDrawView freeDraw = new FreeDrawView(this);
        freeDraw.setLayoutParams(layoutParams);
        parentView.addView(freeDraw, layoutParams);
        final InsertTextView textView = new InsertTextView(this, parentView);
        textView.setLayoutParams(layoutParams);
        parentView.addView(textView, layoutParams);
        bringFront();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                freeDraw.setFreeDrawEnabled(false);


            }
        }, 10000);
    }


    void bringFront() {
        lineView.bringToFront();
        lineView2.bringToFront();
        circleView.bringToFront();
        squareView.bringToFront();
        triangleView.bringToFront();
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        parentView.invalidate();
        return true;
    }
}

