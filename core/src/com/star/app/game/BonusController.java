package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;


public class BonusController extends ObjectPool<Bonus> {
    private TextureRegion texture;

    @Override
    protected Bonus newObject() {
        return new Bonus();
    }


    public BonusController() {

    }

    public void setup(ScreenManager.BonusType type, float x, float y, float vx, float vy){
        switch (type){
            case PHARMACY:
                this.texture = Assets.getInstance().getTextureAtlas().findRegion("pharmacy");
                break;
            case AMMO:
                this.texture = Assets.getInstance().getTextureAtlas().findRegion("ammo");
                break;
            case COIN:
                this.texture = Assets.getInstance().getTextureAtlas().findRegion("coins");
                break;
        }
        getActiveElement().activate(type, x, y, vx, vy);
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++){
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            Bonus b = activeList.get(i);
            batch.draw(texture, b.getPosition().x - b.getHitArea().radius, b.getPosition().y - b.getHitArea().radius);

        }
    }
}
