package com.star.app.game;

public class GameController {
    private Asteroid asteroid;
    private Hero hero;
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;

    public GameController() {
        this.background = new Background(this);
        this.asteroid = new Asteroid();
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController();
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

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public void update(float dt){
        background.update(dt);
        asteroid.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        checkCollisions();
    }

    private void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
           Bullet bullet = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid asd = asteroidController.getActiveList().get(j);
                if (asd.getPosition().dst(bullet.getPosition()) < 130){
                    bullet.deactivate();
                    asteroidController.free(j);
                }
            }
        }

    }
}
