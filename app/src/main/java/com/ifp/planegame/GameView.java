package com.ifp.planegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private boolean isPlaying = false;
    private boolean isGameOver = false;
    private boolean isPaused = false;

    private Player player;
    private Joystick joystick;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();

    private Paint paint = new Paint();
    private SurfaceHolder holder;
    private Context context;
    private int screenWidth, screenHeight;
    private long lastEnemySpawnTime = 0;
    private int score = 0;

    private Bitmap background;

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, false);

        player = new Player(context, screenWidth, screenHeight);
        joystick = new Joystick(screenWidth / 2, screenHeight - 200, 100, 50);
        holder = getHolder();
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (!isPaused && !isGameOver) {
                update();
            }
            draw();
            control();
        }
    }

    private void update() {
        int dx = (int) (joystick.getActuatorX() * 10);
        int dy = (int) (joystick.getActuatorY() * 10);
        player.updatePosition(dx, dy);

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update();
            if (b.getY() < 0) bulletIterator.remove();
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemySpawnTime > 2000) {
            Random random = new Random();
            int enemyX = random.nextInt(screenWidth - 100);
            enemies.add(new Enemy(context, enemyX, -100));
            lastEnemySpawnTime = currentTime;
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();
            e.update();

            if (e.hasReachedGround(screenHeight)) {
                explosions.add(new Explosion(context, e.getX(), e.getY()));
                isGameOver = true;
                return;
            }

            for (Bullet b : bullets) {
                if (e.isHit(b)) {
                    explosions.add(new Explosion(context, e.getX(), e.getY()));
                    score += 10;
                    enemyIterator.remove();
                    break;
                }
            }
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawBitmap(background, 0, 0, null);

            paint.setColor(0xFFFFFFFF);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 50, 80, paint);

            player.draw(canvas, paint);
            joystick.draw(canvas, paint);

            for (Bullet b : bullets) b.draw(canvas, paint);
            for (Enemy e : enemies) e.draw(canvas, paint);
            for (Explosion ex : explosions) ex.draw(canvas, paint);

            if (isGameOver) {
                paint.setTextSize(80);
                paint.setColor(0xFFFF0000);
                canvas.drawText("¡DERROTA!", screenWidth / 2 - 200, screenHeight / 2, paint);
                paint.setTextSize(50);
                canvas.drawText("Toca para reiniciar", screenWidth / 2 - 180, screenHeight / 2 + 80, paint);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            restartGame();
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getY() > screenHeight - 300) {
                    // Joystick
                    float dx = (event.getX() - joystick.centerX) / joystick.baseRadius;
                    float dy = (event.getY() - joystick.centerY) / joystick.baseRadius;
                    joystick.setActuator(dx, dy);
                } else {
                    // Disparo
                    bullets.add(new Bullet(player.getX() + 75, player.getY()));
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getY() > screenHeight - 300) {
                    float dx = (event.getX() - joystick.centerX) / joystick.baseRadius;
                    float dy = (event.getY() - joystick.centerY) / joystick.baseRadius;
                    joystick.setActuator(dx, dy);
                }
                break;

            case MotionEvent.ACTION_UP:
                joystick.setActuator(0, 0);
                if (event.getY() < screenHeight - 300) {
                    isPaused = !isPaused;
                }
                break;
        }

        return true;
    }

    private void restartGame() {
        enemies.clear();
        bullets.clear();
        explosions.clear();
        score = 0;
        isGameOver = false;
        isPaused = false;
    }
}
