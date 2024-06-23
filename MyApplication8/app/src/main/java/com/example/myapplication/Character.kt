package com.example.myapplication

import android.graphics.Paint
import android.graphics.RectF

class Character(left: Float=0f, top: Float=0f, right: Float=0f, bottom: Float=0f) : RectF(left,top,right,bottom) {
    var colDur: Int = 0
    var isOut: Boolean = false
    var paint3: Paint = Paint()
}