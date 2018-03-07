package com.compatiblenumbers.helper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.compatiblenumbers.activities.CompatibleCanvasActivity;
import com.compatiblenumbers.model.TextBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CirclesDrawingView extends View {

    private static final String TAG = CirclesDrawingView.class.getSimpleName();

    /**
     * Main bitmap
     */
    private Bitmap mBitmap = null;

    private Rect mMeasuredRect;

    /**
     * Stores data about single circle
     */
    private static class CircleArea {
        int radius;
        int centerX;
        int centerY;

        CircleArea(int centerX, int centerY, int radius) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        @Override
        public String toString() {
            return "Circle[" + centerX + ", " + centerY + ", " + radius + "]";
        }
    }

    /**
     * Paint to draw circles
     */
    private Paint mCirclePaint;

    private final Random mRadiusGenerator = new Random();
    // Radius limit in pixels
    private final static int RADIUS_LIMIT = 5;

    private static final int CIRCLES_LIMIT = 1;

    public static final int RECT_LIMIT = 4;

    /**
     * All available circles
     */
    private HashSet<CircleArea> mCircles = new HashSet<CircleArea>(CIRCLES_LIMIT);
    private SparseArray<CircleArea> mCirclePointer = new SparseArray<CircleArea>(CIRCLES_LIMIT);

    private List<Rect> mRectangles;
    private List<Paint> myPaints;
    private List<TextBlock> textBlocks;

    public void setRectangles(List mRectangles, List<TextBlock> textBlocks) {
        this.mRectangles = new ArrayList<>(RECT_LIMIT);
        this.myPaints = new ArrayList<>(RECT_LIMIT);
        this.textBlocks = new ArrayList<>(RECT_LIMIT);
        this.mRectangles = mRectangles;
        this.textBlocks = textBlocks;

        //red
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(244, 57, 64));
        myPaint.setStrokeWidth(10);
        myPaint.setStyle(Paint.Style.FILL);

        myPaints.add(myPaint);

        //teal
        myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 150, 136));
        myPaint.setStrokeWidth(10);
        myPaint.setStyle(Paint.Style.FILL);

        myPaints.add(myPaint);

        //green
        myPaint = new Paint();
        myPaint.setColor(Color.rgb(135, 195, 74));
        myPaint.setStrokeWidth(10);
        myPaint.setStyle(Paint.Style.FILL);

        myPaints.add(myPaint);

        //yellow
        myPaint = new Paint();
        myPaint.setColor(Color.rgb(251, 190, 7));
        myPaint.setStrokeWidth(10);
        myPaint.setStyle(Paint.Style.FILL);

        myPaints.add(myPaint);
        invalidate();

    }

    /**
     * Default constructor
     *
     * @param ct {@link android.content.Context}
     */
    public CirclesDrawingView(final Context ct) {
        super(ct);

        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);

        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);

        init(ct);
    }

    public void init(final Context ct) {
        // Generate bitmap used for background
        mBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);

        mCirclePaint = new Paint();

        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(final Canvas canv) {

        Log.d("PAVAN", "View invalidated");

        int height = CompatibleCanvasActivity.userHeight;
        int width = CompatibleCanvasActivity.userWidth;

        getParent().requestDisallowInterceptTouchEvent(true);
        canv.drawBitmap(mBitmap, null, mMeasuredRect, null);

        //int cellWidth = canv.getWidth()/noOfCells;

        int cellWidth = canv.getWidth()/width;
        int cellHeight = canv.getHeight()/height;

        Paint blackPaint = new Paint();
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        for (CircleArea circle : mCircles) {
            canv.drawCircle(circle.centerX, circle.centerY, circle.radius, mCirclePaint);
        }

        if (mRectangles != null && mRectangles.size() == 4) {
            for (int i = 0; i < 4; i++) {
                canv.drawRect(mRectangles.get(i), myPaints.get(i));

                for (int j = 1; j < width; j++) {
                    canv.drawLine(j * cellWidth, 0, j * cellWidth, canv.getHeight(), blackPaint);
                }

                for (int j = 1; j < height; j++) {
                    canv.drawLine(0, j * cellHeight, canv.getWidth(), j * cellHeight, blackPaint);
                }

                TextBlock textBlock = textBlocks.get(i);
                Log.d("BLOCK", "textBlock "+i+" "+textBlock.getText()+" "+textBlock.getPointX()+" "+textBlock.getPointY()+" "+textBlock.getPaint());
                canv.drawText(textBlock.getText(), textBlock.getPointX(), textBlock.getPointY(), textBlock.getPaint());

            }
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;

        CircleArea touchedCircle;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside some circle
                touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                touchedCircle.centerX = xTouch;
                touchedCircle.centerY = yTouch;
                mCirclePointer.put(event.getPointerId(0), touchedCircle);

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside some circle
                touchedCircle = obtainTouchedCircle(xTouch, yTouch);

                mCirclePointer.put(pointerId, touchedCircle);
                touchedCircle.centerX = xTouch;
                touchedCircle.centerY = yTouch;
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                Log.w(TAG, "Move");

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedCircle = mCirclePointer.get(pointerId);

                    if (null != touchedCircle) {
                        touchedCircle.centerX = xTouch;
                        touchedCircle.centerY = yTouch;
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                clearCirclePointer();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex);

                mCirclePointer.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    /**
     * Clears all CircleArea - pointer id relations
     */
    private void clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer");

        mCirclePointer.clear();
    }

    /**
     * Search and creates new (if needed) circle based on touch area
     *
     * @param xTouch int x of touch
     * @param yTouch int y of touch
     * @return obtained {@link CircleArea}
     */
    private CircleArea obtainTouchedCircle(final int xTouch, final int yTouch) {
        CircleArea touchedCircle = getTouchedCircle(xTouch, yTouch);

        if (null == touchedCircle) {
            touchedCircle = new CircleArea(xTouch, yTouch, mRadiusGenerator.nextInt(RADIUS_LIMIT) + RADIUS_LIMIT);

            if (mCircles.size() == CIRCLES_LIMIT) {
                Log.w(TAG, "Clear all circles, size is " + mCircles.size());
                // remove first circle
                mCircles.clear();
            }

            Log.w(TAG, "Added circle " + touchedCircle);
            mCircles.add(touchedCircle);
        }

        return touchedCircle;
    }

    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     * @return {@link CircleArea} touched circle or null if no circle has been touched
     */
    private CircleArea getTouchedCircle(final int xTouch, final int yTouch) {
        CircleArea touched = null;

        for (CircleArea circle : mCircles) {
            if ((circle.centerX - xTouch) * (circle.centerX - xTouch) + (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius * circle.radius) {
                touched = circle;
                break;
            }
        }

        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredWidth());
    }
}