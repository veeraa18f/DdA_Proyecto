package com.tuempresa.investtrack.core.settings;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.tuempresa.investtrack.core.session.AppPreferences;

public final class ThemeHelper {

    private ThemeHelper() {
    }

    public static void applySavedTheme(Context context) {
        setDarkMode(AppPreferences.isDarkModeEnabled(context));
    }

    public static void setDarkMode(boolean enabled) {
        AppCompatDelegate.setDefaultNightMode(
                enabled
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
