package com.star.app.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.helpers.Poolable;
import org.w3c.dom.Text;



public class InfoText implements Poolable {

    private Color color;
    private StringBuilder text;
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private float maxTime;


    @Override
    public boolean isActive() {
        return active;
    }

    public Color getColor() {
        return color;
    }

    public StringBuilder getText() {
        return text;
    }

    public Vector2 getPosition() {
        return position;
    }

    public InfoText() {
        this.color = Color.GREEN;
        this.text = new StringBuilder();
        this.active = false;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(10.0f, 50.0f);
        this.time = 0.0f;
        this.maxTime = 1.5f;
    }

    public float getLifeTime(){
        return time / maxTime;
    }

    public void  setup(float x, float y, Color color, String text ){
        position.set(x,y);
        active = true;
        this.color = color;
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
    }

    public void  setup(float x, float y, Color color, StringBuilder text ){
        position.set(x,y);
        active = true;
        this.color = color;
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime){
            active = false;
        }
    }
}
