package com.star.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private float angle;

    public Asteroid() {
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(-100,600);
        this.angle = 10.0f;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - 128, position.y - 128, 128,128,256, 256, 1, 1,
                angle, 0, 0, 256, 256, false, false);
    }

    public void update(float dt){
        angle += 0.1f;
        position.x += MathUtils.cosDeg(angle) * 200.0f * dt;
        position.y += MathUtils.sinDeg(angle) * 200.0f * dt;
        if (position.x > ScreenManager.SCREEN_WIDTH + 128 || position.y > ScreenManager.SCREEN_HEIGHT +128){
            position.x = MathUtils.random(-150, ScreenManager.SCREEN_WIDTH-1000);
            position.y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT-300);
            angle = MathUtils.random(10.0f, 60.0f);
        }
    }
}
