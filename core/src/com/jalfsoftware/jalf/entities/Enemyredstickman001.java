package com.jalfsoftware.jalf.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jalfsoftware.jalf.screens.GameScreen;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Anton Löwen on 25.10.2014.
 */

public class Enemyredstickman001 extends AbstractLivingEntity{
    private CopyOnWriteArrayList EntityList;

    public Enemyredstickman001(float xPos, float yPos, GameScreen gameScreen) {
        super(xPos, yPos, "jalf_Stand", 10, 10, 15, 3, 5, gameScreen);
        //TODO: jonathan, Gegnertexture in Textureatlas, Zeile drüber Name ersetzen
    }

    @Override
    public void render(Batch batch){
        super.render(batch);

        switch (lastXDirection) {
            case LEFT:
                move(Direction.LEFT);
                if(currentSpeed.x  == 0 ) {
                    move(Direction.RIGHT);
                }
                break;
            case RIGHT:
                move(Direction.RIGHT);
                if(currentSpeed.x  == 0 ) {
                    move(Direction.LEFT);
                }
                break;
            case NONE:
                if(currentSpeed.x  == 0 ) {
                    move(Direction.RIGHT);
                }
                break;
        }
    }

    @Override
    protected void itemCoinPlus() {
        //TODO: Lukas???
    }

    @Override
    protected void itemSetFireballAvalible() {
        //TODO: Lukas???
    }

    @Override
    protected void itemJumpBoost() {
        //TODO: Lukas???
    }

    @Override
    protected void itemSpeedBoost() {
        //TODO: Lukas???
    }

    @Override
    protected void itemLivePlus() {
        //TODO: Lukas???
    }

    @Override
    protected void itemHpPlus() {
        //TODO: Lukas???
    }
}

