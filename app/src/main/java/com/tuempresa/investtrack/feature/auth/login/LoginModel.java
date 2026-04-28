package com.tuempresa.investtrack.feature.auth.login;

import android.content.Context;

import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.data.local.UserEntity;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class LoginModel implements LoginContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public LoginModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public UserEntity authenticateUser(String email, String password) {
        return repository.authenticateUser(email, password);
    }

    @Override
    public void setLoggedIn(String userId) {
        AppPreferences.setLoggedIn(context, true, userId);
    }

    @Override
    public void enableGuestMode() {
        AppPreferences.enableGuestMode(context);
    }
}
