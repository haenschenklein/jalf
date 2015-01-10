package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jalfsoftware.jalf.entities.AbstractLivingEntity.Direction;
import com.jalfsoftware.jalf.screens.GameScreen;

public class AbstractProjectileEntity extends AbstractEntity/* implements Poolable*/ {
    private float xPos;
    private float yPos;
    private float maxSpeed;
    private float delta = Gdx.graphics.getDeltaTime();
    private Direction lastXDirection;
    private float     i;
    public  boolean   alive;

    public AbstractProjectileEntity(float xPos, float yPos, String texture, GameScreen gameScreen, float maxSpeed,
                                    Direction lastXDirection, float entityHeight) {
        super(xPos, yPos, texture, gameScreen);
        this.xPos = xPos;
        this.yPos = yPos + entityHeight - gameScreen.getMap().convertToScreenUnits(1);
        this.maxSpeed = maxSpeed;
        this.lastXDirection = lastXDirection;
        i = 0f;
        alive = true;
        System.out.println("new projectile");
    }

    @Override
    public void render(Batch batch) {
        if (lastXDirection == Direction.LEFT) xPos = xPos - maxSpeed * delta;
        else xPos = xPos + maxSpeed * delta;
        this.setPosition(xPos, yPos);

        this.setRotation(i);
        super.render(batch);
        i += 18;
        if (i > 360) i = 0;
        if (xPos > 200) alive = false;
    }
}
