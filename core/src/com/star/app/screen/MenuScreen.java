package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.Background;
import com.star.app.screen.utils.Assets;

public class MenuScreen extends AbstractScreen{
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage; // сцена, управляет компонентами (кнопки и т.п.), уже содержит все компоненты

    public MenuScreen(SpriteBatch batch){
        super(batch);
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        Gdx.input.setInputProcessor(stage);// InputProcessor  - управляет событиями клавиатуры, мышки и др., здесь stage перехватывает все события

        Skin skin = new Skin(); // Skin класс, который настраивает внешний вид элементов
        skin.addRegions(Assets.getInstance().getTextureAtlas()); // берет элементы из ресурсов проекта

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("New game", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnNewGame.setPosition(480, 210);
        btnExitGame.setPosition(480, 110);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        skin.dispose(); // настройки выполнены и объект skin можно удалить
    }

    public void update(float dt){
        background.update(dt);
        stage.act(dt); // перерасчет координат для всех элементов
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0,0,0,1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "StarGame 2022", 0, 600, 1280, 1, false); // halign - выравнивание или можно Align.center
        batch.end();
        stage.draw(); // перерисовка всех элементов
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
    }
}
