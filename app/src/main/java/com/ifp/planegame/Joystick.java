package com.ifp.planegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {
    public int centerX, centerY, baseRadius, hatRadius;
    private float actuatorX, actuatorY;

    public Joystick(int centerX, int centerY, int baseRadius, int hatRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.baseRadius = baseRadius;
        this.hatRadius = hatRadius;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.GRAY);
        canvas.drawCircle(centerX, centerY, baseRadius, paint);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(centerX + actuatorX * baseRadius, centerY + actuatorY * baseRadius, hatRadius, paint);
    }

    public void setActuator(float x, float y) {
        actuatorX = x;
        actuatorY = y;
    }

    public float getActuatorX() { return actuatorX; }
    public float getActuatorY() { return actuatorY; }
}
