package com.example.chicken.gsm;

import android.graphics.Canvas;

public class PlayState extends GameState {

    private final Player player;
    private final LevelStateManager lsm;
    private final Sprite enemySprite;
    private final int w, h;
    private GameState p;
    private boolean paused;
    private BufferedImage clouds1, clouds2, clouds3;
    private int c1, c2, c3;
    private int counter;
    private boolean finished;
    private Font font;

    public PlayState(GameStatesManager gsm, Font font) {
        super(gsm);
        this.counter = 0;
        this.c3 = 0;
        this.c2 = 0;
        this.c1 = 0;
        this.h = 40;
        this.w = 63;
        this.finished = false;
        this.paused = false;
        this.font = font;
        this.enemySprite = new Sprite("move/cat.png", 32, 32);
        this.clouds1 = null;
        try {
            this.clouds1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("move/clouds1.png"));
        } catch (IOException e) {
            System.out.println("clouds1 Loading....:: " + e);
        }

        this.clouds2 = null;
        try {
            this.clouds2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("move/clouds2.png"));
        } catch (IOException e) {
            System.out.println("clouds2 Loading....:: " + e);
        }

        this.clouds3 = null;
        try {
            this.clouds3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("move/clouds3.png"));
        } catch (IOException e) {
            System.out.println("clouds3 Loading....:: " + e);
        }

        player = new Player(new Sprite("move/chicken.png", w, h), new Sprite("move/egg.png", 9, 10), (int) (w * 2 / 1.5), (int) (h * 2 / 1.5));
        lsm = new LevelStateManager(enemySprite, player, font);
    }

    public PlayState(GameStatesManager gsm, int level, Type type, Font font) {
        super(gsm);
        this.counter = 0;
        this.c3 = 0;
        this.c2 = 0;
        this.c1 = 0;
        this.h = 40;
        this.w = 63;
        this.finished = false;
        this.paused = false;
        this.enemySprite = new Sprite("move/cat.png", 32, 32);
        this.font = font;

        this.clouds1 = null;
        try {
            this.clouds1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("move/clouds1.png"));
        } catch (IOException e) {
            System.out.println("clouds1 Loading....:: " + e);
        }

        this.clouds2 = null;
        try {
            this.clouds2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("move/clouds2.png"));
        } catch (IOException e) {
            System.out.println("clouds2 Loading....:: " + e);
        }

        this.clouds3 = null;
        try {
            this.clouds3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("move/clouds3.png"));
        } catch (IOException e) {
            System.out.println("clouds3 Loading....:: " + e);
        }

        player = new Player(new Sprite("move/chicken.png", w, h), new Sprite("move/egg.png", 9, 10), (int) (w * 2 / 1.5), (int) (h * 2 / 1.5));
        lsm = new LevelStateManager(enemySprite, player, level, type, font);
    }

    @Override
    public void update() {
        if (!this.finished) {
            if (!paused) {
                player.update();
                lsm.update();
                if (lsm.isFinieshed()) {
                    this.finished = true;
                    player.setDeathAnimation();
                }
            }
        } else {
            if (player.isKilled()) {
                player.update_death();
            } else {
                player.update();
            }
            counter++;
            if (counter > 60 * 6) {
                gsm.setlevel();
                gsm.clear();
                gsm.add(State.Menu);
            }
        }
        c1++;
        c2 += 2;
        c3 += 3;
        if (c1 >= GamePanel.height) {
            c1 = 0;
        }
        if (c2 >= GamePanel.height) {
            c2 = 0;
        }
        if (c3 >= GamePanel.height) {
            c3 = 0;
        }
    }

    @Override
    public void input(KeyHandler keyH, MouseHandler mouseH) {
        if (!paused) {
            player.input(keyH, mouseH);
        }

        if (keyH.escape.clicked) {
            if (paused) {
                gsm.pop(p);
            } else {
                p = gsm.add(State.Pause);
            }
            paused = !paused;
        }
    }

    @Override
    public void render(Canvas canvas) {
        g.drawImage(clouds1, 0, -c1, GamePanel.width, GamePanel.height * 2, null);
        g.drawImage(clouds2, 0, -c2, GamePanel.width, GamePanel.height * 2, null);
        g.drawImage(clouds3, 0, -c3, GamePanel.width, GamePanel.height * 2, null);
        if (!this.finished) {
            player.render(g);
            lsm.render(g);
        } else {
            if (player.isKilled()) {

                g.setColor(new Color(10, 10, 10, 255 * counter / (60 * 6)));
                g.fillRect(0, 0, GamePanel.width, GamePanel.height);
                player.rander_death(g);
                font.drawString(g, "Game Over", GamePanel.width / 2, GamePanel.height * 2 / 3, 60, counter / 6);
            } else {
                player.render(g);
                font.drawString(g, "mission completed", GamePanel.width / 2, GamePanel.height / 3 - 100 + counter, 60, -10.0f + 20.0f * counter / (60.0f * 6.0f));
            }
        }
    }

    public void resume() {
        gsm.pop(p);
        paused = false;
    }

    public int getLevel() {
        return lsm.getLevel();
    }

    public Type getType() {
        return lsm.getType();
    }

    public int getScore() {
        return lsm.getScore();
    }
}
