package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.databinding.ActivityMain2Binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    var toggle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        this.enableEdgeToEdge()
        hideUI()
        val tick = findViewById<CheckBox>(R.id.checkBox)
        val button = findViewById<Button>(R.id.startButton)
        val volume = findViewById<ImageView>(R.id.volumeButton)
        volume.setOnClickListener{ volumeToggle()  }
        button.setOnClickListener { v: View? ->
            button.isClickable = false
            val infinite = tick.isChecked
            val title = findViewById<ConstraintLayout>(R.id.constraintLayout)
            val title2 = findViewById<ConstraintLayout>(R.id.constraintLayout2)
            val fadeAnimator = ObjectAnimator.ofFloat(title, "alpha", 1f, 0f)
            fadeAnimator.setDuration(1000)
            val fadeAnimatortick = ObjectAnimator.ofFloat(tick, "alpha", 1f, 0f)
            fadeAnimator.setDuration(1000)

            val fadeAnimator3 = ObjectAnimator.ofFloat(title2, "alpha", 1f, 0f)
            fadeAnimator3.setDuration(1000)
            val jerry = findViewById<ImageView>(R.id.jerry)
            val tom = findViewById<ImageView>(R.id.tom)

            val translationY = (-jerry.top - jerry.height).toFloat()
            val translateAnimator1 = ObjectAnimator.ofFloat(jerry, "translationY", 0f, translationY)
            val fadeAnimator1 = ObjectAnimator.ofFloat(jerry, "alpha", 1f, 0f, 1f)
            val translationY1 = (-tom.top - tom.height).toFloat()
            val translateAnimator2 = ObjectAnimator.ofFloat(tom, "translationY", 0f, translationY1)
            val fadeAnimator2 = ObjectAnimator.ofFloat(tom, "alpha", 1f, 0f, 1f)
            translateAnimator2.startDelay = 300
            translateAnimator1.startDelay = 300

            val animatorSet2 = AnimatorSet()
            animatorSet2.playTogether(
                fadeAnimatortick,
                fadeAnimator,
                fadeAnimator1,
                fadeAnimator2,
                fadeAnimator3
            )
            val animatorSet1 = AnimatorSet()
            animatorSet2.playTogether(
                translateAnimator1,
                translateAnimator2,
            )
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(
                animatorSet2,
                animatorSet1
            )
            animatorSet.setDuration(1000)
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    val intent =
                        Intent(
                            this@MainActivity2,
                            MainActivity::class.java
                        )
                    intent.putExtra("Infinite", infinite)
                    val options = ActivityOptions.makeCustomAnimation(baseContext, androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out).toBundle()
                    startActivity(intent,options)
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            animatorSet.start()
        }
    }
    fun hideUI(){
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = window.insetsController

            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }
    fun volumeToggle(){
        if(toggle){
            val musicIntent = Intent(this, BackgroundMuiscService::class.java)
            musicIntent.action="MUTE"
            startService(musicIntent)
        }
        else{
            val musicIntent = Intent(this, BackgroundMuiscService::class.java)
            musicIntent.action="UNMUTE"
            startService(musicIntent)
        }
        toggle=!toggle
    }
    override fun onPause() {
        val musicIntent = Intent(this, BackgroundMuiscService::class.java)
        musicIntent.action="PAUSE"
        startService(musicIntent)
        super.onPause()
    }

    override fun onResume() {
        val musicIntent = Intent(this, BackgroundMuiscService::class.java).apply {
            action="PLAY"
        }
        startService(musicIntent)
        super.onResume()
    }
}