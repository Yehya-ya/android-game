package com.example.chicken.gsm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.example.chicken.MainLayout;
import com.example.chicken.entity.Player;
import com.example.chicken.level.LevelStateManager;
import com.example.chicken.level.Type;
import com.example.chicken.ui.Font;
import com.example.chicken.graphic.Sprite;

public class PlayState extends GameState {

    private final Player player;
    private final LevelStateManager lsm;
    private final Sprite enemySprite;
    private final int w, h;
    private GameState p;
    private boolean paused;
    private Bitmap clouds1, clouds2, clouds3;
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
        this.enemySprite = new Sprite("res/drawable/cat.png", 32, 32);
        this.clouds1 = null;
        try {
            this.clouds1 = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/clouds1.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.clouds2 = null;
        try {
            this.clouds2 = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/clouds2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.clouds3 = null;
        try {
            this.clouds3 = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/clouds3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player = new Player(new Sprite("res/drawable/chicken.png", w, h), new Sprite("res/drawable/egg.png", 9, 10), (int) (w * 2 / 1.5), (int) (h * 2 / 1.5));
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
        this.enemySprite = new Sprite("res/drawable/cat.png", 32, 32);
        this.font = font;

        this.clouds1 = null;
        try {
            this.clouds1 = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/clouds1.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.clouds2 = null;
        try {
            this.clouds2 = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/clouds2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.clouds3 = null;
        try {
            this.clouds3 = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/clouds3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player = new Player(new Sprite("res/drawable/chicken.png", w, h), new Sprite("res/drawable/egg.png", 9, 10), (int) (w * 2 / 1.5), (int) (h * 2 / 1.5));
        lsm = new LevelStateManager(enemySprite, player, level, type, font);
    }

    @Override
    public void update() {
        if (!this.finished) {
            if (!paused) {
                player.update();
                lsm.update();
                if (lsm.isFinished()) {
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
                gsm.setLevel();
                gsm.clear();
                gsm.add(State.Menu);
            }
        }
        c1++;
        c2 += 2;
        c3 += 3;
        if (c1 >= MainLayout.height) {
            c1 = 0;
        }
        if (c2 >= MainLayout.height) {
            c2 = 0;
        }
        if (c3 >= MainLayout.height) {
            c3 = 0;
        }
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawBitmap(clouds1, null, new Rect(0, -c1, MainLayout.width, MainLayout.height * 2 - c1), null);
        canvas.drawBitmap(clouds2, null, new Rect(0, -c2, MainLayout.width, MainLayout.height * 2 - c2), null);
        canvas.drawBitmap(clouds3, null, new Rect(0, -c3, MainLayout.width, MainLayout.height * 2 - c3), null);

        if (!this.finished) {
            player.render(canvas);
            lsm.render(canvas);
        } else {
            if (player.isKilled()) {
                canvas.drawColor(Color.argb(255 * counter / (60 * 6), 10, 10, 10));
                player.render_death(canvas);
                font.drawString(canvas, "Game Over", MainLayout.width / 2, MainLayout.height * 2 / 3, 60, counter / 6.0f);
            } else {
                player.render(canvas);
                font.drawString(canvas, "mission completed", MainLayout.width / 2, MainLayout.height / 3 - 100 + counter, 60, -10.0f + 20.0f * counter / (60.0f * 6.0f));
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
