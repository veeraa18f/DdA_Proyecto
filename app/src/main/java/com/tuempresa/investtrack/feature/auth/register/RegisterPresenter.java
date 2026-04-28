package com.tuempresa.investtrack.feature.auth.register;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.tuempresa.investtrack.data.local.UserEntity;
import com.tuempresa.investtrack.common.validation.InputValidator;
import com.tuempresa.investtrack.R;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final Context context;
    private WeakReference<RegisterContract.View> view;
    private RegisterContract.Model model;

    public RegisterPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<RegisterContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(RegisterContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        currentView().onRefreshViewWithUpdatedData(new RegisterViewModel());
    }

    @Override
    public void onRegisterClicked(String name, String email, String password) {
        String normalizedName = InputValidator.normalize(name);
        String normalizedEmail = InputValidator.normalize(email);
        String normalizedPassword = InputValidator.normalize(password);

        if (normalizedName.isEmpty()) {
            currentView().showError(getString(
                    R.string.profile_error_invalid_username,
                    "Enter a username."
            ));
            return;
        }

        if (!InputValidator.isValidEmail(normalizedEmail)) {
            currentView().showError(getString(
                    R.string.profile_error_invalid_email,
                    "Enter a valid email address."
            ));
            return;
        }

        if (normalizedPassword.length() < 6) {
            currentView().showError(getString(
                    R.string.login_error_invalid_credentials,
                    "Enter a valid email and a password with at least 6 characters."
            ));
            return;
        }

        UserEntity user = model.createUser(normalizedName, normalizedEmail, normalizedPassword);
        if (user == null) {
            currentView().showError(getString(
                    R.string.register_error_duplicate_email,
                    "There is already an account with that email."
            ));
            return;
        }

        currentView().showSuccess(getString(
                R.string.register_success,
                "Account created successfully."
        ));
        currentView().navigateToPreviousScreen();
    }

    @Override
    public void onBackButtonPressed() {
        currentView().navigateToPreviousScreen();
    }

    @Override
    public void onDestroyCalled() {
        if (view != null) {
            view.clear();
        }
    }

    private String getString(int stringRes, String fallback, Object... args) {
        if (context == null) {
            return args.length == 0 ? fallback : String.format(Locale.US, fallback, args);
        }
        return context.getString(stringRes, args);
    }

    private RegisterContract.View currentView() {
        RegisterContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Register view is not attached.");
        }
        return currentView;
    }
}
