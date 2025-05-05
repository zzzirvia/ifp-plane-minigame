package com.ifp.planegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Explosion {
    private int x, y;
    private boolean active = true;
    private Bitmap bitmap;

    public Explosion(Context context, int x, int y) {
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        this.bitmap = Bitmap.createScaledBitmap(original, 100, 100, false);
        this.x = x - 50;
        this.y = y - 50;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (active) {
            canvas.drawBitmap(bitmap, x, y, paint);
            active = false;
        }
    }
}
