package com.star.app.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.star.app.StarGame;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;

public class ScreenManager {

    public enum ScreenType{
        GAME,
        MENU,
        GAMEOVER
    }

    public enum BonusType{
        PHARMACY,
        AMMO,
        COIN
    }
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int SCREEN_HALF_HEIGHT = SCREEN_HEIGHT / 2;

    private StarGame game;
    private SpriteBatch batch;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private GameOverScreen gameOverScreen;
    private Screen targetScreen;
    private Viewport viewport;  // класс, отвечающий за прорисовку в соответствии с масштабом экрана:
    // - либо растягивает рисунки на всю область экрана, либо  сохраняет пропорции рисунков в соответствии с размерами экрана, тогда обрезает границы

    private static ScreenManager ourInstance = new ScreenManager(); // инициализация на этапе статической загрузки

    public static ScreenManager getInstance(){ // получение ссылки на единственный экземпляр ScreenManager
        return  ourInstance;
    }

    private ScreenManager() {// приватный конструктор, чтобы класс был синглтоном
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void init(StarGame game, SpriteBatch batch){
        this.game = game;
        this.batch = batch;
        this.gameScreen = new GameScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.menuScreen = new MenuScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }

    public void changeScreen(ScreenType type, Object...args){
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if (screen != null){
            screen.dispose();
        }
        game.setScreen(loadingScreen);
            switch (type){
                case GAME:
                    targetScreen = gameScreen;
                    Assets.getInstance().loadAssets(type);
                    break;
                case MENU:
                    targetScreen = menuScreen;
                    Assets.getInstance().loadAssets(type);
                    break;
                case GAMEOVER:
                    targetScreen = gameOverScreen;
                    gameOverScreen.setDefeatedHero((Hero)args[0]);
                    Assets.getInstance().loadAssets(type);
                    break;
        }

    }

    public void resize(int with, int height){
        viewport.update(with, height);
        viewport.apply();
    }

}
