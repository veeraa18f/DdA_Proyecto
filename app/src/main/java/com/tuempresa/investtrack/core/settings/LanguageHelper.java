package com.tuempresa.investtrack.core.settings;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.tuempresa.investtrack.core.session.AppPreferences;

public final class LanguageHelper {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_SPANISH = "es";

    private LanguageHelper() {
    }

    public static void applySavedLanguage(Context context) {
        setLanguage(AppPreferences.getLanguageCode(context));
    }

    public static void setLanguage(String languageCode) {
        AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(normalizeLanguageCode(languageCode))
        );
    }

    public static String normalizeLanguageCode(String languageCode) {
        if (LANGUAGE_SPANISH.equals(languageCode)) {
            return LANGUAGE_SPANISH;
        }
        return LANGUAGE_ENGLISH;
    }
}
