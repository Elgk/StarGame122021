package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class BotController extends ObjectPool<Bot> {
    private GameController gameController;

    @Override
    protected Bot newObject() {
        return new Bot(gameController);
    }

    public BotController(GameController gameController) {
        this.gameController = gameController;
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            Bot bot = activeList.get(i);
            bot.render(batch);
        }
    }
// при установке можно передавать различные характеристики для бота: оружие, жизнеспособность, мощность
    public void setup(float x, float y, float probability){
        if (Math.random() < probability) {
            int hpIncrease    = gameController.getGameLevel() > 1 ? gameController.getGameLevel() * 10 : 0;
            int powerIncrease = gameController.getGameLevel() > 1 ? gameController.getGameLevel() * 100 : 0;
            getActiveElement().activate(x, y, hpIncrease, powerIncrease);
        }
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++){
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
