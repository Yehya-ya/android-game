package com.example.chicken.gsm;


import android.graphics.Canvas;

import java.io.IOException;

/**
 * @author Yahya-YA
 */
public class MenuState extends GameState {

    private final Button start;
    private final Button Exit;
    private final Button resume;
    private final Font font;
    private final int delay;
    private int foucs;
    private BufferedImage image;
    private boolean click;
    private int counter;
    private int mousex, mousey;

    public MenuState(GameStatesManager gsm, Font font) {
        super(gsm);
        this.mousex = GamePanel.width / 2;
        this.mousey = GamePanel.height / 2;
        this.font = font;
        this.click = false;
        this.counter = 0;
        this.delay = 2;
        Sprite sprite = new Sprite("move/button.png", 190, 49);

        int x = (GamePanel.width / 2 - 200);
        int y = (GamePanel.height / 2 - 40);
        int dy = GamePanel.height / 6;

        resume = new Button(sprite, font, new Rectangle(x, y, 400, 80), "resume");
        start = new Button(sprite, font, new Rectangle(x, y + dy, 400, 80), "start");
        Exit = new Button(sprite, font, new Rectangle(x, y + dy * 2, 400, 80), "Exit");

        this.image = null;
        try {
            this.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Image/Img.png"));
        } catch (IOException e) {
            System.out.println("clouds1 Loading....:: " + e);
        }

        if (gsm.IsResumeable()) {
            resume.setClickable(true);
            this.foucs = 1;
        } else {
            resume.setClickable(false);
            this.foucs = 2;
        }
    }

    private void nextfoucs() {
        foucs++;
        if (foucs > 3) {
            foucs = 1;
        }

        if (foucs == 1 && !gsm.IsResumeable()) {
            foucs = 2;
        }
    }

    private void previousfoucs() {
        foucs--;
        if (foucs < 1) {
            foucs = 3;
        }

        if (foucs == 1 && !gsm.IsResumeable()) {
            foucs = 3;
        }
    }

    @Override
    public void update() {
        resume.setFoucsed(foucs == 1);
        start.setFoucsed(foucs == 2);
        Exit.setFoucsed(foucs == 3);

        int x = mousex * 40 / GamePanel.width;
        int y = mousey * 40 / GamePanel.height;
        x -= 20;
        y -= 20;

        resume.setoff(x, y);
        start.setoff(x, y);
        Exit.setoff(x, y);
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
            this.nextfoucs();
        }
        if (keyH.up.clicked) {
            this.previousfoucs();
        }
        if (keyH.enter.clicked) {
            this.click = true;
        }

        this.mousex = mouseH.getMouseX();
        this.mousey = mouseH.getMouseY();

        if (resume.contains(this.mousex, this.mousey)) {
            this.foucs = 1;
        } else if (start.contains(this.mousex, this.mousey)) {
            this.foucs = 2;
        } else if (Exit.contains(this.mousex, this.mousey)) {
            this.foucs = 3;
        }

        if (mouseH.getMouseB() == 1) {
            if (resume.contains(this.mousex, this.mousey)) {
                this.click = true;
            } else if (start.contains(this.mousex, this.mousey)) {
                this.click = true;
            } else if (Exit.contains(this.mousex, this.mousey)) {
                this.click = true;
            }
            mouseH.unclick();
        }
    }

    @Override
    public void render(Canvas canvas) {
        int x = mousex * 40 / GamePanel.width;
        int y = mousey * 40 / GamePanel.height;
        x -= 20;
        y -= 20;

        font.drawString(g, "Cats Invaders", GamePanel.width / 2 + x, GamePanel.height / 5 + y, 120, -20);
        font.drawString(g, "Best Score", GamePanel.width / 5 + x, GamePanel.height * 3 / 5 - 100 + y, 60, -5);
        font.drawString(g, "" + gsm.getScore(), GamePanel.width / 5 + x, GamePanel.height * 3 / 5 + y, 60, -5);
        g.drawImage(image, GamePanel.width * 4 / 6 + x, GamePanel.height / 3 + y, null);

        resume.render(g);
        start.render(g);
        Exit.render(g);
    }

}
