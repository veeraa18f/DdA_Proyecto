package com.tuempresa.investtrack.feature.auth.login;

import java.lang.ref.WeakReference;

import com.tuempresa.investtrack.data.local.UserEntity;

public interface LoginContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(LoginViewModel viewModel);

        void showError(String message);

        void navigateToHomeScreen();

        void navigateToRegisterScreen();

        void navigateToForgotPasswordScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onLoginClicked(String email, String password);

        void onRegisterClicked();

        void onForgotPasswordClicked();

        void onGuestModeClicked();

        void onDestroyCalled();
    }

    interface Model {
        UserEntity authenticateUser(String email, String password);

        void setLoggedIn(String userId);

        void enableGuestMode();
    }

}
