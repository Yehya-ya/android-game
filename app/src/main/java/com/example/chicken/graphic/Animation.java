package com.example.chicken.graphic;

import android.graphics.Bitmap;

public class Animation {

    private Bitmap[] frames;
    private int currentFrame;
    private int frameNumber;

    private int count;
    private int delay;

    public Animation() {

    }

    public void setFrame(Bitmap[] frames) {
        this.frames = frames;
        this.currentFrame = 0;
        this.frameNumber = frames.length;
        this.count = 0;
    }

    public int getFrameNumber() {
        return this.frameNumber;
    }

    public void update() {
        if (delay == -1) {
            return;
        }
        count++;
        if (count == delay) {
            currentFrame++;
            count = 0;
        }
        if (currentFrame == frameNumber) {
            currentFrame = 0;
        }
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int i) {
        this.delay = i;
    }

    public int getCurrentFrame() {
        return this.currentFrame;
    }

    public void setCurrentFrame(int i) {
        this.currentFrame = i;
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }

}
