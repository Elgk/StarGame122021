package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {
    private boolean active;
    private float rotationSpeed;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public Bot(GameController gameController) {
        super(gameController, 30, 200);
        this.texture = Assets.getInstance().getTextureAtlas().findRegion("ship");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0,0);
        this.hitArea = new Circle(position, BASE_RADIUS * 2.9f);
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void activate( float x, float y){
        this.position.set(x,y);
        this.velocity.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
        velocity.nor().scl(30.0f); // величина скорости
        this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        this.active = true;
    }

    public void update(float dt) {
        super.update(dt);
        angle += rotationSpeed *dt;
        if (hitArea.contains(gameController.getHero().getHitArea())){
            tryToFire();
        }
        // эффект выхлопа
        velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
        velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        // определяем позицию сзади корабля
        float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
        float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
        // создаются частицы, иммитирующие огонь
        for (int i = 0; i < 3; i++) {
            gameController.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                    velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                    0.4f, 1.2f, 0.2f,
                    1.0f, 0.1f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.5f, 0.0f);
        }
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
    }


}
