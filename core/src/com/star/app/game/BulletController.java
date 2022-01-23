package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private TextureRegion textureBullet;
    private GameController gameController;

    @Override
    protected Bullet newObject() {
        return new Bullet(gameController);
    }

    public BulletController(GameController gameController) {
        this.gameController = gameController;
        this.textureBullet = Assets.getInstance().getTextureAtlas().findRegion("bullet"); //new Texture("bullet.png");
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            batch.draw(textureBullet, b.getPosition().x - 16, b.getPosition().y - 16);

        }
    }
    public void setup(Ship owner, float x, float y, float vx, float vy){
        getActiveElement().activate(owner, x, y, vx, vy);
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++){
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
