package com.jalfsoftware.jalf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.jalfsoftware.jalf.screens.MenuScreen;
import com.jalfsoftware.jalf.services.PreferencesManager;

/**
 * Einstiegsklasse für das Spiel
 */
public class Jalf extends Game {
    private static final String LOG = Jalf.class.getSimpleName();

    // Services
    private PreferencesManager preferencesManager;

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    @Override
    public void create() {
        Gdx.app.log(LOG, "Creating game on " + Gdx.app.getType());

        preferencesManager = new PreferencesManager();
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log(LOG, "Disposing game");
    }

    @Override
    public void pause() {
        super.pause();
        Gdx.app.log(LOG, "Pausing game");
    }

    @Override
    public void resume() {
        super.resume();
        Gdx.app.log(LOG, "Resuming game");
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.log(LOG, "Resizing game to: " + width + " x " + height);

        if (getScreen() == null) setScreen(new MenuScreen(this)); // Bewirkt erste Screenauswahl
    }
}
