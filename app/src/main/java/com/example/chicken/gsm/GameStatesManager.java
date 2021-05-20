package com.example.chicken.gsm;


import android.graphics.Canvas;

import java.util.ArrayList;

public class GameStatesManager {

    private final ArrayList<GameState> states;
    private final Font font;
    private int score;
    private int level;
    private Type type;

    public GameStatesManager() {
        this.score = 0;
        this.type = Type.moveBy;
        this.level = 1;
        this.font = new Font("Font/font.png", 20, 20);
        states = new ArrayList<>();

        states.add(new PlayState(this, font));
    }

    public void pop(GameState s) {
        states.remove(s);
    }

    public GameState add(State s) {
        switch (s) {
            case Play: {
                PlayState temp = new PlayState(this, font);
                states.add(temp);
                return temp;
            }
            case Menu: {
                MenuState temp = new MenuState(this, font);
                states.add(temp);
                return temp;
            }
            case Pause: {
                PauseState temp = new PauseState(this, font);
                states.add(temp);
                return temp;
            }
            default:
                return null;
        }
    }

    public void setlevel() {
        for (GameState s : states) {
            if (s.getClass() == PlayState.class) {
                PlayState temp = (PlayState) s;
                level = temp.getLevel();
                type = temp.getType();
                if (temp.getScore() > score) {
                    score = temp.getScore();
                }
            }
        }
        if (level > 3 || (level == 3 && type == Type.row)) {
            level = 1;
            type = Type.moveBy;
        }
    }

    public void update() {
        for (int i = 0; i < states.size(); i++) {
            states.get(i).update();
        }
    }

    public void input(KeyHandler keyH, MouseHandler mouseH) {
        for (int i = 0; i < states.size(); i++) {
            states.get(i).input(keyH, mouseH);
        }
    }

    public void render(Canvas canvas) {
        for (int i = 0; i < states.size(); i++) {
            states.get(i).render(canvas);
        }
    }

    public void resumePlayState() {
        for (GameState s : states) {
            if (s.getClass() == PlayState.class) {
                PlayState temp = (PlayState) s;
                temp.resume();
            }
        }
    }

    public void resumeGame() {
        PlayState temp = new PlayState(this, level, type, font);
        states.add(temp);
    }

    public void clear() {
        states.clear();
    }

    public boolean IsResumeable() {
        return level != 1 || type != Type.moveBy;
    }

    public int getScore() {
        return this.score;
    }
}
