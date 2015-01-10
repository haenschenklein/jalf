package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.helper.DefaultActorListener;

/**
 * Screen für das Optionsmenü
 */
public class OptionsScreen extends AbstractScreen {
    public static final String LOG = OptionsScreen.class.getSimpleName();

    private Table table; // Dient der Anordnung der Menüelemente

    public OptionsScreen(Jalf jalf) {
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
        table.add("OPTIONS").align(1).spaceBottom(20);
        table.row();

        // FPS-Counter-Checkbox
        final CheckBox fpsCounterCheckbox = new CheckBox("FPS-Counter", skin);
        fpsCounterCheckbox.setChecked(jalf.getPreferencesManager().isFpsCounterEnabled());
        fpsCounterCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = fpsCounterCheckbox.isChecked();
                jalf.getPreferencesManager().setFPSCounterEnabled(enabled);
            }
        });
        table.add(fpsCounterCheckbox).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Button "Back"
        TextButton backButton = new TextButton("BACK", skin);
        backButton.setColor(1, 1, 1, 0.9f);
        backButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new MenuScreen(jalf));
            }
        });
        table.add(backButton).fill().pad(0, 150, 25, 150);
        table.row();

        // Footer
        table.add(new Label("www.jalfsoftware.com", skin)).row();
        table.add().padBottom(25).row();

        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    protected String getName() {
        return OptionsScreen.class.getSimpleName();
    }
}
