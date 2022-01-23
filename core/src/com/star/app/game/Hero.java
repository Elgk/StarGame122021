package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero extends Ship{
    public enum Skill{
        HP_MAX(10, 10),
        HP(20, 10),
        WEAPON(100, 1),
        MAGNET(50, 10);

        int cost;
        int power;

        Skill(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }

    private int score;
    private int scoreView;
    private StringBuilder sb;
    private int money;
    private Shop shop;
    private Circle magneticField;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public Circle getMagneticField() {
        return magneticField;
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
        super(gameController, 100, 500);
        this.texture = Assets.getInstance().getTextureAtlas().findRegion("ship"); // new Texture("ship.png");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0,0);

        this.money = 1500;
        this.hitArea = new Circle(position, BASE_RADIUS * 0.9f);
        this.sb = new StringBuilder();
        this.shop = new Shop(this);
        this.magneticField = new Circle(position, 100.0f);
        this.ownerType = OwnerType.PLAYER;
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        sb.setLength(0);
        sb.append("SCORE ").append(scoreView).append("\n");
        sb.append("VIABILITY ").append(hp).append(" / ").append(hpMax).append("\n");
        sb.append("BULLETS ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("MONEY ").append(money).append("\n");
        sb.append("MAGNET ").append((int) magneticField.radius).append("\n");
        font.draw(batch, sb, 20, 700);
    }

    public boolean isMoneyEnough(int value){
        return money >= value;
    }

    public void decreaseMoney(int value){
        money -= value;
    }

    public boolean upgrade(Skill skill){// наращивает ресурсы через магазин за монеты
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
            case MAGNET:
                if (magneticField.radius <=  ScreenManager.SCREEN_HEIGHT / 2){
                    magneticField.radius += skill.power;
                    return true;
                }
        }
        return false;
    }

    public void continueGame(){
        gameController.setPause(false);
    }

    public void setPause(boolean pause) {
        gameController.setPause(pause);
    }

    public void update(float dt) {
        super.update(dt);
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
            accelerate(dt);

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
            brake(dt);
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
        magneticField.setPosition(position);
    }

    private void updateScore(float dt) {
        if (scoreView < score){
            scoreView += 1000 * dt;
            if (scoreView > score){
                scoreView = score;
            }
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

    public void consume(PowerUp up) {// увеличивает ресурсы за счет поглощения выпадающих предметов-бонусов
        sb.setLength(0);
        switch (up.getType()){
            case MEDKIT:
                int oldHp = hp;
                addHp(up.getPower());
                sb.append("HP + ").append(hp - oldHp);
                gameController.getInfoController().setup(position.x, position.y, Color.GREEN, sb);
                break;
            case AMMOS:
                currentWeapon.addAmmos(up.getPower());
                sb.append("AMMOS + ").append(up.getPower());
                gameController.getInfoController().setup(position.x, position.y, Color.ORANGE, sb);
                break;
            case MONEY:
                money += up.getPower();
                sb.append("MONEY + ").append(up.getPower());
                gameController.getInfoController().setup(position.x, position.y, Color.YELLOW, sb);
                break;
        }
    }

}
