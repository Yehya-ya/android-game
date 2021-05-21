package com.example.chicken.gsm;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.ui.Button;
import com.example.chicken.ui.Font;
import com.example.chicken.graphic.Sprite;

public class MenuState extends GameState {

    private final Button start;
    private final Button Exit;
    private final Button resume;
    private final Font font;
    private final int delay;
    private int focus;
    private Bitmap image;
    private boolean click;
    private int counter;
    private int mouse_x, mouse_y;

    public MenuState(GameStatesManager gsm, Font font) {
        super(gsm);
        this.mouse_x = GamePanel.width / 2;
        this.mouse_y = GamePanel.height / 2;
        this.font = font;
        this.click = false;
        this.counter = 0;
        this.delay = 2;
        Sprite sprite = new Sprite("move/button.png", 190, 49);

        int x = (GamePanel.width / 2 - 200);
        int y = (GamePanel.height / 2 - 40);
        int dy = GamePanel.height / 6;

        resume = new Button(sprite, font, new Rect(x, y, x + 400, y + 80), "resume");
        start = new Button(sprite, font, new Rect(x, y + dy, y + dy + 400, y + 80), "start");
        Exit = new Button(sprite, font, new Rect(x, y + dy * 2, y + dy * 2 + 400, y + 80), "Exit");

        this.image = null;
        try {
            this.image = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/Img.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gsm.IsResumeable()) {
            resume.setClickable(true);
            this.focus = 1;
        } else {
            resume.setClickable(false);
            this.focus = 2;
        }
    }

    private void nextFocus() {
        focus++;
        if (focus > 3) {
            focus = 1;
        }

        if (focus == 1 && !gsm.IsResumeable()) {
            focus = 2;
        }
    }

    private void previousFocus() {
        focus--;
        if (focus < 1) {
            focus = 3;
        }

        if (focus == 1 && !gsm.IsResumeable()) {
            focus = 3;
        }
    }

    @Override
    public void update() {
        resume.setFocused(focus == 1);
        start.setFocused(focus == 2);
        Exit.setFocused(focus == 3);

        int x = mouse_x * 40 / GamePanel.width;
        int y = mouse_y * 40 / GamePanel.height;
        x -= 20;
        y -= 20;

        resume.setOff(x, y);
        start.setOff(x, y);
        Exit.setOff(x, y);
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
                        gsm.pop(this);
                        gsm.resumeGame();
                        break;
                    }
                    case 2: {
                        gsm.add(State.Play);
                        gsm.pop(this);
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

        this.mouse_x = mouseH.getMouseX();
        this.mouse_y = mouseH.getMouseY();

        if (resume.contains(this.mouse_x, this.mouse_y)) {
            this.focus = 1;
        } else if (start.contains(this.mouse_x, this.mouse_y)) {
            this.focus = 2;
        } else if (Exit.contains(this.mouse_x, this.mouse_y)) {
            this.focus = 3;
        }

        if (mouseH.getMouseB() == 1) {
            if (resume.contains(this.mouse_x, this.mouse_y)) {
                this.click = true;
            } else if (start.contains(this.mouse_x, this.mouse_y)) {
                this.click = true;
            } else if (Exit.contains(this.mouse_x, this.mouse_y)) {
                this.click = true;
            }
            mouseH.unclick();
        }
    }

    @Override
    public void render(Canvas canvas) {
        int x = mouse_x * 40 / GamePanel.width;
        int y = mouse_y * 40 / GamePanel.height;
        x -= 20;
        y -= 20;

        font.drawString(canvas, "Cats Invaders", GamePanel.width / 2 + x, GamePanel.height / 5 + y, 120, -20);
        font.drawString(canvas, "Best Score", GamePanel.width / 5 + x, GamePanel.height * 3 / 5 - 100 + y, 60, -5);
        font.drawString(canvas, "" + gsm.getScore(), GamePanel.width / 5 + x, GamePanel.height * 3 / 5 + y, 60, -5);

        int left = GamePanel.width * 4 / 6 + x;
        int top = GamePanel.height / 3 + y;
        canvas.drawBitmap(this.image, null, new Rect(left, top, left + this.image.getWidth(), top + this.image.getHeight()), null);

        resume.render(canvas);
        start.render(canvas);
        Exit.render(canvas);
    }

}
