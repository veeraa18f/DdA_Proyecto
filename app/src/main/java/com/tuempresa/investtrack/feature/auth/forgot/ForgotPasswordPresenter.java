package com.tuempresa.investtrack.feature.auth.forgot;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.tuempresa.investtrack.common.validation.InputValidator;
import com.tuempresa.investtrack.R;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    private final Context context;
    private WeakReference<ForgotPasswordContract.View> view;
    private ForgotPasswordContract.Model model;

    public ForgotPasswordPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<ForgotPasswordContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ForgotPasswordContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        currentView().onRefreshViewWithUpdatedData(new ForgotPasswordViewModel());
    }

    @Override
    public void onResetPasswordClicked(String email, String password, String confirmPassword) {
        String normalizedEmail = InputValidator.normalize(email);
        String normalizedPassword = InputValidator.normalize(password);
        String normalizedConfirmPassword = InputValidator.normalize(confirmPassword);

        if (!InputValidator.isValidEmail(normalizedEmail)) {
            currentView().showError(getString(
                    R.string.profile_error_invalid_email,
                    "Enter a valid email address."
            ));
            return;
        }

        if (!model.userExistsByEmail(normalizedEmail)) {
            currentView().showError(getString(
                    R.string.forgot_error_email_not_found,
                    "No account was found with that email."
            ));
            return;
        }

        if (normalizedPassword.length() < 6) {
            currentView().showError(getString(
                    R.string.forgot_error_short_password,
                    "Password must have at least 6 characters."
            ));
            return;
        }

        if (!normalizedPassword.equals(normalizedConfirmPassword)) {
            currentView().showError(getString(
                    R.string.forgot_error_password_mismatch,
                    "Passwords do not match."
            ));
            return;
        }

        if (!model.resetPassword(normalizedEmail, normalizedPassword)) {
            currentView().showError(getString(
                    R.string.forgot_error_update_failed,
                    "Could not update the password."
            ));
            return;
        }

        currentView().showSuccess(getString(
                R.string.forgot_success,
                "Password updated. You can log in now."
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

    private ForgotPasswordContract.View currentView() {
        ForgotPasswordContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Forgot password view is not attached.");
        }
        return currentView;
    }
}
