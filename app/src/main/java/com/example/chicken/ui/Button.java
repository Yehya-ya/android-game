package com.example.chicken.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.graphic.Sprite;

public class Button {

    private final Rect r;
    private final String text;
    private final Font font;
    private final Sprite sprite;
    private boolean focused;
    private boolean clickable;
    private int currentFrame;
    private Bitmap img;
    private int x_off;
    private int y_off;

    public Button(Sprite sprite, Font font, Rect r, String text) {
        this.y_off = 0;
        this.x_off = 0;
        this.r = r;
        this.font = font;
        this.text = text;
        this.sprite = sprite;
        clickable = true;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean contains(int X, int Y) {
        if (clickable) {
            return r.contains(X, Y);
        }
        return false;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame += currentFrame;
    }

    public void update() {
        if (this.clickable) {
            if (this.focused) {
                this.currentFrame = 1;
            } else {
                this.currentFrame = 0;
            }
        } else {
            this.currentFrame = 4;
        }
    }

    public void render(Canvas canvas) {
        img = sprite.anImage(currentFrame, 0);
        if (img != null) {
            canvas.drawBitmap(img, null, new Rect(r.left + x_off, r.right + y_off, r.left + r.width(), r.top + r.height()), null);
        }
        font.drawString(canvas, text, r.centerX() + x_off, r.centerY() + y_off, 60, -2);
    }

    public void setOff(int x, int y) {
        this.x_off = x;
        this.y_off = y;
    }
}
