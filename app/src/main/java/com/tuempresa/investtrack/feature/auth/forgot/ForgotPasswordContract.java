package com.tuempresa.investtrack.feature.auth.forgot;

import java.lang.ref.WeakReference;

public interface ForgotPasswordContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(ForgotPasswordViewModel viewModel);

        void showError(String message);

        void showSuccess(String message);

        void navigateToPreviousScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onResetPasswordClicked(String email, String password, String confirmPassword);

        void onBackButtonPressed();

        void onDestroyCalled();
    }

    interface Model {
        boolean userExistsByEmail(String email);

        boolean resetPassword(String email, String password);
    }
}
