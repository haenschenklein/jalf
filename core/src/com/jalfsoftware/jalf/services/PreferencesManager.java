package com.jalfsoftware.jalf.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Flaiker on 15.09.2014.
 */
public class PreferencesManager {
    public static final String LOG = PreferencesManager.class.getSimpleName();

    private static final String PREFS_NAME              = "jalfprefs";
    private static final String PREF_FPSCOUNTER_ENABLED = "fpscounter.enabled";

    private Preferences preferences;

    private Preferences getPreferences() {
        if (preferences == null) {
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        }
        return preferences;
    }

    public boolean isFpsCounterEnabled() {
        return getPreferences().getBoolean(PREF_FPSCOUNTER_ENABLED, true);
    }

    public void setFPSCounterEnabled(boolean fpsCounterEnabled) {
        getPreferences().putBoolean(PREF_FPSCOUNTER_ENABLED, fpsCounterEnabled);
        getPreferences().flush();
        Gdx.app.log(LOG, "Set " + PREF_FPSCOUNTER_ENABLED + " to " + fpsCounterEnabled);
    }
}
