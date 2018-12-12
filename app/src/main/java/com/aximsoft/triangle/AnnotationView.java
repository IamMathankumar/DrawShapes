package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

@SuppressLint("ClickableViewAccessibility")
public class AnnotationView extends FrameLayout {
    ConstraintLayout parentView;
    FreeDrawView freeDraw;
    int freeDrawCount = -1;
    long clickedMilliSecond = 0;
    boolean TextViewDialogInFront = false;

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
    }


    private void onAddView(View view) {
        freeDrawDisable();
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        parentView.addView(view, layoutParams);
        view.bringToFront();
    }

    public void onAddTriAngle() {
        DrawTriangleView triangleView = new DrawTriangleView(getContext(), parentView);
        onAddView(triangleView);
    }

    public void onAddSquare() {
        DrawSquareView squareView = new DrawSquareView(getContext(), parentView);
        onAddView(squareView);
    }

    public void onAddCircle() {
        DrawCircleView circleView = new DrawCircleView(getContext(), parentView);
        onAddView(circleView);
    }

    public void onAddLine() {
        DrawLineView circleView = new DrawLineView(getContext(), parentView);
        onAddView(circleView);
    }

    public void onAddFreeDraw() {
        freeDraw = new FreeDrawView(getContext());
        onAddView(freeDraw);
        freeDrawEnable();
    }


    public void onAddText() {
        freeDrawDisable();
        if (!TextViewDialogInFront) {
            final InsertTextView textView = new InsertTextView(getContext(), parentView);
            onAddView(textView);
            textView.setTextViewListener(new InsertTextView.InsertTextViewListener() {
                @Override
                public void removeView() {
                    parentView.removeView(textView);
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


    }

    public void clearAll() {
        freeDrawDisable();
        parentView.removeAllViews();
        freeDraw = null;
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


}

