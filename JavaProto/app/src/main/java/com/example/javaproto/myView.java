package com.example.javaproto;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

    public class myView extends View {
    Paint paint1,paintx;
    int a=0;
    int b=0;
    boolean end=false;
    boolean ifStart=true;
    Character jerry,tom;
    float[] widths = new float[4];
    ArrayList<Obstacles> obsList = new ArrayList<>();
    int collision=0;
    boolean immFram=true;
    boolean cutscene = true;
    boolean startCutscene=false;
    Context context;
    Bitmap mouse = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.frame_3);
    Bitmap cat = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.frame_4);
    Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.group_2);

    float score=0f;


    public myView(Context context) {
        super(context);
        obsList.clear();
        paint1 = new Paint();
        paint1.setColor(Color.parseColor("#f9eee0"));
        paint1.setStrokeWidth(10);
        paintx = new Paint();
        this.context=context;
    }
    public void vibrate(int a){
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(a, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(a);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float width = widths[1]*2;
        if(0<=event.getX()&&event.getX()<width/2){
            a--;
            if(a>-2){
                jerry.offset(-width*5/16,0);
                vibrate(100);
            }
            else{
                a++;
            }
        }
        else{
            a++;
            if(a<2){
                jerry.offset(width*5/16,0);
                vibrate(100);
            }
            else{
                a--;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        float width = canvas.getWidth();
        widths[1]=width/2;
        widths[0]=width*3/16;
        widths[2]=width*13/16;
        widths[3]=canvas.getHeight();
        float newWidth = width/4;
        float smallWidth = newWidth/4;
        canvas.drawColor(Color.parseColor("#e5dccd"));
        canvas.drawRect(smallWidth,0,newWidth+smallWidth,getHeight(),paint1);
        canvas.drawRect(newWidth+smallWidth*2,0,newWidth*2+smallWidth*2,getHeight(),paint1);
        canvas.drawRect(newWidth*2+smallWidth*3,0,newWidth*3+smallWidth*3,getHeight(),paint1);
        if(ifStart){
            ifStart=!ifStart;
            addObstacle();
//            jerry = new Character(width/2-smallWidth,getHeight()*0.7f-smallWidth,width/2+smallWidth,getHeight()*0.7f+smallWidth);
            jerry = new Character(width/2-smallWidth,getHeight(),width/2+smallWidth,getHeight()+2*smallWidth);
            tom = new Character(width/2-width/8,getHeight()*1.2f,width/2+width/8,getHeight()*1.2f+width/4);
        }
        Paint paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setStrokeWidth(2);
        canvas.drawBitmap(mouse,null,jerry,jerry.paint3);
        if(collision>0||cutscene||!startCutscene){
            canvas.drawBitmap(cat,null,tom,tom.paint3);
        }
        Iterator<Obstacles> iterator21 = obsList.iterator();
        while (iterator21.hasNext()) {
            Obstacles b = iterator21.next();
            canvas.drawBitmap(icon,null,b.draw(canvas),b.paint);
        }
        Integer txtscore = (int) score;
        Paint textpaint = new Paint();
        textpaint.isAntiAlias();
        textpaint.setTextSize(70);
        textpaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth()*0.75f,90f,83f,textpaint);
        canvas.drawCircle(getWidth()*0.95f,90f,83f,textpaint);
        canvas.drawRect(getWidth()*0.75f,7f,getWidth()*0.95f,173f,textpaint);
        textpaint.setColor(Color.DKGRAY);
        canvas.drawCircle(getWidth()*0.75f,90f,70f,textpaint);
        canvas.drawCircle(getWidth()*0.95f,90f,70f,textpaint);
        canvas.drawRect(getWidth()*0.75f,20f,getWidth()*0.95f,160f,textpaint);
        textpaint.setColor(Color.WHITE);
        canvas.drawText("Score: "+txtscore.toString(),getWidth()*0.745f,120f,textpaint);
        super.onDraw(canvas);
    }

    public void update() {
        if(!startCutscene){
            tom.offset(0f,-6f);
            jerry.offset(0f,-6f);
            if(jerry.centerY()<=getHeight()*0.7f){
                startCutscene=true;
                addObstacle();
            }
        }
        else if(cutscene){

            tom.offset(0f,2f);
            if(tom.top>getHeight()){
                cutscene=false;
            }
        }
        score+=0.1f;
        Iterator<Obstacles> iterator1 = obsList.iterator();
        while(iterator1.hasNext()){
            iterator1.next().updatePosition();
        }
        Iterator<Obstacles> iterator = obsList.iterator();
        while(iterator.hasNext()){
            if(iterator.next().isOut()){
                iterator.remove();
                tomMove();
            }
        }
        jerryIntersects();
        tomMove();
        if(collision>1||score>200){
            if(score<200){
                Toast toast = Toast.makeText(getContext(),"GAME END",Toast.LENGTH_LONG);
                toast.show();
                tom.offset(jerry.centerX()-tom.centerX(),-50);
                invalidate();
            }
            else{
                Toast toast = Toast.makeText(getContext(),"YOU WON",Toast.LENGTH_LONG);
                toast.show();
            }
            end=true;
        }
        if(jerry.getColDur()>0) {
            jerry.setColDur(jerry.getColDur() - 1);
            if(jerry.getColDur()%10==0){
                if (immFram) {
                    jerry.paint3.setAlpha(150);
                }
                else{
                    jerry.paint3.setAlpha(255);
                }
                immFram=!immFram;
            }
        }
        else{
            jerry.paint3.setAlpha(255);
            immFram=true;
        }
        if(collision>0 && tom.top>jerry.bottom+20){
            tom.offset(0,-5f);
        }
        invalidate();
    }
    public void addObstacle(){
        Random rand = new Random();
        int x = rand.nextInt(1)+1;
        for(int i=0;i<x;i++) {
            Obstacles ab = new Obstacles(widths[1], 120);
            int randInt = rand.nextInt(5) % 3;
            if (randInt == 1) {
                ab.setPosition(widths[0],-1);
            } else if (randInt == 2) {
                ab.setPosition(widths[1],0);
            } else {
                ab.setPosition(widths[2],1);
            }
            obsList.add(ab);
        }
    }

    public void tomMove(){
        boolean move = false;
        boolean right = b != 1;
        boolean left = b != -1;
        Iterator<Obstacles> iteratorx = obsList.iterator();
        while(iteratorx.hasNext()){
            Obstacles ab = iteratorx.next();
            if(ab.centerX()==tom.centerX()&& ab.bottom>tom.top*0.8f && ab.top<tom.bottom){
                move=true;
            }
        }
        Iterator<Obstacles> iteratorx1 = obsList.iterator();
        if(move){
            while(iteratorx1.hasNext()){
                Obstacles ab = iteratorx1.next();

                if(ab.lane==b-1 && ab.bottom>tom.top && ab.top<tom.bottom){
                    left=false;
                }
                if(ab.lane==b+1 && ab.bottom>tom.top && ab.top<tom.bottom){
                    right=false;
                }
            }
        }
        else{
            right=false;
            left=false;
        }
        if(left){
            b--;
            if(b>-2){
                tom.offset(-widths[1]*5/8,0);
            }
            else{
                b++;
            }
        }
        else if(right){
            b++;
            if(b<2){
                tom.offset(widths[1]*5/8,0);
            }
            else{
                b--;
            }
        }
    }

    public void jerryIntersects(){
        Iterator<Obstacles> iterator2 = obsList.iterator();
        while(iterator2.hasNext()&& jerry.getColDur()==0){
            if(iterator2.next().intersect(jerry)){
                if(collision<1){
                    final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.wrong);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    mp.start();
                    vibrate(800);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                iterator2.remove();
                jerry.setColDur(100);
                cutscene=false;
                collision++;
            }
        }
    }


    public ArrayList<Obstacles> getObsList() {
        return obsList;
    }


}
