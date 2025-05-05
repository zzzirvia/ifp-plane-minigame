package com.ifp.planegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 10;

    public Enemy(Context context, int x, int y) {
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        this.bitmap = Bitmap.createScaledBitmap(original, 120, 120, false);
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public boolean hasReachedGround(int groundY) {
        return y >= groundY;
    }

    public boolean isHit(Bullet bullet) {
        return bullet.getX() > x && bullet.getX() < x + bitmap.getWidth()
                && bullet.getY() > y && bullet.getY() < y + bitmap.getHeight();
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
