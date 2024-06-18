package com.example.javaproto;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Character extends RectF{
    private int colDur=0;
    boolean isOut=false;
    Paint paint3 = new Paint();
    public int getColDur() {
        return colDur;
    }
    public void setColDur(int colDur) {
        this.colDur = colDur;
    }
    public Character(float left, float top, float right, float bottom) {
        super(left, top, right, bottom);
    }

}
