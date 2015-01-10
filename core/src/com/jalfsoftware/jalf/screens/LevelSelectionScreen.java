package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.helper.DefaultActorListener;
import com.jalfsoftware.jalf.helper.Map;

/**
 * Screen zur Levelauswahl
 */
public class LevelSelectionScreen extends AbstractScreen {
    public static final String LOG = LevelSelectionScreen.class.getSimpleName();

    private static final String DEFAULTMAPS_FOLDER = "defaultmaps";
    private static final String USERMAPS_FOLDER    = "maps";
    private static final String MAP_LIST_FILENAME  = "maplist.txt";

    private Table table; // Dient der Anordnung der Menüelemente
    private Array maps;

    public LevelSelectionScreen(Jalf jalf) {
        super(jalf);
        fillMapArray();
    }

    /**
     * Füllt das Array der möglichen zu ladenen Maps mit Maps aus dem defaultmaps- (intern) und dem usermaps-Ordner (extern)
     */
    private void fillMapArray() {
        maps = new Array();
        int defaultMapCount, userMapCount = 0;

        // Lade interne Maps (defaultmaps-Ordner)
        FileHandle maplist = Gdx.files.internal(DEFAULTMAPS_FOLDER + "/" + MAP_LIST_FILENAME);
        String fileContent = maplist.readString();
        String[] entries = fileContent.split("\\r?\\n");
        defaultMapCount = entries.length;

        for (String entry : entries) {
            String[] entryParts = entry.split("\\r?\\|");
            maps.add(new Map(entryParts[0], DEFAULTMAPS_FOLDER + "/" + entryParts[1], true));
        }

        // Lade externe Maps (maps-Ordner neben der jar)
        FileHandle externalMapsFolder = Gdx.files.local(USERMAPS_FOLDER);
        if (!externalMapsFolder.exists()) {
            externalMapsFolder.mkdirs();
        }

        FileHandle[] externalMaps = externalMapsFolder.list();
        for (FileHandle map : externalMaps) {
            if (map.extension().equals("tmx")) {
                maps.add(new Map(map.nameWithoutExtension(), map.path(), false));
                userMapCount++;
            }
        }

        Gdx.app.log(LOG, "Found " + defaultMapCount + " defaultmaps and " + userMapCount + " usermaps");
    }

    @Override
    public void show() {
        super.show();

        table = new Table(skin);
        table.setFillParent(true);
        uiStage.addActor(table);

        table.add().padBottom(50).row();

        // Titel
        Label titleLabel = new Label("to be named", skin, "magneto-92", Color.WHITE);
        table.add(titleLabel).spaceBottom(5).align(1);
        table.row();

        // Untertitel
        table.add("Level-Selection").align(1).spaceBottom(20);
        table.row();

        // Level-Liste
        final List levelList = new List(skin);
        levelList.setItems(maps);
        levelList.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new GameScreen(jalf, (Map) levelList.getSelected()));
            }
        });
        final ScrollPane scrollPane = new ScrollPane(levelList, skin);
        scrollPane.setFadeScrollBars(false);
        table.add(scrollPane).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Button "Back"
        TextButton backButton = new TextButton("Back", skin);
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
        return LevelSelectionScreen.class.getSimpleName();
    }
}
