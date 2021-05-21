package com.example.chicken.service;

public class Vector2f {

    public float x, y;

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f plus(Vector2f other) {
        return new Vector2f(x + other.x, y + other.y);
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public void Scale(double d) {
        x *= d;
        y *= d;
    }

    public double distance(Vector2f d) {

        float dx = Math.abs(x - d.x);
        float dy = Math.abs(y - d.y);

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void mag(float m) {
        float Mag = getMag();
        if (Mag != 0) {
            x = x * m / Mag;
            y = y * m / Mag;
        }
    }

    public float getMag() {
        return (float) Math.sqrt(x * x + y * y);
    }
}