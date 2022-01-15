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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen{
    private BitmapFont font72;
    private BitmapFont font24;
    private BitmapFont font32;
    private Stage stage;
    private StringBuilder sb;

    public  GameOverScreen(SpriteBatch batch){
        super(batch);
        this.sb = new StringBuilder();
    }

    @Override
    public void show() {
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getTextureAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnBackToMenu = new TextButton("Back to Menu", textButtonStyle);
        btnBackToMenu.setPosition(480, 110);
        btnBackToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        stage.addActor(btnBackToMenu);
        skin.dispose();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void setScore(int value){
        sb.setLength(0);
        sb.append("SCORE: ").append(value).append("\n");
    }

    @Override
    public void render(float delta) {

        update(delta);
        ScreenUtils.clear(0,0,0,1);
        batch.begin();
        font72.draw(batch, "Game is over", 0, 600, 1280,  Align.center, false);
        font32.draw(batch, sb, 0, 400, 1200, Align.center, false);
        batch.end();
        stage.draw();
    }


}
