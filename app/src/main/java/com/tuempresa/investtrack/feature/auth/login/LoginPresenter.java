package com.tuempresa.investtrack.feature.auth.login;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.tuempresa.investtrack.data.local.UserEntity;
import com.tuempresa.investtrack.common.validation.InputValidator;
import com.tuempresa.investtrack.R;

public class LoginPresenter implements LoginContract.Presenter {

    private final Context context;
    private WeakReference<LoginContract.View> view;
    private LoginContract.Model model;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<LoginContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(LoginContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        currentView().onRefreshViewWithUpdatedData(new LoginViewModel());
    }

    @Override
    public void onLoginClicked(String email, String password) {
        String normalizedEmail = InputValidator.normalize(email);
        String normalizedPassword = InputValidator.normalize(password);

        if (!InputValidator.isValidEmail(normalizedEmail) || normalizedPassword.length() < 6) {
            currentView().showError(getString(
                    R.string.login_error_invalid_credentials,
                    "Enter a valid email and a password with at least 6 characters."
            ));
            return;
        }

        UserEntity user = model.authenticateUser(normalizedEmail, normalizedPassword);
        if (user == null) {
            currentView().showError(getString(
                    R.string.login_error_invalid_credentials,
                    "Enter a valid email and a password with at least 6 characters."
            ));
            return;
        }

        model.setLoggedIn(user.id);
        currentView().navigateToHomeScreen();
    }

    @Override
    public void onRegisterClicked() {
        currentView().navigateToRegisterScreen();
    }

    @Override
    public void onForgotPasswordClicked() {
        currentView().navigateToForgotPasswordScreen();
    }

    @Override
    public void onGuestModeClicked() {
        model.enableGuestMode();
        currentView().navigateToHomeScreen();
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

    private LoginContract.View currentView() {
        LoginContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Login view is not attached.");
        }
        return currentView;
    }
}
