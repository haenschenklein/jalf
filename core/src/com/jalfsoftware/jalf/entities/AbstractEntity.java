package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.screens.GameScreen;

/**
 * Abstrakte Entitätsklasse mit Sprite
 */
public abstract class AbstractEntity {
    protected Sprite sprite;

    public AbstractEntity(float xPos, float yPos, Texture texture) {
        sprite = new Sprite(texture);
        sprite.setPosition(xPos, yPos);
        sprite.setSize(texture.getWidth() * GameScreen.UNITSCALE, texture.getHeight() * GameScreen.UNITSCALE);
    }

    public void render(Batch batch) {
        sprite.draw(batch);
    }

    protected void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
