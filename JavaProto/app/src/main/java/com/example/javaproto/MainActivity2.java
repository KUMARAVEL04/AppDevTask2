package com.example.javaproto;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CheckBox tick = findViewById(R.id.checkBox);
        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(v -> {
            button.setClickable(false);
            boolean infinite = tick.isChecked();
            TextView title = findViewById(R.id.textView);
            ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(title, "alpha", 1f, 0f);
            fadeAnimator.setDuration(1000);
            ObjectAnimator fadeAnimatortick = ObjectAnimator.ofFloat(tick, "alpha", 1f, 0f);
            fadeAnimator.setDuration(1000);

            ObjectAnimator fadeAnimator3 = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f);
            fadeAnimator3.setDuration(1000);
            ImageView jerry = findViewById(R.id.jerry);
            ImageView tom = findViewById(R.id.tom);

            float translationY = -jerry.getTop() - jerry.getHeight();
            ObjectAnimator translateAnimator1 = ObjectAnimator.ofFloat(jerry, "translationY", 0f, translationY);
            ObjectAnimator fadeAnimator1 = ObjectAnimator.ofFloat(jerry, "alpha", 1f, 0f, 1f);

            float translationY1 = -tom.getTop() - tom.getHeight();
            ObjectAnimator translateAnimator2 = ObjectAnimator.ofFloat(tom, "translationY", 0f, translationY1);
            ObjectAnimator fadeAnimator2 = ObjectAnimator.ofFloat(tom, "alpha", 1f, 0f, 1f);
            translateAnimator2.setStartDelay(100);
            fadeAnimator2.setStartDelay(100);
            fadeAnimator1.setStartDelay(100);
            translateAnimator1.setStartDelay(100);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(fadeAnimatortick,fadeAnimator,fadeAnimator1, translateAnimator1,fadeAnimator2, translateAnimator2,fadeAnimator3);
            animatorSet.setDuration(1000);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                    intent.putExtra("Infinite",infinite);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                }
            });
            animatorSet.start();
        });
    }

}