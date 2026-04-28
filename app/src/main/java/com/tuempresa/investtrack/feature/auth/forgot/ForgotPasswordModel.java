package com.tuempresa.investtrack.feature.auth.forgot;

import android.content.Context;

import com.tuempresa.investtrack.data.repository.InvestTrackRepository;

public class ForgotPasswordModel implements ForgotPasswordContract.Model {

    private final InvestTrackRepository repository;

    public ForgotPasswordModel(Context context) {
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return repository.userExistsByEmail(email);
    }

    @Override
    public boolean resetPassword(String email, String password) {
        return repository.resetPassword(email, password);
    }
}
