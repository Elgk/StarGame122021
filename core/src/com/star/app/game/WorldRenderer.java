package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gameController;

    public WorldRenderer(SpriteBatch batch, GameController gameController) {
        this.batch = batch;
        this.gameController = gameController;
    }
    public void render(){
        ScreenUtils.clear(0.0f, 0.1f, 0.7f,1);
        batch.begin();
        gameController.getBackground().render(batch);
        gameController.getHero().render(batch);
        gameController.getAsteroidController().render(batch);
        gameController.getBulletController().render(batch);
        batch.end();
    }
}
