package com.example.javaproto;

import android.app.Dialog;
import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private final long FPS = 1000/30;
    private Runnable run;
    private Boolean ifPause=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        myView canvasView = new myView(this);
        setContentView(canvasView);
        View decor_View = getWindow().getDecorView();

        int ui_Options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decor_View.setSystemUiVisibility(ui_Options);
        handler = new Handler();
        boolean infinite = getIntent().getBooleanExtra("Infinite",false);
        Random random = new Random();
        run = new Runnable() {
            @Override
            public void run() {
                canvasView.update(infinite);
                if(canvasView.collision>1){
                    handler.removeCallbacksAndMessages(null);
                }
                ArrayList<Obstacles> ab = canvasView.getObsList();
                if(ab.size()<15&& !ab.isEmpty() && canvasView.startCutscene){
                    if(ab.get(ab.size()-1).getTop()>canvasView.tom.height()*1.7f) {
                        canvasView.addObstacle();
                        if(random.nextInt(10)==1){
                            delayedHandler(canvasView);
                        }
                    }
                }
                if(!ifPause && !canvasView.end)
                    handler.postDelayed(this, FPS);
                else if (canvasView.end){
                    winDialog((int)canvasView.score);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        ifPause=false;
        handler.postDelayed(run,1000/10);
    }
    public void delayedHandler(myView canvasView){
        Handler handler1 = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                canvasView.addAdditive();
            }
        };
        handler1.postDelayed(run,250);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ifPause=true;
    }
    public void winDialog(Integer scorx){
        Dialog winBox = new Dialog(MainActivity.this);
        winBox.setContentView(R.layout.win_dialog);
        winBox.setCancelable(false);
        winBox.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        TextView score = winBox.getWindow().findViewById(R.id.score);
        Button plyAg= winBox.getWindow().findViewById(R.id.playButton);
        Button homeBut= winBox.getWindow().findViewById(R.id.homeButton);
        Button textBut= winBox.getWindow().findViewById(R.id.nameField);
        textBut.setText("GAME OVER");
        score.setText("Score: " + scorx.toString());
        plyAg.setOnClickListener(v -> {
            winBox.dismiss();
            finish();
            startActivity(getIntent());
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        homeBut.setOnClickListener(v -> {
            Intent home = new Intent(MainActivity.this, MainActivity2.class);
            winBox.dismiss();
            finish();
            startActivity(home);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });
        winBox.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.sound3);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
        winBox.show();
    }
}
