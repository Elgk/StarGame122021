package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Asteroid implements Poolable {

    private Vector2 position;
    private float angle;
    private boolean active;

    public Asteroid() {
        this.position = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate(){
        active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public void activate(float x, float y, float ang){
        position.set(x,y);
        angle = ang;
        active = true;
    }

    public void update(float dt){
        if (active) {
            angle += 0.1f;
            position.x += MathUtils.cosDeg(angle) * 200.0f * dt;
            position.y += MathUtils.sinDeg(angle) * 200.0f * dt;
            if (position.x > ScreenManager.SCREEN_WIDTH + 128 || position.y > ScreenManager.SCREEN_HEIGHT +128){
                position.x = MathUtils.random(-150, ScreenManager.SCREEN_WIDTH-1000);
                position.y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT-500);
                angle = MathUtils.random(10.0f, 60.0f);
            }
        }
    }
}
