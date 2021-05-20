package com.example.chicken.entity;


import android.graphics.Canvas;
import android.graphics.Rect;

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
        bounds = new Rect(x, y, w, h);

        ani = new Animation();
        setAnimation(sprite.getSpriteArray(0), 2);
    }

    private void setAnimation(BufferedImage[] spriteArray, int i) {
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
        g.drawImage(ani.getImage(), bounds.x, bounds.y, bounds.width, bounds.height, null);
    }

    public boolean isFinished() {
        return finished;
    }
}
