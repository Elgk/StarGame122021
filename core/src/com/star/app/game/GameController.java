package com.star.app.game;

public class GameController {
    private Asteroid asteroid;
    private Hero hero;
    private Background background;
    private BulletController bulletController;

    public GameController() {
        this.background = new Background(this);
        this.asteroid = new Asteroid();
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
    }

    public Asteroid getAsteroid() {
        return asteroid;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public void update(float dt){
        background.update(dt);
        asteroid.update(dt);
        hero.update(dt);
        bulletController.update(dt);
    //    checkCollisions();
    }

    private void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveElement();
        }
    }
}
