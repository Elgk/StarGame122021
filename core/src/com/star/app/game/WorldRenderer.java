package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gameController;
    private BitmapFont font32;


    public WorldRenderer(SpriteBatch batch, GameController gameController) {
        this.batch = batch;
        this.gameController = gameController;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);

    }
    public void render(){
        ScreenUtils.clear(0.0f, 0.1f, 0.7f,1);
        batch.begin();
        gameController.getBackground().render(batch);
        gameController.getAsteroidController().render(batch);
        gameController.getBulletController().render(batch);
        gameController.getParticleController().render(batch);
        gameController.getPowerUpsController().render(batch);
        gameController.getHero().render(batch);
        gameController.getHero().renderGUI(batch, font32);
        batch.end();
        gameController.getStage().draw();
    }
}
