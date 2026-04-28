package com.tuempresa.investtrack.core.app;

import android.app.Application;

import com.tuempresa.investtrack.core.settings.LanguageHelper;
import com.tuempresa.investtrack.core.settings.ThemeHelper;

public class InvestTrackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LanguageHelper.applySavedLanguage(this);
        ThemeHelper.applySavedTheme(this);
    }
}
