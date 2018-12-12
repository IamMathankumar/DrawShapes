package com.aximsoft.triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class LineView extends View {
    private Paint paint = new Paint();
    Canvas canvas;
    PointF pointA = new PointF(0, 0);
    PointF pointB = new PointF(0, 0);

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPaintColor(int color) {
        paint.setColor(color);
        draw();
    }



    public void setPaintColorResource(int resource) {
        paint.setColor(resource);
        draw();
    }

    public void setLineSize(int size) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(ContextCompat.getColor(getContext(),R.color.yellow));

        draw();
    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paint);

        super.onDraw(canvas);
        this.canvas = canvas;
    }

    public void setPointA(PointF point) {
        pointA = point;
    }

    public void setPointB(PointF point) {
        pointB = point;
    }

    public void draw() {
        invalidate();
        requestLayout();
    }

    public void drawLine(float startX, float startY, float stopX, float stopY) {
        pointA = new PointF(startX, startY);
        pointB = new PointF(stopX, stopY);
        draw();
    }



    //127.0, 1122.0, 920.0, 446.0
}