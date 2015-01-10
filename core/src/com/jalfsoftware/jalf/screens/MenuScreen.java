package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.helper.DefaultActorListener;

/**
 * Screen zur Darstellung des Einstiegmenüs
 */
public class MenuScreen extends AbstractScreen {
    public static final String LOG = MenuScreen.class.getSimpleName();

    private Table table; // Dient der Anordnung der Menüelemente

    public MenuScreen(Jalf jalf) {
        super(jalf);
    }

    @Override
    public void show() {
        super.show();

        table = new Table(skin);
        table.setFillParent(true);
        uiStage.addActor(table);

        table.add().padBottom(50).row();

        // Titel
        Label titleLabel = new Label("tbn", skin, "digital7-92", Color.WHITE);
        //titleLabel.setFontScale(2);
        table.add(titleLabel).spaceBottom(5).align(1);
        table.row();

        // Untertitel
        table.add("[insertGameNameHere]").align(1).spaceBottom(20);
        table.row();

        // Button "Start"
        TextButton startGameButton = new TextButton("START", skin);
        startGameButton.setColor(1, 1, 1, 0.9f);
        startGameButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new LevelSelectionScreen(jalf));
            }
        });
        table.add(startGameButton).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Button "Options"
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        optionsButton.setColor(1, 1, 1, 0.9f);
        optionsButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new OptionsScreen(jalf));
            }
        });
        table.add(optionsButton).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Button "Exit"
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.setColor(1, 1, 1, 0.9f);
        exitButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();
            }
        });
        table.add(exitButton).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Footer
        table.add(new Label("www.jalfsoftware.com", skin)).row();
        table.add().padBottom(25).row();

        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    protected String getName() {
        return MenuScreen.class.getSimpleName();
    }
}
