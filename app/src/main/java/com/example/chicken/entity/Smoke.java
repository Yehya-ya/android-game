package com.example.chicken.entity;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.graphic.Animation;
import com.example.chicken.graphic.Sprite;

public class Smoke {

    private final Animation ani;
    private final Rect bounds;
    private boolean finished;

    public Smoke(Sprite sprite, Vector2f pos, int s) {
        s += 2;
        int w = 16 * s;
        int h = 16 * s;
        int x = (int) (pos.x - w / 2);
        int y = (int) (pos.y - h / 2);
        bounds = new Rect(x, y, x + w, y + h);

        ani = new Animation();
        setAnimation(sprite.getSpriteArray(0), 2);
    }

    private void setAnimation(Bitmap[] spriteArray, int i) {
        ani.setFrame(spriteArray);
        ani.setDelay(i);
    }

    public void update() {
        ani.update();
        this.finished = ani.getCurrentFrame() == ani.getFrameNumber() - 1;
        if (ani.getCurrentFrame() == 3) {
            ani.setDelay(5);
        }
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(ani.getImage(), null, bounds, null);
    }

    public boolean isFinished() {
        return finished;
    }
}
