package com.example.chicken.gsm;


import android.graphics.Canvas;

/**
 * @author Yahya-YA
 */
public class PauseState extends GameState {

    private final Button start;
    private final Button Exit;
    private final Button resume;
    private final int width;
    private final int height;
    private final Sprite sprite;
    private final int delay;
    private int foucs;
    private boolean click;
    private int counter;

    public PauseState(GameStatesManager gsm, Font font) {
        super(gsm);
        this.delay = 3;
        this.counter = 0;
        this.foucs = 1;
        this.click = false;
        this.sprite = new Sprite("move/button.png", 190, 49);
        this.width = 900;
        this.height = 100;

        int x = (GamePanel.width / 2 - width / 2);
        int y = (GamePanel.height / 2 - height / 2) + 100;
        int dy = GamePanel.height / 8;

        resume = new Button(sprite, font, new Rectangle(x, y - dy * 2, this.width, this.height), "resume");
        start = new Button(sprite, font, new Rectangle(x, y, this.width, this.height), "Back to Menu");
        Exit = new Button(sprite, font, new Rectangle(x, y + dy * 2, this.width, this.height), "Exit");
    }

    @Override
    public void update() {
        resume.setFoucsed(foucs == 1);
        start.setFoucsed(foucs == 2);
        Exit.setFoucsed(foucs == 3);

        resume.update();
        start.update();
        Exit.update();

        if (this.click) {
            counter++;
            switch (foucs) {
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
                switch (foucs) {
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

    private void nextfoucs() {
        foucs++;
        if (foucs > 3) {
            foucs = 1;
        }
    }

    private void previousfoucs() {
        foucs--;
        if (foucs < 1) {
            foucs = 3;
        }
    }

    @Override
    public void input(KeyHandler keyH, MouseHandler mouseH) {
        if (keyH.down.clicked) {
            this.nextfoucs();
        }
        if (keyH.up.clicked) {
            this.previousfoucs();
        }
        if (keyH.enter.clicked) {
            this.click = true;
        }

        if (resume.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
            this.foucs = 1;
        } else if (start.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
            this.foucs = 2;
        } else if (Exit.contains(mouseH.getMouseX(), mouseH.getMouseY())) {
            this.foucs = 3;
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
        canvas.setColor(new Color(0, 0, 0, 100));
        canvas.fillRect(0, 0, GamePanel.width, GamePanel.height);
        resume.render(g);
        start.render(g);
        Exit.render(g);
    }
}
