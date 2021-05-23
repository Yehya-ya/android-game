package com.example.chicken;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.chicken.gsm.GameStatesManager;

public class MainLayout extends SurfaceView implements Runnable {
    boolean canDraw;
    Thread thread;
    Bitmap image;
    SurfaceHolder surfaceHolder;
    GameStatesManager gsm;
    static public int width;
    static public int height;

    public MainLayout(Context context) {
        super(context);
        surfaceHolder = getHolder();
        image = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
        gsm = new GameStatesManager();
        Canvas canvas = surfaceHolder.lockCanvas();
        width = canvas.getWidth();
        height = canvas.getHeight();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        while (canDraw) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            this.update();
            Canvas canvas = surfaceHolder.lockCanvas();
            this.render(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        gsm.update();
    }

    private void render(Canvas canvas) {
        canvas.drawBitmap(image, null, new Rect(0, 0, getWidth(), getHeight()), null);
        gsm.render(canvas);
    }

    public void Pause() {
        canDraw = false;
        while (thread != null) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void Resume() {
        canDraw = true;
        Canvas canvas = surfaceHolder.lockCanvas();
        width = canvas.getWidth();
        height = canvas.getHeight();
        surfaceHolder.unlockCanvasAndPost(canvas);
        thread = new Thread(this);
        thread.start();
    }
}
