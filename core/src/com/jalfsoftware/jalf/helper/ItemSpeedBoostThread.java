package com.jalfsoftware.jalf.helper;

import com.jalfsoftware.jalf.entities.AbstractLivingEntity;

public class ItemSpeedBoostThread implements Runnable {
    private final static int timeout     = 8000;
    private final static int speedFaktor = 1000;
    private AbstractLivingEntity entity;

    public ItemSpeedBoostThread(AbstractLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        entity.setMaxSpeed(entity.getMaxSpeed() * speedFaktor);
        System.out.println("SPEED!");

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        entity.setMaxSpeed(entity.getMaxSpeed() / speedFaktor);
        System.out.println("timeout!");
    }

}
