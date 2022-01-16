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
import com.star.app.game.Background;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;
import org.graalvm.compiler.core.target.Backend;

public class GameOverScreen extends AbstractScreen{
    private Background background;
    private BitmapFont font72;
    private BitmapFont font48;
    private BitmapFont font24;
    private StringBuilder sb;
    private Hero defeatedHero;

    public  GameOverScreen(SpriteBatch batch){
        super(batch);
        this.sb = new StringBuilder();
    }

    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font48 = Assets.getInstance().getAssetManager().get("fonts/font48.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");

    }

    public void update(float dt) {
        background.update(dt);
        if (Gdx.input.justTouched()){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {

        update(delta);
        ScreenUtils.clear(0,0,0,1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "The Game is over", 0, 600, 1280,  Align.center, false);
        sb.setLength(0);
        sb.append("SCORE: ").append(defeatedHero.getScore()).append("\n");
        sb.append("MONEY: ").append(defeatedHero.getMoney()).append("\n");
        font48.draw(batch, sb, 0, 400, 1200, Align.center, false);
        font24.draw(batch, "Click screen anywhere to return to main menu", 0, 60, 1280,  Align.center, false);
        batch.end();

    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
