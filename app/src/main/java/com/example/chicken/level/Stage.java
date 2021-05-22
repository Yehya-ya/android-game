package com.example.chicken.level;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.chicken.entity.Coin;
import com.example.chicken.entity.Enemy;
import com.example.chicken.entity.Player;
import com.example.chicken.entity.Shoot;
import com.example.chicken.entity.Smoke;
import com.example.chicken.graphic.Sprite;
import com.example.chicken.service.Vector2f;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.random;

class Stage {

    private final ArrayList<Enemy> enemies;
    private final ArrayList<Enemy> intro_enemies;
    private final ArrayList<Enemy> attacked_enemies;
    private final ArrayList<Coin> coins;
    private final ArrayList<Smoke> smokes;
    private final Vector2f acc;
    private final Sprite sprite;
    private final Sprite coinSprite;
    private final Sprite smokeSprite;
    private int enemy_num;
    private Type type;
    private int counter;
    private int c;
    private int loop;
    private int delay;
    private int level;
    private boolean finishAnimation;

    public Stage(Sprite sprite, Sprite coin_sprite, Sprite smoke_sprite) {
        this.smokeSprite = smoke_sprite;
        this.coinSprite = coin_sprite;
        this.sprite = sprite;
        this.acc = new Vector2f();
        this.enemies = new ArrayList<>();
        this.intro_enemies = new ArrayList<>();
        this.attacked_enemies = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.smokes = new ArrayList<>();
    }

    public void setNewStage(int num, Type type, int level) {
        this.enemy_num = num;
        this.type = type;
        this.finishAnimation = false;
        this.delay = 15;
        this.loop = 0;
        this.counter = 0;
        this.c = 45;
        this.acc.x = 1;
        this.acc.y = 0;
        this.level = level;

        enemies.clear();
        intro_enemies.clear();

        switch (type) {
            case row: {
                setRowStage();
                break;
            }
            case column: {
                setColumnStage();
                break;
            }
            case circle: {
                setCircleStage();
                break;
            }
            case moveBy: {
                setMoveByStage();
                break;
            }
            default:
                ;
        }
    }

    private void move() {
        switch (type) {
            case row: {
                c--;
                if (c < 0) {

                    acc.x *= -1;
                    c = 90;
                }
                break;
            }
            case column: {
                c--;
                if (c < 0) {

                    acc.x *= -1;
                    c = 90;
                }
                break;
            }
            case circle: {
                acc.x = 0;
                acc.y = 0;
                break;
            }
            case moveBy: {
                acc.x = 0;
                acc.y = 0;
                break;
            }
            default:
                ;
        }

    }

    public void update() {
        move();

        for (Enemy enemy : this.enemies) {
            enemy.update();
        }

        for (Enemy enemy : this.attacked_enemies) {
            enemy.update();
        }

        if (!this.finishAnimation) {
            switch (this.type) {
                case row: {
                    updateRowStage();
                    break;
                }
                case column: {
                    updateColumnStage();
                    break;
                }
                case circle: {
                    updateCircleStage();
                    break;
                }
                case moveBy: {
                    updateMoveByStage();
                    break;
                }
                default:
                    ;
            }
        }
        for (Enemy enemy : this.enemies) {
            enemy.move(acc);
        }

        if (this.type == Type.circle) {
            for (Enemy enemy : this.enemies) {
                enemy.moveCircle();
            }
        }

        for (Coin coin : this.coins) {
            coin.update();
        }

        for (Smoke smoke : this.smokes) {
            smoke.update();
        }

        for (Smoke smoke : this.smokes) {
            if (smoke.isFinished()) {
                smokes.remove(smoke);
                break;
            }
        }
    }

    public void render(Canvas canvas) {
        for (Enemy enemy : this.enemies) {
            enemy.render(canvas);
        }

        for (Enemy enemy : this.attacked_enemies) {
            enemy.render(canvas);
        }

        for (Coin coin : this.coins) {
            coin.render(canvas);
        }

        for (Smoke smoke : this.smokes) {
            smoke.render(canvas);
        }
    }

