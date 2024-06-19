package com.example.javaproto;

import android.graphics.RectF;

public class Additives extends Obstacles {
    public Integer luck;

    public Additives(float width, float size, Integer luck) {
        super(width, size);
        this.luck = luck;
    }
}
