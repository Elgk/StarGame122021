package com.star.app;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.screen.ScreenManager;

public class StarGame extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        // this.gameScreen = new GameScreen(batch);
       // setScreen(gameScreen);
        ScreenManager.getInstance().init(this, batch);// инициализируются все экраны
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU); // устанавливается текущий экран -
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
