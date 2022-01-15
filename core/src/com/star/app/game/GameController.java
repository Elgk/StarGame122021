package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.GameOverScreen;
import com.star.app.screen.ScreenManager;

public class GameController {

    private Hero hero;
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private Vector2 tempVec;
    private boolean pause;
    private PowerUpsController powerUpsController;

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.particleController = new ParticleController();
        this.tempVec = new Vector2();
        this.powerUpsController = new PowerUpsController(this);
        this.pause = false;

        for (int i = 0; i < 2; i++) {
            asteroidController.setup(MathUtils.random( 0, ScreenManager.SCREEN_WIDTH - 200),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT-100),
                    MathUtils.random(-100, 100),
                    MathUtils.random(-100, 100), 1.0f);
        }
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

    public ParticleController getParticleController() {
        return particleController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public void update(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            pause = !pause;
        }
        if (!pause){
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
            background.update(dt);
            hero.update(dt);
            asteroidController.update(dt);
            bulletController.update(dt);
            particleController.update(dt);
            powerUpsController.update(dt);
            checkCollisions();
        }

    }

    private void checkCollisions() {

        for (int j = 0; j < asteroidController.getActiveList().size(); j++){
            Asteroid ad = asteroidController.getActiveList().get(j);
            if (ad.getHitArea().overlaps(hero.getHitArea())){
                float dst = ad.getPosition().dst(hero.getPosition());
                float halfOverLen = (ad.getHitArea().radius + hero.getHitArea().radius - dst)/ 2; // вычисление половины расстояния перекрытия радиусов при столкновении
                tempVec.set(hero.getPosition()).sub(ad.getPosition()).nor(); // вычисляется разность двух векторов и нормализуется (приводится к единице)
                // оба объекта отталкиваются друг от друга в противоположных направлениях
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                ad.getPosition().mulAdd(tempVec, -halfOverLen);
                // определяется скорость разлетания в зависимости от размера ( массы) объекта
                float sumScl = hero.getHitArea().radius + ad.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, ad.getHitArea().radius / sumScl * 100);
                ad.getVelocity().mulAdd(tempVec, hero.getHitArea().radius / sumScl + 100);

                if (ad.takeDamage(2)){
                    hero.addScore(ad.getHpMax() * 50);
                }
                hero.damage(2);
                if (hero.getHp() <= 0){
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER);
                }
            }
        }


        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
           Bullet bullet = bulletController.getActiveList().get(i);
           for (int j = 0; j < asteroidController.getActiveList().size(); j++){
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(bullet.getPosition())) {

                       particleController.setup(bullet.getPosition().x + MathUtils.random(-4, 4), bullet.getPosition().y + MathUtils.random(-4, 4),
                                bullet.getVelocity().x * -0.3f + MathUtils.random(-30, 30), bullet.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                                0.3f, 2.2f, 1.2f,
                                1.0f, 1.0f, 1.0f, 1.0f,
                                0.0f, 0.0f, 1.0f, 0.0f);

                    bullet.deactivate();
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp up = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(up.getPosition())){
                up.deactivate();
                particleController.getEffectBuilder().takePowerUpEffect(up.getPosition().x, up.getPosition().y);
                hero.consume(up);
            }
        }

    }
}
