package com.aximsoft.triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {
    private Paint paint = new Paint();
    float calculatedAngle = 0f;
    Canvas canvas;
    PointF pointA = new PointF(0, 0);
    PointF pointB = new PointF(0, 0);

    public ArcView(Context context) {
        super(context);
        initPaint();
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    void initPaint() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.yellow));

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
        paint.setStrokeWidth(size);

        draw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paint);

        //  arcBetween(pointA, pointB, paint, canvas);
        drawCurvedArrow(pointA.x, pointA.y, pointB.x, pointB.y, 90, canvas);
        super.onDraw(canvas);
        this.canvas = canvas;
    }

    public void drawCurvedArrow(float x1, float y1, float x2, float y2, int curveRadius, Canvas canvas) {


        final Path path = new Path();
        float midX = x1 + ((x2 - x1) / 2);
        float midY = y1 + ((y2 - y1) / 2);
        float xDiff = midX - x1;
        float yDiff = midY - y1;

        int val = 90;
        if (calculatedAngle > 180) {
            val = -90;
        }
        double angle = (Math.atan2(yDiff, xDiff) * (180 / Math.PI)) + val;
        double angleRadians = Math.toRadians(angle);
        float pointX = (float) (midX + curveRadius * Math.cos(angleRadians));
        float pointY = (float) (midY + curveRadius * Math.sin(angleRadians));

        path.moveTo(x1, y1);
        path.cubicTo(x1, y1, pointX, pointY, x2, y2);
        //  System.out.println("Distance a1Degrees: " + Math.atan2(yDiff, xDiff));

        canvas.drawPath(path, paint);

    }


    private static void arcBetween(PointF e1, PointF e2, Paint paint, Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        float a1Degrees = 45.0f;
        double a1 = Math.toRadians(a1Degrees);
        // l1 is half the length of the line from e1 to e2
        double dx = e2.x - e1.x, dy = e2.y - e1.y;
        double l = Math.sqrt((dx * dx) + (dy * dy));
        double l1 = l / 2.0;
        // h is length of the line from the middle of the connecting line to the
        //  center of the circle.
        double h = l1 / (Math.tan(a1 / 2.0));

        // r is the radius of the circle
        double r = l1 / (Math.sin(a1 / 2.0));
        // a2 is the angle at which L intersects the x axis
        double a2 = Math.atan2(dy, dx);
        // a3 is the angle at which H intersects the x axis
        double a3 = (Math.PI / 2.0) - a2;
        // m is the midpoint of the line from e1 to e2
        double mX = (e1.x + e2.x) / 2.0;
        double mY = (e1.y + e2.y) / 2.0;

        // c is the the center of the circle
        double cY = mY + (h * Math.sin(a3));
        double cX = mX - (h * Math.cos(a3));
        // rect is the square RectF that bounds the "oval"
        RectF oval =
                new RectF((float) (cX - r), (float) (cY - r), (float) (cX + r), (float) (cY + r));
        // a4 is the starting sweep angle
        double rawA4 = Math.atan2(e1.y - cY, e1.x - cX);

        float a4 = (float) Math.toDegrees(rawA4);
        System.out.println("Distance a1Degrees: " + a4);
        canvas.drawArc(oval, a4, a1Degrees, false, paint);
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

    public void drawArc(float startX, float startY, float stopX, float stopY, float calculatedAngle) {
        pointA = new PointF(startX, startY);
        pointB = new PointF(stopX, stopY);
        this.calculatedAngle = calculatedAngle;
        draw();
    }

    //127.0, 1122.0, 920.0, 446.0
}