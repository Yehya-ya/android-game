package com.example.chicken.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.MainLayout;
import com.example.chicken.graphic.Animation;
import com.example.chicken.graphic.Sprite;
import com.example.chicken.service.Vector2f;

public class Shoot {

    private final Vector2f pos;
    private final Animation ani;
    private final Vector2f delta;
    private final Rect bounds;

    public Shoot(Sprite sprite, Vector2f pos, Vector2f delta) {
        this.pos = pos;
        pos.x = pos.x - 16 / 2;
        bounds = new Rect((int) pos.x, (int) pos.y, (int) pos.x + 16, (int) pos.y + 18);
        this.delta = delta;

        ani = new Animation();
        setAnimation(sprite.getSpriteArray(0), 5);
    }

    public Rect getBounds() {
        return bounds;
    }

    private void setAnimation(Bitmap[] spriteArray, int i) {
        ani.setFrame(spriteArray);
        ani.setDelay(i);
    }

    public void update() {
        ani.update();
        bounds.offset((int) pos.x, (int) pos.y);
        move();
    }

    private void move() {
        pos.x += delta.x;
        pos.y += delta.y;
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(ani.getImage(), null, this.bounds, null);
    }

    public boolean delete() {
        return pos.y >= MainLayout.width;
    }
}
