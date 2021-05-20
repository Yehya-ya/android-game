package com.example.chicken.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.graphic.Animation;
import com.example.chicken.graphic.Sprite;

public class Coin {

    private final Animation ani;
    private final Vector2f acc;
    private final Rect bounds;

    public Coin(Sprite sprite, Vector2f pos, Vector2f delta) {
        int w = (17 * 2) - 4;
        int h = (2 * 18) - 4;
        int x = (int) (pos.x - w / 2);
        int y = (int) (pos.y - h / 2);
        this.bounds = new Rect(x, y, w, h);
        this.acc = delta;

        this.ani = new Animation();
        setAnimation(sprite.getSpriteArray(0), 5);
    }

    public Rect getBouns() {
        return bounds;
    }

    private void setAnimation(Bitmap[] spriteArray, int i) {
        this.ani.setFrame(spriteArray);
        this.ani.setDelay(i);
    }

    public void update() {
        ani.update();
        move();
    }

    private void move() {
        bounds.x += acc.x;
        bounds.y += acc.y;
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(ani.getImage(), null, bounds, null);
    }

    public boolean delete() {
        return bounds.bottom < 0;
    }
}
