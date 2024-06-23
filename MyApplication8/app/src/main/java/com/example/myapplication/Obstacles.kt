package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

open class Obstacles(width: Float, size: Float): RectF() {
    companion object{
        var speed=15f
    }
    var sizx: Float = 0f
    private var isOut = false
    var lane: Int = 0
    var paint: Paint? = null
    var solution: ArrayList<Int> = ArrayList()
    init {
        this.sizx = size
        this.top=0f
        this.bottom = this.top + size
        this.right = width - size / 2
        this.left = width + size / 2
    }

    fun getTop(): Float {
        return top
    }
    fun setPosition(dim: Float, lane:Int) {
        this.left = dim - sizx / 2
        this.right = dim + sizx / 2
        this.lane=lane
    }
    fun isOut():Boolean{
        return isOut
    }
    fun draw(canvas: Canvas):RectF{
        if (canvas.height < top) {
            isOut = true
        }
        return this
    }
    fun updatePosition(){
        this.top+=speed
        this.bottom+=speed
    }
}