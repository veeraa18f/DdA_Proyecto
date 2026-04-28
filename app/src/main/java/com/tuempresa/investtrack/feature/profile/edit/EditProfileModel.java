package com.tuempresa.investtrack.feature.profile.edit;

import android.content.Context;

import com.tuempresa.investtrack.core.session.AppPreferences;

public class EditProfileModel implements EditProfileContract.Model {

    private final Context context;

    public EditProfileModel(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public String getProfileUsername() {
        return AppPreferences.getProfileUsername(context);
    }

    @Override
    public String getProfilePhone() {
        return AppPreferences.getProfilePhone(context);
    }

    @Override
    public String getProfileEmail() {
        return AppPreferences.getProfileEmail(context);
    }

    @Override
    public boolean isGuestMode() {
        return AppPreferences.isGuestMode(context);
    }

    @Override
    public void setProfileData(String username, String phone, String email) {
        AppPreferences.setProfileData(context, username, phone, email);
    }
}
