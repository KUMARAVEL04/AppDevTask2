package com.example.javaproto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

public class Obstacles extends RectF{
    public float sizx;
    private boolean isOut=false;
    public int lane=0;
    public static float speed=15f;
    public Paint paint;
    public ArrayList<Integer> solution = new ArrayList<>();
    public Obstacles(float width, float size) {
        this.sizx=size;
        this.top=0;
        this.bottom=this.top+size;
        this.right=width-size/2;
        this.left=width+size/2;
    }

    public float getTop() {
        return top;
    }


    public void setPosition(float dim,int lane) {
        this.left = dim-sizx/2;
        this.right = dim+sizx/2;
        this.lane = lane;
    }

    public boolean isOut() {
        return isOut;
    }
    public RectF draw(Canvas canvas){
        if(canvas.getHeight()<top){
            isOut=true;
        }
        return this;
    }
    public void updatePosition(){
        this.top+=speed;
        this.bottom+=speed;
    }

}
