package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    public enum Skill{
        HP_MAX(10, 10),
        HP(20, 10),
        WEAPON(100, 1);

        int cost;
        int power;

        Skill(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }
    private GameController gameController;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hpMax; // время жизни объекта
    private int hp; // текущее время жизни
    private Circle hitArea; // область столкновения
    private StringBuilder sb;
    private Weapon currentWeapon;
    private int money;
    private Shop shop;
    private Weapon[] weapons;
    private int weaponNum;
    private float distanceLimit;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public float getDistanceLimit() {
        return distanceLimit;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public Shop getShop() {
        return shop;
    }

    public int getScore() {
        return score;
    }

    public int getMoney() {
        return money;
    }

    public Hero(GameController gameController) {
        this.gameController = gameController;
        this.texture = Assets.getInstance().getTextureAtlas().findRegion("ship"); // new Texture("ship.png");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0,0);
        this.angle = 0.0f;
        enginePower = 500.0f;
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.money = 50;
        this.hitArea = new Circle(position, BASE_RADIUS * 0.9f);
        this.sb = new StringBuilder();
        this.shop = new Shop(this);
        this.weaponNum = 0;
        createWeapos();
        this.currentWeapon = weapons[weaponNum];
        this.distanceLimit = 100.0f;
//        this.currentWeapon = new Weapon(gameController, this, "Laser", 0.1f, 1, 600.0f, 300,
//                new Vector3[]{
//                        new Vector3(28, 0, 0),
//                        new Vector3(28, 90, 20),
//                        new Vector3(28, -90, -20)
//                });
    }


    public void renderGUI(SpriteBatch batch, BitmapFont font){
        sb.setLength(0);
        sb.append("SCORE ").append(scoreView).append("\n");
        sb.append("VIABILITY ").append(hp).append(" / ").append(hpMax).append("\n");
        sb.append("BULLETS ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("MONEY ").append(money).append("\n");
        font.draw(batch, sb, 20, 700);
    }

    public void damage(int amount){
        hp -= amount;
    }

    public boolean isAlive(){
        return  hp > 0;
    }

    public boolean isMoneyEnough(int value){
        return money >= value;
    }

    public void decreaseMoney(int value){
        money -= value;
    }

    public boolean upgrade(Skill skill){
        switch (skill){
            case HP_MAX:
                hpMax += skill.power;
                return true;
            case HP:
                if (hp + skill.power <= hpMax){
                    hp += skill.power;
                    return true;
                }
                break;
            case WEAPON:
                if (weaponNum < weapons.length -1 ){
                    weaponNum ++;
                    currentWeapon = weapons[weaponNum];
                    return  true;
                }
        }
        return false;
    }

    public void continueGame(){
        gameController.setPause(false);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1,
                angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFire();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            gameController.setPause(true);
            shop.setVisible(true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
            // определяем позицию сзади корабля
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            // создаются частицы, иммитирующие огонь
            for (int i = 0; i < 3; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4),by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f);
            }
        }
        // обратный ход
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;
            // определяем позицию с одного бока
            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            // создаются частицы, иммитирующие огонь
            for (int i = 0; i < 2; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4),by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.5f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f);
            }
            // определяем позицию с другого бока
             bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
             by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            // создаются частицы, иммитирующие огонь
            for (int i = 0; i < 2; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4),by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.5f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f);
            }
        }

        position.mulAdd(velocity, dt);

        // торможение
        float stopKoef = 1.0f - 0.8f * dt;
        if (stopKoef < 0.0f){
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);


        CheckSpaceBorders();
        hitArea.setPosition(position);
    }

    private void updateScore(float dt) {
        if (scoreView < score){
            scoreView += 1000 * dt;
            if (scoreView > score){
                scoreView = score;
            }
        }
    }

    private void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
//            float wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
//            float wy = position.y + MathUtils.sinDeg(angle + 90) * 20;
//
//            gameController.getBulletController().setup(wx, wy,
//                    MathUtils.cosDeg(angle) * 500.0f + velocity.x,
//                    MathUtils.sinDeg(angle) * 500.0f + velocity.y);
//            wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
//            wy = position.y + MathUtils.sinDeg(angle - 90) * 20;
//
//            gameController.getBulletController().setup(wx, wy,
//                    MathUtils.cosDeg(angle) * 500.0f + velocity.x,
//                    MathUtils.sinDeg(angle) * 500.0f + velocity.y);
        }
    }

    private void CheckSpaceBorders() {

        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32){
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            velocity.x *= -0.5f;
        }
        if (position.y < 32){

            velocity.y *= -0.5f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32){
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            velocity.y *= -0.5f;
        }

    }

    public void addScore(int amount) {
        score += amount;
    }

    public void  addHp(int value){
        hp += value;
        if (hp >= hpMax){
            hp = hpMax;
        }

    }

    public void consume(PowerUp up) {
        switch (up.getType()){
            case MEDKIT:
                addHp(up.getPower());
                break;
            case AMMOS:
                currentWeapon.addAmmos(up.getPower());
                break;
            case MONEY:
                money += up.getPower();
                break;
        }
    }

    private void createWeapos(){
        weapons = new Weapon[]{
                new Weapon(gameController, this, "Laser", 0.2f, 1, 300.0f, 300,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0)
                        }),
                new Weapon(gameController, this, "Laser", 0.2f, 1, 600.0f, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(gameController, this, "Laser", 0.1f, 1, 600.0f, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(gameController, this, "Laser", 0.1f, 2, 600.0f, 1000,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0),
                                new Vector3(28, 90, 15),
                                new Vector3(28, -90, -15)
                        }),
                new Weapon(gameController, this, "Laser", 0.1f, 3, 600.0f, 1500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20)
                        })
        };
    }

}
