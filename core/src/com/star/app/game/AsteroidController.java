package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture texture;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public AsteroidController() {
        this.texture = new Texture("asteroid.png");
        getActiveElement().activate(-100.0f,600.0f, 10.0f );
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid asd = activeList.get(i);
            batch.draw(texture, asd.getPosition().x - 128, asd.getPosition().y - 128, 128,128,256, 256, 1, 1,
                    asd.getAngle(), 0, 0, 256, 256, false, false);
        }

    }
    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++){
            activeList.get(i).update(dt);
        }
    }
}
