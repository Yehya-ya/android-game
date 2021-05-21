package com.example.chicken.gsm;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.example.chicken.ui.Button;
import com.example.chicken.ui.Font;
import com.example.chicken.graphic.Sprite;

public class PauseState extends GameState {

    private final Button start;
    private final Button Exit;
    private final Button resume;
    private final int width;
    private final int height;
    private final Sprite sprite;
    private final int delay;
    private int focus;
    private boolean click;
    private int counter;

    public PauseState(GameStatesManager gsm, Font font) {
        super(gsm);
        this.delay = 3;
        this.counter = 0;
        this.focus = 1;
        this.click = false;
        this.sprite = new Sprite("move/button.png", 190, 49);
        this.width = 900;
        this.height = 100;

        int x = (GamePanel.width / 2 - width / 2);
        int y = (GamePanel.height / 2 - height / 2) + 100;
        int dy = GamePanel.height / 8;

        resume = new Button(sprite, font, new Rect(x, y - dy * 2, x + this.width, y - dy * 2 + this.height), "resume");
        start = new Button(sprite, font, new Rect(x, y, x + this.width, y + this.height), "Back to Menu");
        Exit = new Button(sprite, font, new Rect(x, y + dy * 2, x + this.width, y + dy * 2 + this.height), "Exit");
    }

    @Override
    public void update() {
        resume.setFocused(focus == 1);
        start.setFocused(focus == 2);
        Exit.setFocused(focus == 3);

        resume.update();
        start.update();
        Exit.update();

        if (this.click) {
            counter++;
            switch (focus) {
                case 1: {
                    resume.setCurrentFrame(counter / delay);
                    break;
                }
                case 2: {
                    start.setCurrentFrame(counter / delay);
                    break;
                }
                case 3: {
                    Exit.setCurrentFrame(counter / delay);
                    break;
                }
            }
            if (counter == 3 * delay) {
                switch (focus) {
                    case 1: {
                        gsm.resumePlayState();
                        break;
                    }
                    case 2: {
                        gsm.setlevel();
                        gsm.clear();
                        gsm.add(State.Menu);
                        break;
                    }
                    case 3: {
                        System.exit(0);
                        break;
                    }
                }
            }
        }
    }

    private void nextFocus() {
        focus++;
        if (focus > 3) {
            focus = 1;
        }
    }

    private void previousFocus() {
        focus--;
        if (focus < 1) {
            focus = 3;
        }
    }

    @Override
    public void input(KeyHandler keyH, MouseHandler mouseH) {
        if (keyH.down.clicked) {
            this.nextFocus();
        }
        if (keyH.up.clicked) {
            this.previousFocus();
        }
        if (keyH.enter.clicked) {
            this.click = true;
        }

        if (resume.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
            this.focus = 1;
        } else if (start.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
            this.focus = 2;
        } else if (Exit.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
            this.focus = 3;
        }

        if (mouseH.getMouseB() == 1) {
            if (resume.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
                this.click = true;
            } else if (start.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
                this.click = true;
            } else if (Exit.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
                this.click = true;
            }
            mouseH.unclick();
        }
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawColor(Color.argb(100, 0, 0, 0));
        resume.render(canvas);
        start.render(canvas);
        Exit.render(canvas);
    }
}