    public void collides(Player player) {
        ArrayList<Shoot> shoots = player.getShoots();

        if (this.type == Type.moveBy) {
            for (Enemy e : this.enemies) {
                if (!e.isIntroAnimation()) {
                    this.enemies.remove(e);
                    break;
                }
            }
        }

        for (Enemy enemy : this.attacked_enemies) {

            if (enemy.getBouns().bottom < 0 || enemy.getBouns().top > GamePanel.height) {
                this.attacked_enemies.remove(enemy);
                break;
            } else if (enemy.getBouns().right < 0 || enemy.getBouns().left > GamePanel.width) {
                this.attacked_enemies.remove(enemy);
                break;
            }
        }

        for (Coin coin : this.coins) {
            if (player.getBouns().intersect(coin.getBouns())) {
                this.coins.remove(coin);
                player.Score(100);
                break;
            }
        }

        for (Coin coin : this.coins) {
            if (coin.delete()) {
                this.coins.remove(coin);
                break;
            }
        }

        A:
        for (Shoot shoot : shoots) {
            for (Enemy enemy : this.enemies) {
                if (shoot.getBouns().intersect(enemy.getBouns())) {
                    if (enemy.hit()) {
                        Rect enemy_bouns = enemy.getBouns();
                        if (random() < 0.05 * (enemy.getJ() + 1)) {
                            Coin temp = new Coin(this.coinSprite, new Vector2f(enemy_bouns.centerX(), enemy_bouns.centerY()), new Vector2f(0, -1));
                            this.coins.add(temp);
                        }
                        player.Score(enemy_bouns.width() * 2);
                        Smoke temp_smoke = new Smoke(this.smokeSprite, new Vector2f(enemy_bouns.centerX(), enemy_bouns.centerY()), enemy.getJ());
                        this.smokes.add(temp_smoke);
                        this.enemies.remove(enemy);
                    }
                    enemy.setHit(true);
                    shoots.remove(shoot);
                    break A;
                }
            }
        }

        A:
        for (Shoot shoot : shoots) {
            for (Enemy enemy : this.attacked_enemies) {
                if (shoot.getBouns().intersect(enemy.getBouns())) {
                    if (enemy.hit()) {
                        Rect enemy_bouns = enemy.getBouns();
                        if (random() < 0.05 * (enemy.getJ() + 1)) {
                            Coin temp_coin = new Coin(this.coinSprite, new Vector2f(enemy_bouns.centerX(), enemy_bouns.centerY()), new Vector2f(0, -1));
                            this.coins.add(temp_coin);
                        }
                        Smoke t = new Smoke(this.smokeSprite, new Vector2f(enemy_bouns.centerX(), enemy_bouns.centerY()), enemy.getJ());
                        this.smokes.add(t);
                        player.Score(enemy.getBouns().width() * 2);
                        this.attacked_enemies.remove(enemy);
                    }
                    enemy.setHit(true);
                    shoots.remove(enemy);
                    break A;
                }
            }
        }

        for (Enemy enemy : this.enemies) {
            if (enemy.getBouns().intersect(player.getBouns())) {
                player.Kill();
            }
        }

        for (Enemy enemy : this.attacked_enemies) {
            if (enemy.getBouns().intersect(player.getBouns())) {
                player.Kill();
            }
        }
    }

    private void updateRowStage() {
        for (Enemy enemy : this.intro_enemies) {
            enemy.move(this.acc);
        }

        this.counter++;

        if (this.counter == 20) {
            this.enemies.addAll(this.intro_enemies.subList(loop - 1, loop));
            this.intro_enemies.remove(loop - 1);
            this.loop -= 1;
            this.counter = 0;
            if (this.loop <= 0) {
                this.finishAnimation = true;
                this.intro_enemies.clear();
            }
        }
    }

    private void updateColumnStage() {
        for (Enemy enemy : this.intro_enemies) {
            enemy.move(this.acc);
        }
        this.counter++;

        if (this.counter == 60) {
            this.enemies.addAll(this.intro_enemies.subList(0, 3));
            for (int i = 0; i < 3; i++) {
                this.intro_enemies.remove(0);
            }
            this.loop += 3;
            this.counter = 0;
            if (this.loop >= this.enemy_num) {
                this.finishAnimation = true;
                this.intro_enemies.clear();
            }
        }
    }

    private void updateCircleStage() {
        this.counter++;
        if (this.counter == 20) {
            this.enemies.add(this.intro_enemies.get(0));
            this.intro_enemies.remove(0);
            this.loop++;
            this.counter = 0;
            if (this.loop >= this.enemy_num) {
                this.finishAnimation = true;
                this.intro_enemies.clear();
            }
        }
    }

