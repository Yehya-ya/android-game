package com.example.chicken.entity;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Shoot {

    private final Vector2f pos;
    private final Animation ani;
    private final Vector2f delta;
    private final Rect bounds;

    public Shoot(Sprite sprite, Vector2f pos, Vector2f delta) {
        this.pos = pos;
        pos.x = pos.x - 16 / 2;
        bounds = new Rect((int) pos.x, (int) pos.y, 16, 18);
        this.delta = delta;

        ani = new Animation();
        setAnimation(sprite.getSpriteArray(0), 5);
    }

    public Rect getBouns() {
        return bounds;
    }

    private void setAnimation(BufferedImage[] spriteArray, int i) {
        ani.setFrame(spriteArray);
        ani.setDelay(i);
    }

    public void update() {
        ani.update();
        bounds.x = (int) pos.x;
        bounds.y = (int) pos.y;
        move();
    }

    private void move() {
        pos.x += delta.x;
        pos.y += delta.y;
    }

    public void render(Canvas canvas) {
        g.drawImage(ani.getImage(), bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4, null);
    }

    public boolean delete() {
        return pos.y >= GamePanel.width;
    }
}
