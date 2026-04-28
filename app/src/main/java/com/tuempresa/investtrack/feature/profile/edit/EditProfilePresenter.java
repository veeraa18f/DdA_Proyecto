package com.tuempresa.investtrack.feature.profile.edit;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.tuempresa.investtrack.common.validation.InputValidator;
import com.tuempresa.investtrack.R;

public class EditProfilePresenter implements EditProfileContract.Presenter {

    private final Context context;
    private WeakReference<EditProfileContract.View> view;
    private EditProfileContract.Model model;

    public EditProfilePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<EditProfileContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(EditProfileContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        if (model.isGuestMode()) {
            showGuestProfileReadOnlyMessage();
            currentView().navigateToPreviousScreen();
            return;
        }

        EditProfileViewModel viewModel = new EditProfileViewModel();
        viewModel.username = model.getProfileUsername();
        viewModel.phone = model.getProfilePhone();
        viewModel.email = model.getProfileEmail();
        currentView().onRefreshViewWithUpdatedData(viewModel);
    }

    @Override
    public void onSaveProfileClicked(String username, String phone, String email) {
        if (model.isGuestMode()) {
            showGuestProfileReadOnlyMessage();
            currentView().navigateToPreviousScreen();
            return;
        }

        String normalizedUsername = InputValidator.normalize(username);
        String normalizedPhone = InputValidator.normalize(phone);
        String normalizedEmail = InputValidator.normalize(email);

        if (normalizedUsername.isEmpty()) {
            currentView().showError(getString(
                    R.string.profile_error_invalid_username,
                    "Enter a username."
            ));
            return;
        }

        if (normalizedPhone.isEmpty()) {
            currentView().showError(getString(
                    R.string.profile_error_invalid_phone,
                    "Enter a phone number."
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

        model.setProfileData(normalizedUsername, normalizedPhone, normalizedEmail);
        currentView().showSuccess(getString(
                R.string.profile_saved,
                "Profile updated."
        ));
        currentView().navigateToPreviousScreen();
    }

    @Override
    public void onCancelButtonPressed() {
        currentView().navigateToPreviousScreen();
    }

    @Override
    public void onDestroyCalled() {
        if (view != null) {
            view.clear();
        }
    }

    private void showGuestProfileReadOnlyMessage() {
        currentView().showError(getString(
                R.string.profile_guest_read_only,
                "Guest mode is read-only. Log in to edit your profile."
        ));
    }

    private String getString(int stringRes, String fallback, Object... args) {
        if (context == null) {
            return args.length == 0 ? fallback : String.format(Locale.US, fallback, args);
        }
        return context.getString(stringRes, args);
    }

    private EditProfileContract.View currentView() {
        EditProfileContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Edit profile view is not attached.");
        }
        return currentView;
    }
}
