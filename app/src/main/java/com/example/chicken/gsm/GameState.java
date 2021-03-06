package com.example.chicken.gsm;


import android.graphics.Canvas;

public abstract class GameState {

    final GameStatesManager gsm;

    public GameState(GameStatesManager newGsm) {
        this.gsm = newGsm;
    }

    abstract void update();

    abstract void render(Canvas canvas);

}
