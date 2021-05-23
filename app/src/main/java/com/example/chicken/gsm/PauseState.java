package com.example.chicken.gsm;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.example.chicken.MainLayout;
import com.example.chicken.ui.Button;
import com.example.chicken.ui.Font;
import com.example.chicken.graphic.Sprite;

public class PauseState extends GameState {

    private final Button start;
    private final Button Exit;
    private final Button resume;
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
        Sprite sprite = new Sprite("res/drawable/button.png", 190, 49);
        int width = 900;
        int height = 100;

        int x = (MainLayout.width / 2 - width / 2);
        int y = (MainLayout.height / 2 - height / 2) + 100;
        int dy = MainLayout.height / 8;

        resume = new Button(sprite, font, new Rect(x, y - dy * 2, x + width, y - dy * 2 + height), "resume");
        start = new Button(sprite, font, new Rect(x, y, x + width, y + height), "Back to Menu");
        Exit = new Button(sprite, font, new Rect(x, y + dy * 2, x + width, y + dy * 2 + height), "Exit");
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
                        gsm.setLevel();
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
    public void render(Canvas canvas) {
        canvas.drawColor(Color.argb(100, 0, 0, 0));
        resume.render(canvas);
        start.render(canvas);
        Exit.render(canvas);
    }
}
