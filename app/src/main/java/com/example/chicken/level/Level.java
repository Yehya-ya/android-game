package com.example.chicken.level;


import android.graphics.Canvas;

import com.example.chicken.MainLayout;
import com.example.chicken.entity.Player;
import com.example.chicken.graphic.Sprite;
import com.example.chicken.ui.Font;

public class Level {

    private final Stage stages;
    private final Font font;
    private final Player player;
    private int enemy_num;
    private int level;
    private Type type;
    private int counter;
    private int intro_counter;
    private boolean intro;
    private boolean finished;
    private boolean next;

    public Level(Sprite enemy_sprite, Player player, Font font) {
        this.font = font;
        this.intro_counter = 0;
        this.intro = true;
        this.type = Type.moveBy;
        this.counter = 0;
        this.next = false;
        this.level = 1;
        this.finished = false;
        this.enemy_num = 24;
        this.stages = new Stage(enemy_sprite, new Sprite("move/coin.png", 17, 18), new Sprite("move/smoke.png", 16, 16));
        this.player = player;

    }

    Level(Sprite enemy_sprite, Player player, int level, Type type, Font font) {
        this.font = font;
        this.intro_counter = 0;
        this.intro = true;
        this.type = type;
        this.counter = 0;
        this.next = false;
        this.level = level;
        this.finished = false;
        this.enemy_num = 18 + level * 6;
        this.stages = new Stage(enemy_sprite, new Sprite("move/coin.png", 17, 18), new Sprite("move/smoke.png", 16, 16));
        this.player = player;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void update() {
        if (intro) {
            intro_counter++;
            if (intro_counter >= 60 * 6) {
                intro_counter = 0;
                intro = false;
                this.stages.setNewStage(enemy_num, this.type, level);
                type = type.next();
            }
        } else {
            stages.update();
            stages.collides(player);
            stages.attack(player);
            next = stages.isFinished();
            if (next) {
                counter++;
                if (counter == 60 * 5) {
                    intro = true;
                    intro_counter = 0;
                    if (type == Type.moveBy) {
                        level++;
                        finished = true;
                        enemy_num += 9;
                    }
                    counter = 0;
                }
            }

        }
    }

    public void render(Canvas canvas) {
        stages.render(canvas);
        if (intro && intro_counter < 60 * 4) {

            font.drawString(canvas, "Wave " + (type.ordinal() + 1), MainLayout.width / 2 - 50 + (int) ((100.0f * intro_counter / (60.0f * 4.0f))), MainLayout.height / 2, 60, 5);
        }
    }

    int getLevel() {
        return level;
    }

    Type getType() {
        return type;
    }
}
