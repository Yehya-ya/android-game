package com.example.chicken.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.MainLayout;
import com.example.chicken.graphic.Sprite;
import com.example.chicken.service.Vector2f;

import java.util.ArrayList;


public class Player extends Entity {

    private final ArrayList<Shoot> shoots;
    private final int power;
    private final int reviveDuration;
    private final Sprite eggSprite;
    private final int shoot_speed;
    private Bitmap image;
    private Bitmap icon;
    private int lives;
    private int score;
    private int reviveCounter;
    private boolean revive;
    private boolean killed;

    public Player(Sprite sprite, Sprite eggSprite, int width, int height) {
        super(sprite, width, height);
        this.eggSprite = eggSprite;
        this.score = 0;
        this.killed = false;
        this.shoot_speed = 10;

        this.revive = false;
        this.reviveCounter = 0;
        this.reviveDuration = 120;
        this.lives = 3;

        this.power = 1;
        this.attackSpeed = 10;
        this.attackDuration = 0;

        shoots = new ArrayList<>();
        this.image = null;
        try {
            this.image = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/heart.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.icon = null;
        try {
            this.icon = BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("res/drawable/icon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Shoot> getShoots() {
        return shoots;
    }

    public void update() {
        super.update();
        move();
        Attack();

        for (Shoot shoot : shoots) {
            shoot.update();
        }
    }

    public void update_death() {
        ani.update();
    }

    public void render(final Canvas canvas) {
        if (revive) {
            if (reviveCounter % 15 < 10) {
                canvas.drawBitmap(ani.getImage(), null, bounds, null);
            }
            reviveCounter++;
            if (reviveCounter >= reviveDuration) {
                revive = false;
            }
        } else {
            canvas.drawBitmap(ani.getImage(), null, bounds, null);
        }
        for (Shoot shoot : shoots) {
            shoot.render(canvas);
        }

        for (Shoot s : shoots) {
            if (s.delete()) {
                shoots.remove(s);
                break;
            }
        }
        canvas.drawBitmap(icon, null, new Rect(30, 5, 30 + 17 * 2, 5 + 27 * 2), null);
        for (int i = 1; i <= lives; i++) {
            canvas.drawBitmap(image, null, new Rect(60 + 28 * i, 20, (60 + 28 * i) + 13 * 2, 20 + 12 * 2), null);
        }

    }

    public void render_death(Canvas canvas) {
        canvas.drawBitmap(ani.getImage(), null, bounds, null);
    }

    public boolean isKilled() {
        return killed;
    }

    public int getScore() {
        return score;
    }

    private void move() {
        if (up) {
            dy -= acc;
            if (dy < -maxSpeed) {
                dy = -maxSpeed;
            }
        } else {
            if (dy < 0) {
                dy += de_acc;
                if (dy > 0) {
                    dy = 0;
                }
            }
        }
        if (down) {
            dy += acc;
            if (dy > maxSpeed) {
                dy = maxSpeed;
            }
        } else {
            if (dy > 0) {
                dy -= de_acc;
                if (dy < 0) {
                    dy = 0;
                }
            }
        }
        if (left) {
            dx -= acc;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else {
            if (dx < 0) {
                dx += de_acc;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }
        if (right) {
            dx += acc;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if (dx > 0) {
                dx -= de_acc;
                if (dx < 0) {
                    dx = 0;
                }
            }
        }
        pos.x += dx;
        pos.y += dy;

        if (pos.x <= 0) {
            pos.x = 0;
        } else if (bounds.right >= MainLayout.width) {
            pos.x = MainLayout.width - bounds.width();
        }
        if (pos.y <= 0) {
            pos.y = 0;
        } else if (bounds.bottom >= MainLayout.height) {
            pos.y = MainLayout.height - bounds.height();
        }
    }

    private void Attack() {
        if (attack) {
            if (attackDuration == 0) {
                double deltaAngle = ((double) 30 / (power + 1));

                for (int i = 0; i < power; i++) {
                    double angle = 105 - deltaAngle * (i + 1);
                    angle = Math.toRadians(angle);
                    double deltax = Math.cos(angle) * shoot_speed;
                    double deltay = Math.sin(angle) * shoot_speed;

                    shoots.add(new Shoot(eggSprite, new Vector2f(bounds.centerX(), bounds.bottom), new Vector2f((float) deltax, (float) deltay)));
                }
            }
            attackDuration++;
            if (attackDuration >= attackSpeed) {
                attackDuration = 0;
            }
        } else {
            attackDuration = 0;
        }
    }

    public void Kill() {
        if (!revive) {
            revive = true;
            reviveCounter = 0;
            lives--;
            if (lives <= 0) {
                this.killed = true;
            }
            pos.x = MainLayout.width / 2.0f - bounds.centerX();
            pos.y = 0;
            dy = maxSpeed;
            dx = 0;
        }
    }

    public void Score(int score) {
        this.score += score;
    }

    public void setDeathAnimation() {
        Sprite deathSprite = new Sprite("res/drawable/chickendeath.png", 32, 32);
        ani.setFrame(deathSprite.getSpriteArray(0));
        ani.setDelay(10);
        up = false;
        down = false;
        left = false;
        right = false;
    }
}
