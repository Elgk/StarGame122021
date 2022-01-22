package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.GameOverScreen;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {

    private Hero hero;
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private Vector2 tempVec;
    private boolean pause;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private Stage stage;
    private int gameLevel;
    private float timer;
    private Music music;
    private StringBuilder sb;

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.particleController = new ParticleController();
        this.tempVec = new Vector2();
        this.powerUpsController = new PowerUpsController(this);
        this.infoController = new InfoController();
        this.sb = new StringBuilder();

        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(hero.getShop());

        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();

        this.pause = false;
        this.gameLevel = 1;
        createAsteroid();
        generateBigAsteroids(gameLevel);

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

    public InfoController getInfoController() {
        return infoController;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public float getTimer() {
        return timer;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

    private void createAsteroid(){
        for (int i = 0; i < gameLevel + 2; i++) {
            asteroidController.setup(MathUtils.random( 0, ScreenManager.SCREEN_WIDTH - 200),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT-100),
                    MathUtils.random(-25, 25),
                    MathUtils.random(-25, 25), 1.0f);
        }

    }
    private void generateBigAsteroids(int n) {
        for (int i = 0; i < n; i++) {
            asteroidController.setup(MathUtils.random( 0, ScreenManager.SCREEN_WIDTH - 200),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT-100),
                    MathUtils.random(-25, 25),
                    MathUtils.random(-25, 25), 1.0f);
        }
    }

    public void update(float dt){
        if (pause){
            return;
        }
        timer += dt; // за секуду станет равным 1, т.к. пересчитывается 60 раз в сек
        stage.act(dt);
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        powerUpsController.update(dt);
        infoController.update(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (asteroidController.getActiveList().size() == 0){
            gameLevel ++;
            generateBigAsteroids(gameLevel <=3 ? gameLevel : 3);
            timer = 0.0f;
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
                ad.getVelocity().mulAdd(tempVec, hero.getHitArea().radius / sumScl * 100);

                if (ad.takeDamage(2)){
                    hero.addScore(ad.getHpMax() * 50);
                }
                hero.damage(gameLevel * 2);
                sb.setLength(0);
                sb.append("HP - ").append(gameLevel * 2);
                infoController.setup(hero.getPosition().x, hero.getPosition().y, Color.RED, sb);

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
            if (hero.getMagneticField().contains(up.getPosition())) {
                tempVec.set(hero.getPosition()).sub(up.getPosition()).nor();
                up.getVelocity().mulAdd(tempVec, 100);
            }
            if (hero.getHitArea().contains(up.getPosition())){
                up.deactivate();
                particleController.getEffectBuilder().takePowerUpEffect(up.getPosition().x, up.getPosition().y, up.getType());
                hero.consume(up);
            }
        }

    }
}
