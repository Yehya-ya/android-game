package com.example.chicken.graphic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Sprite {

    private final int width;
    private final int height;
    private final int sprite_column;
    private final int sprite_rows;
    private Bitmap spriteSheet;
    private Bitmap[][] spriteArray;

    public Sprite(String file, int width, int height) {
        this.spriteSheet = null;
        this.width = width;
        this.height = height;
        this.spriteSheet = loadSprite(file);

        this.sprite_column = spriteSheet.getWidth() / width;
        this.sprite_rows = spriteSheet.getHeight() / height;
        loadSpriteArray();
    }

    public Bitmap[] getSpriteArray(int i) {
        return this.spriteArray[i];
    }

    private Bitmap getSprite(int row, int column) {
        return Bitmap.createBitmap(this.spriteSheet, column * this.width, row * this.height, this.width, this.height);
    }

    public Bitmap anImage(int x, int y) {
        if (x < this.sprite_rows && y < this.sprite_column) {
            return this.spriteArray[x][y];
        } else {
            return null;
        }
    }

    private Bitmap loadSprite(String file) {
        Bitmap sprite = null;
        try {
            sprite = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sprite;
    }

    private void loadSpriteArray() {
        this.spriteArray = new Bitmap[this.sprite_rows][this.sprite_column];

        for (int row = 0; row < this.sprite_rows; row++) {
            for (int column = 0; column < this.sprite_column; column++) {
                this.spriteArray[row][column] = getSprite(row, column);
            }
        }
    }
}
