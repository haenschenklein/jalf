package com.jalfsoftware.jalf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.jalfsoftware.jalf.screens.AbstractScreen;
import com.jalfsoftware.jalf.screens.MenuScreen;
import com.jalfsoftware.jalf.services.ConsoleManager;
import com.jalfsoftware.jalf.services.PreferencesManager;

import java.util.HashMap;

/**
 * Einstiegsklasse für das Spiel
 */
public class Jalf extends Game {
    private static final String LOG = Jalf.class.getSimpleName();

    // Services
    private PreferencesManager preferencesManager;
    private ConsoleManager     consoleManager;

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public ConsoleManager getConsoleManager() {
        return consoleManager;
    }

    private void initializeConsoleCommands() {
        consoleManager.clearCommands();

        consoleManager.addCommand(new ConsoleManager.ConsoleCommand("test", new ConsoleManager.CommandExecutor() {
            @Override
            public void OnCommandFired(HashMap<String, String> parValuePairs) {
                if (parValuePairs.containsKey("x")) {
                    Gdx.app.log(LOG, parValuePairs.get("x"));
                }
                if (parValuePairs.containsKey("y")) {
                    Gdx.app.log(LOG, parValuePairs.get("y"));
                }
            }
        }));

        consoleManager.addCommand(new ConsoleManager.ConsoleCommand("fullscreen", new ConsoleManager.CommandExecutor() {
            @Override
            public void OnCommandFired(HashMap<String, String> parValuePairs) {
                if (parValuePairs.containsKey("true")) {
                    Gdx.graphics
                            .setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
                    Gdx.app.log(LOG, "Enabled fullscreen");
                } else if (parValuePairs.containsKey("false")) {
                    Gdx.graphics.setDisplayMode((int) AbstractScreen.SCREEN_WIDTH, (int) AbstractScreen.SCREEN_HEIGHT, false);
                    Gdx.app.log(LOG, "Disabled fullscreen");
                }
            }
        }));

        consoleManager.addCommand(new ConsoleManager.ConsoleCommand("exit", new ConsoleManager.CommandExecutor() {
            @Override
            public void OnCommandFired(HashMap<String, String> parValuePairs) {
                Gdx.app.exit();
            }
        }));
    }

    @Override
    public void create() {
        Gdx.app.log(LOG, "Creating game on " + Gdx.app.getType());

        preferencesManager = new PreferencesManager();
        consoleManager = new ConsoleManager();
        initializeConsoleCommands();
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

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        initializeConsoleCommands();
        if (screen instanceof ConsoleManager.CommandableInstance)
            consoleManager.addCommands(((ConsoleManager.CommandableInstance) screen).getConsoleCommands());
    }
}
