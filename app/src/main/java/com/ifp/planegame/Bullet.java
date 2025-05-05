package com.ifp.planegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bullet {
    private int x, y, speed = 20;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(new Rect(x - 5, y - 20, x + 5, y), paint);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
