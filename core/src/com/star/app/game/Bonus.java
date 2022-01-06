package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Bonus implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private ScreenManager.BonusType type;
    private Circle hitArea;

    private final float BASE_RADIUS = 75.0f / 2;

    @Override
    public boolean isActive() {
        return active;
    }

    public Bonus( ) {
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.hitArea = new Circle(position, BASE_RADIUS);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public ScreenManager.BonusType getType() {
        return type;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public void deactivate(){
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x <= -40 || position.y <= -40 || position.x >= ScreenManager.SCREEN_WIDTH + 40 || position.y >= ScreenManager.SCREEN_HEIGHT + 40) {
            deactivate();
        }
        hitArea.setPosition(position);
    }

    public void activate(ScreenManager.BonusType type, float x, float y, float vx, float vy){
        this.type = type;
        position.set(x,y);
        velocity.set(vx, vy);
        active = true;
    }
}
