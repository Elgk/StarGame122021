package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {
    private boolean active;
    private Vector2 tempVec;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public Bot(GameController gameController) {
        super(gameController, 30, 200);
        this.texture = Assets.getInstance().getTextureAtlas().findRegion("ship");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0,0);
        this.tempVec = new Vector2(0,0);
        this.hitArea = new Circle(position, BASE_RADIUS * 0.9f);
        this.active = false;
        this.ownerType = OwnerType.BOT;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void activate( float x, float y, int hpIncrease, int enginePowerIncrease){
        this.position.set(x,y);
        this.hpMax += hpIncrease;
        this.enginePower += enginePowerIncrease;
        this.active = true;
    }

    public void update(float dt) {
        super.update(dt);

        if (!isAlive()) {
            deactivate();
        }
        tempVec.set(gameController.getHero().getPosition()).sub(position).nor();
        angle = tempVec.angleDeg(); // направление движения на hero

        if (gameController.getHero().getPosition().dst(position) > 200){
            accelerate(dt);
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
            // определяем позицию сзади корабля
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            // создаются частицы, иммитирующие огонь  // эффект выхлопа
            for (int i = 0; i < 3; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.5f, 0.0f, 0.0f);
            }
        }
        if (gameController.getHero().getPosition().dst(position) < 300){
            tryToFire();
        }
    }


}
