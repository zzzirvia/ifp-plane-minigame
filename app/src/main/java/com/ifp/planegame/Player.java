package com.ifp.planegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
    private Bitmap bitmap;
    private int x, y;
    private int screenWidth, screenHeight;

    public Player(Context context, int screenWidth, int screenHeight) {
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        this.bitmap = Bitmap.createScaledBitmap(original, 150, 150, false);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Centrado horizontal y altura visible desde el principio
        this.x = Math.max(0, screenWidth / 2 - bitmap.getWidth() / 2);
        this.y = Math.min(screenHeight - bitmap.getHeight() - 200, screenHeight / 2);
    }

    public void updatePosition(int dx, int dy) {
        x += dx;
        y += dy;

        // Limitar dentro de la pantalla
        x = Math.max(0, Math.min(x, screenWidth - bitmap.getWidth()));
        y = Math.max(0, Math.min(y, screenHeight - bitmap.getHeight()));
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
