package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.jalfsoftware.jalf.entities.AbstractLivingEntity.Direction;
import com.jalfsoftware.jalf.screens.GameScreen;

public class Fireball extends AbstractProjectileEntity {
    private static float maxSpeed = 300;

    public Fireball(float xPos, float yPos, GameScreen gameScreen, Direction lastXDirection, float entityHeight) {
        super(xPos, yPos, "Feuerball", gameScreen, maxSpeed, lastXDirection, entityHeight);
    }

}
