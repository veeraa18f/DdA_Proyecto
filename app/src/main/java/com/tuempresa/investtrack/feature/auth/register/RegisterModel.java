package com.tuempresa.investtrack.feature.auth.register;

import android.content.Context;

import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.data.local.UserEntity;

public class RegisterModel implements RegisterContract.Model {

    private final InvestTrackRepository repository;

    public RegisterModel(Context context) {
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public UserEntity createUser(String name, String email, String password) {
        return repository.createUser(name, email, password);
    }
}
