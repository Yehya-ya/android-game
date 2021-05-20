package com.example.chicken.entity;

import android.graphics.Canvas;

import java.io.IOException;
import java.util.ArrayList;


public class Player extends Entity {

    private final ArrayList<Shoot> shoots;
    private final int power;
    private final int reviveDuration;
    private final Sprite eggSprite;
    private final int shoot_speed;
    private BufferedImage image;
    private BufferedImage icon;
    private Sprite deathSprite;
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
            this.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Image/heart.png"));
        } catch (IOException e) {
            System.out.println("clouds1 Loading....:: " + e);
        }
        this.icon = null;
        try {
            this.icon = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Image/icon.png"));
        } catch (IOException e) {
            System.out.println("clouds1 Loading....:: " + e);
        }

    }

    public ArrayList<Shoot> getShoots() {
        return shoots;
    }

    public void input(KeyHandler keyH, MouseHandler mouseH) {
        up = keyH.up.down;
        down = keyH.down.down;
        left = keyH.left.down;
        right = keyH.right.down;
        attack = keyH.shoot.down;
    }

    public void update() {
        super.update();
        move();
        Attack();

        shoots.forEach((s) -> {
            s.update();
        });
    }

    public void update_death() {
        ani.update();
    }

    public void render(Canvas canvas) {
        if (revive) {
            if (reviveCounter % 15 < 10) {
                g.drawImage(ani.getImage(), bounds.x - bounds.width / 4, bounds.y - bounds.height / 4, (int) (bounds.width * 1.5), (int) (bounds.height * 1.5), null);
            }
            reviveCounter++;
            if (reviveCounter >= reviveDuration) {
                revive = false;
            }
        } else {
            g.drawImage(ani.getImage(), bounds.x - bounds.width / 4, bounds.y - bounds.height / 4, (int) (bounds.width * 1.5), (int) (bounds.height * 1.5), null);
        }
        shoots.forEach((Shoot s) -> {
            s.rander(g);
        });

        for (Shoot s : shoots) {
            if (s.delete()) {
                shoots.remove(s);
                break;
            }
        }
        g.drawImage(icon, 30, 5, 17 * 2, 27 * 2, null);
        for (int i = 1; i <= lives; i++) {
            g.drawImage(image, 60 + 28 * i, 20, 13 * 2, 12 * 2, null);
        }

    }

    public void render_death(Canvas canvas) {
        g.drawImage(ani.getImage(), bounds.x - 16, bounds.y, 32 * 3, 32 * 2, null);
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
        } else if (pos.x + bounds.width >= GamePanel.width) {
            pos.x = GamePanel.width - bounds.width;
        }
        if (pos.y <= 0) {
            pos.y = 0;
        } else if (pos.y + bounds.height >= GamePanel.height) {
            pos.y = GamePanel.height - bounds.height;
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

                    shoots.add(new Shoot(eggSprite, new Vector2f(pos.x + bounds.width / 2, pos.y + bounds.height), new Vector2f((float) deltax, (float) deltay)));
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
            pos.x = GamePanel.width / 2 - bounds.width / 2;
            pos.y = 0;
            dy = maxSpeed;
            dx = 0;
        }
    }

    public void Score(int score) {
        this.score += score;
    }

    public void setDeathAnimation() {
        deathSprite = new Sprite("move/chickendeath.png", 32, 32);
        ani.setFrame(deathSprite.getSpriteArray(0));
        ani.setDelay(10);
        up = false;
        down = false;
        left = false;
        right = false;
    }
}
