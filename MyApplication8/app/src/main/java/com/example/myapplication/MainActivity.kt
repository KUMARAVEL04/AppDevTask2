package com.example.myapplication

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private val FPS = (1000 / 30).toLong()
    private var ifPause = false
    var infinite: Boolean =false
    lateinit var canvasView: MyView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        canvasView = MyView(this)
        val backgroundView = BackgroundView(this)
        val frame:FrameLayout = FrameLayout(this)
        var layoutparams: FrameLayout.LayoutParams? = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        )
        frame.layoutParams=layoutparams
        frame.addView(backgroundView)
        frame.addView(canvasView)
        setContentView(frame)
        infinite= intent.getBooleanExtra("Infinite",false)
        hideUI()
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
    override fun onPause() {
        ifPause=true
        val musicIntent = Intent(this, BackgroundMuiscService::class.java)
        musicIntent.action="PAUSE"
        startService(musicIntent)
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            delay(500L)
            updateView(canvasView)
        }
        val musicIntent = Intent(this, BackgroundMuiscService::class.java).apply {
            action="PLAY"
        }
        startService(musicIntent)
        ifPause = false
    }
    private fun updateView(canvasView: MyView) {
        lifecycleScope.launch {
            canvasView.update(infinite)
            val ab: ArrayList<Obstacles> = canvasView.getObsList()
            if (ab.size < 15 && ab.isNotEmpty() && canvasView.startCutscene) {
                if (ab[ab.size - 1].getTop() > canvasView.tom.height() * 1.7f) {
                    canvasView.addObstacle()
                    if ((0..10).random() == 1) {
                        delayedHandler(canvasView)
                    }
                }
            }
            if (!ifPause && !canvasView.end){
                delay(FPS)
                updateView(canvasView)
            }
            else if (canvasView.end) {
                winDialog(canvasView.score.toInt())
            }
        }
    }

    suspend fun delayedHandler(myView: MyView){
        lifecycleScope.launch(Dispatchers.Default) {
            delay(950L)
            withContext(Dispatchers.Main) {
                canvasView.addAdditive()
            }
        }
    }
    fun winDialog(scorx: Int) {
        val winBox = Dialog(this@MainActivity)
        winBox.setContentView(R.layout.win_dialog)
        winBox.setCancelable(false)
        winBox.window!!.setBackgroundDrawable(getDrawable(R.drawable.dialog_bg))
        val score = winBox.window!!.findViewById<TextView>(R.id.score)
        val plyAg = winBox.window!!.findViewById<Button>(R.id.playButton)
        val homeBut = winBox.window!!.findViewById<Button>(R.id.homeButton)
        val textBut = winBox.window!!.findViewById<Button>(R.id.nameField)
        textBut.text = "GAME OVER"
        score.text = "Score: $scorx"
        val options = ActivityOptions.makeCustomAnimation(baseContext, androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out).toBundle()
        plyAg.setOnClickListener { v: View? ->
            winBox.dismiss()
            finish()
            startActivity(intent,options)
        }
        homeBut.setOnClickListener { v: View? ->
            val home = Intent(this@MainActivity, MainActivity2::class.java)
            winBox.dismiss()
            finish()
            startActivity(home,options)
        }
        winBox.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val mp: MediaPlayer = MediaPlayer.create(baseContext, R.raw.sound3)
        mp.setOnCompletionListener { mp -> mp.release() }
        mp.start()
        winBox.show()
    }
}