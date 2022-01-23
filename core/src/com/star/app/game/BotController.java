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
    public void setup(float x, float y){
        getActiveElement().activate(x, y);
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++){
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
