package com.tuempresa.investtrack.feature.profile.view;

import java.lang.ref.WeakReference;

public interface ProfileContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(ProfileViewModel viewModel);

        void showProfilePhotoOptions(int selectedOption);

        void showLanguageOptions(int selectedOption);

        void showError(String message);

        void navigateToEditProfileScreen();

        void navigateToLoginScreen();

        void recreateScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onResumeCalled();

        void onProfilePhotoClicked();

        void onProfilePhotoSelected(int optionIndex);

        void onEditProfileClicked();

        void onLanguageClicked();

        void onLanguageSelected(int optionIndex);

        void onDarkModeChanged(boolean enabled);

        void onLogoutClicked();

        void onDestroyCalled();
    }

    interface Model {
        String getProfileUsername();

        String getProfileEmail();

        String getProfilePhone();

        int getProfilePhotoIndex();

        void setProfilePhotoIndex(int profilePhotoIndex);

        boolean isGuestMode();

        String getLanguageCode();

        void setLanguageCode(String languageCode);

        boolean isDarkModeEnabled();

        void setDarkModeEnabled(boolean enabled);

        void logout();
    }
}
