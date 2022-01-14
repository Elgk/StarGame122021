package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

public class PowerUp implements Poolable {
    public enum Type{
        MEDKIT(0),
        MONEY(1),
        AMMOS(2);

        int index;

         Type(int index){
            this.index = index;
        }
    }

    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float time; // время активности элемента и время для анимации
    private int power; // характиристика "мощности" элемента
    private Type type;
    private GameController gameController;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public PowerUp(GameController gameController) {
        this.gameController = gameController;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.active = false;
    }

    public void activate(Type type, float x, float y, int power){
        this.type = type;
        this.position.set(x,y);
        this.velocity.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f)); // выбор произвольного направления
        velocity.nor().scl(50.0f); // величина скорости
        this.active = true;
        this.power = power;
        this.time = 0.0f;
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= 7.0f){ // время жизни элемента 7 сек
            deactivate();
        }
    }

    public void deactivate() {
        active = false;
    }
}
