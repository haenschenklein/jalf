package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jalfsoftware.jalf.Jalf;

/**
 * Basisklasse für Screens
 */
public abstract class AbstractScreen implements Screen {
    public static final String LOG = AbstractScreen.class.getSimpleName();

    protected static final float SCREEN_WIDTH  = 800;
    protected static final float SCREEN_HEIGHT = 480;

    protected final Stage              uiStage; // Stage zum Hosten von UI-Elementen
    protected final Skin               skin; // Skin mit visueller Darstellung des UI
    protected final Jalf               jalf; // Die aufrufende Spielinstanz
    protected final SpriteBatch        batch; // Ein SpriteBatch zum Rendern der Stage
    protected final OrthographicCamera camera; // Die Kamera als Perspektive auf das Spiel
    protected final Viewport           viewport;

    private Label fpsLabel; // FPS-Anzeige

    public AbstractScreen(Jalf jalf) {
        this.jalf = jalf;
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        uiStage = new Stage(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        batch = new SpriteBatch();
        this.fpsLabel = new Label("", skin, "arial", Color.WHITE);
    }

    /**
     * Name des abgeleiteten Screens
     */
    protected abstract String getName();

    @Override
    public void show() {
        Gdx.app.log(LOG, "Showing screen: " + getName());

        //FPS Label
        fpsLabel.setFontScale(0.5f);
        fpsLabel.setPosition(SCREEN_WIDTH - fpsLabel.getPrefWidth(), SCREEN_HEIGHT - fpsLabel.getHeight());
        if (jalf.getPreferencesManager().isFpsCounterEnabled()) uiStage.addActor(fpsLabel);

    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        uiStage.getViewport().update(width, height, false);
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG, "Hiding screen: " + getName());
        dispose();
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing screen: " + getName());
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing screen: " + getName());
        uiStage.dispose();
        //skin.dispose();
        //game.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        preUIrender(delta);

        batch.end();

        // Update FPS-Counter
        fpsLabel.setText("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
        fpsLabel.setPosition(uiStage.getWidth() - fpsLabel.getPrefWidth(), uiStage.getHeight() - fpsLabel.getPrefHeight() / 2);
        uiStage.act(delta);
        uiStage.draw();
    }

    /**
     * Wird vor dem UI gerendert, sodass dieses auf oberster Ebene erscheint
     */
    protected void preUIrender(float delta) {}
}
