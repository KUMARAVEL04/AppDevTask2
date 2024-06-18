package com.example.javaproto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Obstacles extends RectF{
    public float sizx;
    private boolean isOut=false;
    public int lane=0;
    public float speed=15f;
    public Paint paint;
    public Obstacles(float width, float size) {
        this.sizx=size;
        this.top=0;
        this.bottom=this.top+size;
        this.right=width-size/2;
        this.left=width+size/2;
        Paint paint = new Paint();
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
    public void draw(Canvas canvas, Bitmap bitmap){
        RectF rectF1=new RectF(left,top,right,bottom);
        canvas.drawBitmap(bitmap,null,this,paint);
        if(canvas.getHeight()<top){
            isOut=true;
        }
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
