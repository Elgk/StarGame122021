package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class PowerUpsController extends ObjectPool<PowerUp> {
    private GameController gameController;
    private TextureRegion [][] textures; //двумерный массив рисунков

    @Override
    protected PowerUp newObject() {
        return new PowerUp(gameController);
    }

    public PowerUpsController(GameController gameController) {
        this.gameController = gameController;
        // рисунок разделяется на квадратики 60*60 и ими заполняется массив рисунков textures
        this.textures = new TextureRegion(Assets.getInstance().getTextureAtlas().findRegion("powerups")).split(60,60);
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            PowerUp up = activeList.get(i);
            // время жизни элемента изменяется с определенной скоростью, up.Time каждую сек увеличивается на : 0.1, 0.2, 0.3 ...
            // находится остаток от деления на длинну массива рисунков, всегда будет от 0 до 5,
            // т.о. вычисляемое значение нужно для нахождения индекса рисунка, это нужно для эффекта аннимации
            int frameIndex = (int) (up.getTime() / 0.1f) % textures[up.getType().index].length;
            batch.draw(textures[up.getType().index][frameIndex], up.getPosition().x - 30, up.getPosition().y - 30);
        }
    }

    public void setup(float x, float y, float probability){
        if (Math.random() <= probability){ // число от 0 до 1, проверяется относительно заданной вероятности, напр. probability = 0.3, то вероятность 30%
            // тип элемента выбирается случайно
            getActiveElement().activate(PowerUp.Type.values()[MathUtils.random(0,2)], x, y, 60);
        }
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++){
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
