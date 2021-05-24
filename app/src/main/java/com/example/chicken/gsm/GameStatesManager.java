package com.example.chicken.gsm;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.MainLayout;
import com.example.chicken.level.Type;
import com.example.chicken.ui.Font;

import java.util.ArrayList;

public class GameStatesManager {

    private final ArrayList<GameState> states;
    private final Font font;
    private int score;
    private int level;
    private Type type;
    private Bitmap background;

    public GameStatesManager() {
        this.score = 0;
        this.type = Type.moveBy;
        this.level = 1;
        this.font = new Font("res/drawable/font.png", 20, 20);
        this.background = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/background.png"));
        this.states = new ArrayList<>();

        this.states.add(new PlayState(this, font));
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

    public void setLevel() {
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

    public void render(Canvas canvas) {
        canvas.drawBitmap(this.background, null, new Rect(0, 0, MainLayout.width, MainLayout.height), null);
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

    public boolean isResumable() {
        return level != 1 || type != Type.moveBy;
    }

    public int getScore() {
        return this.score;
    }
}
