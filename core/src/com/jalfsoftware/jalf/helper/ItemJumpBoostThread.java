package com.jalfsoftware.jalf.helper;

import com.jalfsoftware.jalf.entities.AbstractLivingEntity;

public class ItemJumpBoostThread implements Runnable {
    private final static int   timeout    = 8000;
    private final static float jumpFaktor = 1.5f;
    private AbstractLivingEntity entity;

    public ItemJumpBoostThread(AbstractLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        entity.setJumpSpeed(entity.getJumpSpeed() * jumpFaktor);
        System.out.println("JUMP!");

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        entity.setJumpSpeed(entity.getJumpSpeed() / jumpFaktor);
        System.out.println("timeout!");
    }
}
