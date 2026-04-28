package com.tuempresa.investtrack.feature.profile.edit;

import java.lang.ref.WeakReference;

public interface EditProfileContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(EditProfileViewModel viewModel);

        void showError(String message);

        void showSuccess(String message);

        void navigateToPreviousScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onSaveProfileClicked(String username, String phone, String email);

        void onCancelButtonPressed();

        void onDestroyCalled();
    }

    interface Model {
        String getProfileUsername();

        String getProfilePhone();

        String getProfileEmail();

        boolean isGuestMode();

        void setProfileData(String username, String phone, String email);
    }
}
