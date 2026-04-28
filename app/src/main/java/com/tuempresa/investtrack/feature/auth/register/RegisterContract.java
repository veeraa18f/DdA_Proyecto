package com.tuempresa.investtrack.feature.auth.register;

import java.lang.ref.WeakReference;

import com.tuempresa.investtrack.data.local.UserEntity;

public interface RegisterContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(RegisterViewModel viewModel);

        void showError(String message);

        void showSuccess(String message);

        void navigateToPreviousScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onRegisterClicked(String name, String email, String password);

        void onBackButtonPressed();

        void onDestroyCalled();
    }

    interface Model {
        UserEntity createUser(String name, String email, String password);
    }
}
