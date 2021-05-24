package com.example.chicken.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.chicken.MainLayout;
import com.example.chicken.graphic.Animation;
import com.example.chicken.graphic.Sprite;
import com.example.chicken.service.Vector2f;

import static com.example.chicken.entity.Direction.*;

public class Entity {

    protected final Rect bounds;
    private final Sprite sprite;
    protected Vector2f pos;
    protected Animation ani;
    protected int currentAnimation;
    protected boolean up = false;
    protected boolean down = false;
    protected boolean left = false;
    protected boolean right = false;
    protected boolean attack;
    protected int attackSpeed;
    protected int attackDuration;
    protected float dx;
    protected float dy;
    protected float maxSpeed;
    protected float acc;
    protected float de_acc;

    public Entity(Sprite sprite, int width, int height) {
        this.maxSpeed = 8;
        this.sprite = sprite;
        this.pos = new Vector2f(MainLayout.width / 2f - width / 2f, MainLayout.height / 2f - height / 2f);
        this.dy = -4;
        this.dx = 0;
        this.acc = 1;
        this.de_acc = 0f;
        bounds = new Rect((int) pos.x, (int) pos.y, (int) pos.x + width, (int) pos.y + height);

        ani = new Animation();
        setAnimation(DOWN.ordinal(), sprite.getSpriteArray(DOWN.ordinal()), 6);
    }

    public Rect getBounds() {
        return bounds;
    }

    private void setAnimation(int dir, Bitmap[] spriteArray, int i) {
        currentAnimation = dir;
        ani.setFrame(spriteArray);
        ani.setDelay(i);
    }

    public void update() {
        animate();
        ani.update();
        bounds.offsetTo((int) pos.x, (int) pos.y);
    }

    private void animate() {
        if (up) {
            if (currentAnimation != UP.ordinal()) {
                setAnimation(UP.ordinal(), sprite.getSpriteArray(UP.ordinal()), 3);
            }
        } else if (left) {
            if (currentAnimation != LEFT.ordinal()) {
                setAnimation(LEFT.ordinal(), sprite.getSpriteArray(LEFT.ordinal()), 5);
            }
        } else if (right) {
            if (currentAnimation != RIGHT.ordinal()) {
                setAnimation(RIGHT.ordinal(), sprite.getSpriteArray(RIGHT.ordinal()), 5);
            }
        } else if (down) {
            if (currentAnimation != DOWN.ordinal() || ani.getDelay() != 10) {
                setAnimation(DOWN.ordinal(), sprite.getSpriteArray(DOWN.ordinal()), 10);
            }
        } else {
            if (ani.getDelay() != 7) {
                setAnimation(DOWN.ordinal(), sprite.getSpriteArray(DOWN.ordinal()), 7);
            }
        }
    }
}