    private void updateMoveByStage() {
        this.counter++;

        if (this.counter == this.delay) {
            this.enemies.add(this.intro_enemies.get(0));
            this.intro_enemies.remove(0);
            this.loop++;
            this.counter = 0;
            if (this.loop % 3 == 0) {
                this.delay = 60;
            } else {
                this.delay = 15;
            }

            if (this.loop >= this.enemy_num) {
                this.finishAnimation = true;
                this.intro_enemies.clear();
            }
        }
    }

    private void setRowStage() {
        int enemyNumRow = this.enemy_num / 3;
        int distance = (GamePanel.width / (enemyNumRow + 3));

        for (int j = 1; j <= 3; j++) {
            for (int i = 0; i < enemyNumRow; i++) {
                Enemy temp_enemy;
                if (j % 2 == 0) {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(GamePanel.width, GamePanel.height / 2), 64 - 8 * j, this.type, 4 - j, this.level);
                } else {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(0 - 31, GamePanel.height / 2), 64 - 8 * j, this.type, 4 - j, this.level);
                }

                int x = (i + 2) * distance;
                int y = (GamePanel.height - GamePanel.height * j / 6);

                temp_enemy.setDelta(new Vector2f(0, -1));
                temp_enemy.setDestination(new Vector2f(x, y));

                this.intro_enemies.add(temp_enemy);
            }
        }
        this.loop = this.enemy_num;
    }

    private void setColumnStage() {
        int enemyNumRow = this.enemy_num / 3;
        int distance = (GamePanel.width / (enemyNumRow + 3));

        for (int i = 0; i < enemyNumRow; i++) {
            for (int j = 1; j <= 3; j++) {

                Enemy temp_enemy;
                if (i < enemyNumRow / 2) {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(-30, GamePanel.height), 64 - 8 * j, this.type, 4 - j, this.level);
                    temp_enemy.setDelta(new Vector2f(1, 3));
                } else {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(GamePanel.width, GamePanel.height), 64 - 8 * j, this.type, 4 - j, this.level);
                    temp_enemy.setDelta(new Vector2f(-1, 3));

                }
                int x = (i + 2) * distance;
                int y = (GamePanel.height - GamePanel.height * j / 6);

                temp_enemy.setDestination(new Vector2f(x, y));

                this.intro_enemies.add(temp_enemy);
            }
        }
    }

    private void setCircleStage() {
        int enemyNumRow = this.enemy_num / 3 + 4;
        for (int j = 1; j <= 3; j++) {
            enemyNumRow -= 2;
            for (int i = 0; i < enemyNumRow; i++) {
                Enemy temp_enemy;
                if (j % 2 == 0) {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(GamePanel.width / 3, GamePanel.height), 64 - 8 * j, this.type, 4 - j, this.level);
                    temp_enemy.setDelta(new Vector2f(-3, 1));
                } else {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(GamePanel.width * 2 / 3, GamePanel.height), 62 - 8 * j, this.type, 4 - j, this.level);
                    temp_enemy.setDelta(new Vector2f(3, 1));
                }
                float b = 360.0f / (20.0f * enemyNumRow);
                temp_enemy.setDestination(new Vector2f(GamePanel.width / 2, GamePanel.height / 4 + (j * 90)), j, b);

                this.intro_enemies.add(temp_enemy);
            }
        }
    }

    private void setMoveByStage() {
        int enemyNumRow = this.enemy_num / 3;

        for (int j = 0; j < enemyNumRow; j++) {
            Enemy temp_enemy;
            Random random = new Random();
            int randomInteger = GamePanel.height / 4 - 60 + random.nextInt((GamePanel.height * 3 / 4));
            if (random.nextDouble() < 0.5) {
                for (int i = 0; i < 3; i++) {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(GamePanel.width, randomInteger), 64 - 16, this.type, i + 1, this.level);
                    temp_enemy.setDestination(new Vector2f(-40, randomInteger));
                    this.intro_enemies.add(temp_enemy);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    temp_enemy = new Enemy(this.sprite, new Vector2f(-31, randomInteger), 64 - 16, this.type, i + 1, this.level);
                    temp_enemy.setDestination(new Vector2f(GamePanel.width + 10, randomInteger));
                    this.intro_enemies.add(temp_enemy);
                }
            }
        }
    }

    public boolean isFinished() {
        return this.enemies.isEmpty() & this.intro_enemies.isEmpty();
    }

    void attack(Player player) {
        for (Enemy e : this.enemies) {
            if (random() < 0.001 * this.level) {

                e.attack(player.getBouns());
                this.attacked_enemies.add(e);
                this.enemies.remove(e);
                break;
            }
        }
    }
}
