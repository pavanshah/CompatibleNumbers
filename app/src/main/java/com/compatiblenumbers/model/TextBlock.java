package com.compatiblenumbers.model;

import android.graphics.Paint;

/**
 * Created by avinash on 5/11/17.
 */

public class TextBlock {

    private int pointX;
    private int pointY;
    private String text;
    private Paint paint;


    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
