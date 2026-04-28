package com.tuempresa.investtrack.feature.profile.view;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.tuempresa.investtrack.core.settings.LanguageHelper;
import com.tuempresa.investtrack.R;

public class ProfilePresenter implements ProfileContract.Presenter {

    private final Context context;
    private WeakReference<ProfileContract.View> view;
    private ProfileContract.Model model;

    public ProfilePresenter() {
        this(null);
    }

    public ProfilePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<ProfileContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ProfileContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        refreshProfile();
    }

    @Override
    public void onResumeCalled() {
        refreshProfile();
    }

    @Override
    public void onProfilePhotoClicked() {
        if (model.isGuestMode()) {
            showGuestProfileReadOnlyMessage();
            return;
        }

        currentView().showProfilePhotoOptions(model.getProfilePhotoIndex() - 1);
    }

    @Override
    public void onProfilePhotoSelected(int optionIndex) {
        if (model.isGuestMode()) {
            showGuestProfileReadOnlyMessage();
            return;
        }

        model.setProfilePhotoIndex(optionIndex + 1);
        refreshProfile();
    }

    @Override
    public void onEditProfileClicked() {
        if (model.isGuestMode()) {
            showGuestProfileReadOnlyMessage();
            return;
        }

        currentView().navigateToEditProfileScreen();
    }

    @Override
    public void onLanguageClicked() {
        String currentLanguage = model.getLanguageCode();
        int selectedOption = LanguageHelper.LANGUAGE_SPANISH.equals(currentLanguage) ? 1 : 0;
        currentView().showLanguageOptions(selectedOption);
    }

    @Override
    public void onLanguageSelected(int optionIndex) {
        String languageCode = optionIndex == 1
                ? LanguageHelper.LANGUAGE_SPANISH
                : LanguageHelper.LANGUAGE_ENGLISH;
        model.setLanguageCode(languageCode);
        currentView().recreateScreen();
    }

    @Override
    public void onDarkModeChanged(boolean enabled) {
        model.setDarkModeEnabled(enabled);
    }

    @Override
    public void onLogoutClicked() {
        model.logout();
        currentView().navigateToLoginScreen();
    }

    @Override
    public void onDestroyCalled() {
        if (view != null) {
            view.clear();
        }
    }

    private void refreshProfile() {
        currentView().onRefreshViewWithUpdatedData(buildViewModel());
    }

    private ProfileViewModel buildViewModel() {
        ProfileViewModel viewModel = new ProfileViewModel();
        viewModel.username = model.getProfileUsername();
        viewModel.email = model.getProfileEmail();
        viewModel.phone = model.getProfilePhone();
        viewModel.profilePhotoDrawable = getProfilePhotoDrawable(model.getProfilePhotoIndex());
        viewModel.languageText = LanguageHelper.LANGUAGE_SPANISH.equals(model.getLanguageCode())
                ? R.string.language_spanish
                : R.string.language_english;
        viewModel.darkModeEnabled = model.isDarkModeEnabled();
        return viewModel;
    }

    private int getProfilePhotoDrawable(int profilePhotoIndex) {
        if (profilePhotoIndex == 2) {
            return R.drawable.profile_photo_2;
        }
        if (profilePhotoIndex == 3) {
            return R.drawable.profile_photo_3;
        }
        return R.drawable.profile_photo_1;
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

    private ProfileContract.View currentView() {
        ProfileContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Profile view is not attached.");
        }
        return currentView;
    }
}
