package com.example.chicken.level;

import android.graphics.Canvas;

import com.example.chicken.MainLayout;
import com.example.chicken.entity.Player;
import com.example.chicken.graphic.Sprite;
import com.example.chicken.ui.Font;

public class LevelStateManager {

    private final Level level;
    private final Player player;
    private final Font font;
    private boolean finished;
    private boolean intro;
    private int intro_counter;

    public LevelStateManager(Sprite enemySprite, Player player, Font font) {
        this.player = player;
        this.font = font;
        this.intro_counter = 0;
        this.intro = true;
        this.level = new Level(enemySprite, player, font);
        this.finished = false;
    }

    public LevelStateManager(Sprite enemySprite, Player player, int level, Type type, Font font) {
        this.player = player;
        this.font = font;
        this.intro_counter = 0;
        this.intro = true;
        this.level = new Level(enemySprite, player, level, type, font);
        this.finished = false;
    }

    public boolean isFinished() {
        return finished;
    }

    public void update() {
        if (this.intro) {
            this.intro_counter++;
            if (this.intro_counter > 60 * 3) {
                this.intro_counter = 0;
                this.intro = false;
                this.level.setFinished(false);
            }
        } else {
            if (!this.finished) {
                this.level.update();
                this.finished = player.isKilled();

                if (this.level.isFinished()) {
                    this.intro = true;
                    this.intro_counter = 0;

                    this.finished = this.level.getLevel() > 3;
                }
            }
        }
    }

    public void render(Canvas canvas) {
        if (this.intro) {
            this.font.drawString(canvas, "Level " + level.getLevel(), MainLayout.width / 2 - 100 + (int) (200.0 * (intro_counter / (60.0 * 3.0))), MainLayout.height / 2, 80, 10);
        } else {
            this.level.render(canvas);
        }

        String s = "" + player.getScore();

        String temp = "0";

        for (int i = 1; i < 10 - s.length(); i++) {
            temp += "0";
        }

        font.drawString(canvas, "score:  " + temp + s, MainLayout.width - 200, 20, 20, 0);

    }

    public int getLevel() {
        return this.level.getLevel();
    }

    public Type getType() {
        return this.level.getType();
    }

    public int getScore() {
        return player.getScore();
    }
}
