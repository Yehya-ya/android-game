package com.example.chicken.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Font {

    private Bitmap fontSheet;
    private Bitmap[][] FontArray;

    private int width, height;
    private int sheet_columns, sheet_rows;

    public Font(String file, int width, int height) {
        this.width = width;
        this.height = height;
        this.fontSheet = loadFont(file);

        this.sheet_columns = fontSheet.getWidth() / width;
        this.sheet_rows = fontSheet.getHeight() / height;

        this.loadFontArray();
    }

    private Bitmap getLetter(char letter) {
        int value = letter - ' ' + 0;
        int column = value % this.sheet_columns;
        int row = value / this.sheet_columns;

        return this.FontArray[row][column];
    }

    private Bitmap loadFont(String file) {
        Bitmap sprite = null;
        try {
            sprite = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sprite;
    }

    private void loadFontArray() {
        this.FontArray = new Bitmap[this.sheet_rows][this.sheet_columns];

        for (int row = 0; row < this.sheet_rows; row++) {
            for (int column = 0; column < this.sheet_columns; column++) {
                this.FontArray[row][column] = this.setLetter(row, column);
            }
        }
    }

    private Bitmap setLetter(int row, int column) {
        return Bitmap.createBitmap(this.fontSheet, column * this.width, row * this.height, this.width, this.height);
    }

    public void drawString(Canvas canvas, String text, int pos_x, int pos_y, int size, float offset) {
        int length = text.length();
        pos_x = (int) (pos_x - ((length * (size + offset)) / 2));
        pos_y = pos_y - ((size) / 2);

        for (int i = 0; i < length; i++) {
            char temp = text.charAt(i);
            Bitmap img = getLetter(temp);
            int x = (int) (pos_x + (i * size) + (i * offset));

            canvas.drawBitmap(img, null, new Rect(x, pos_y, x + size, pos_y + size), null);
        }
    }
}
