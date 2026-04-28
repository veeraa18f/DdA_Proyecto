package com.tuempresa.investtrack.feature.profile.view;

import android.content.Context;

import com.tuempresa.investtrack.core.session.AppPreferences;
import com.tuempresa.investtrack.core.settings.LanguageHelper;
import com.tuempresa.investtrack.core.settings.ThemeHelper;

public class ProfileModel implements ProfileContract.Model {

    private final Context context;

    public ProfileModel(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public String getProfileUsername() {
        return AppPreferences.getProfileUsername(context);
    }

    @Override
    public String getProfileEmail() {
        return AppPreferences.getProfileEmail(context);
    }

    @Override
    public String getProfilePhone() {
        return AppPreferences.getProfilePhone(context);
    }

    @Override
    public int getProfilePhotoIndex() {
        return AppPreferences.getProfilePhotoIndex(context);
    }

    @Override
    public void setProfilePhotoIndex(int profilePhotoIndex) {
        AppPreferences.setProfilePhotoIndex(context, profilePhotoIndex);
    }

    @Override
    public boolean isGuestMode() {
        return AppPreferences.isGuestMode(context);
    }

    @Override
    public String getLanguageCode() {
        return AppPreferences.getLanguageCode(context);
    }

    @Override
    public void setLanguageCode(String languageCode) {
        AppPreferences.setLanguageCode(context, languageCode);
        LanguageHelper.setLanguage(languageCode);
    }

    @Override
    public boolean isDarkModeEnabled() {
        return AppPreferences.isDarkModeEnabled(context);
    }

    @Override
    public void setDarkModeEnabled(boolean enabled) {
        AppPreferences.setDarkModeEnabled(context, enabled);
        ThemeHelper.setDarkMode(enabled);
    }

    @Override
    public void logout() {
        AppPreferences.logout(context);
    }
}
