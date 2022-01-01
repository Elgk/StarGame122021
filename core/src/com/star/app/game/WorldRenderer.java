package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gameController;
    private BitmapFont font32;
    private BitmapFont font20;
    private StringBuilder sb;
    private StringBuilder hpHero;

    public WorldRenderer(SpriteBatch batch, GameController gameController) {
        this.batch = batch;
        this.gameController = gameController;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf", BitmapFont.class);
        this.sb = new StringBuilder();
        this.hpHero = new StringBuilder();
    }
    public void render(){
        ScreenUtils.clear(0.0f, 0.1f, 0.7f,1);
        batch.begin();
        gameController.getBackground().render(batch);
        gameController.getAsteroidController().render(batch);
        gameController.getBulletController().render(batch);
        gameController.getHero().render(batch);
        sb.setLength(0);
        hpHero.setLength(0);
        sb.append("SCORE ").append(gameController.getHero().getScoreView());
        hpHero.append("VIABILITY ").append(gameController.getHero().getHp());
        font32.draw(batch, sb, 20, 700);
        font20.draw(batch, hpHero, 20, 670);
        batch.end();
    }
}
