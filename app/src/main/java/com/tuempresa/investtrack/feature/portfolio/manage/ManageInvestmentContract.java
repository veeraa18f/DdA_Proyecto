package com.tuempresa.investtrack.feature.portfolio.manage;

import java.lang.ref.WeakReference;

import com.tuempresa.investtrack.core.navigation.DetailToManageState;
import com.tuempresa.investtrack.data.model.Asset;

public interface ManageInvestmentContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(ManageInvestmentViewModel viewModel);

        void onRefreshPreviewWithUpdatedData(ManageInvestmentViewModel viewModel);

        void showError(String message);

        void showSuccess(String message);

        void navigateToPreviousScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled(DetailToManageState state);

        void onPositionChanged(String price, String quantity);

        void onUpdatePositionClicked(String price, String quantity);

        void onCancelButtonPressed();

        void onDestroyCalled();

        String getCurrentAssetId();
    }

    interface Model {
        boolean isGuestMode();

        Asset getAssetById(String assetId);

        boolean updateAssetPosition(String assetId, double currentPrice, double quantity);
    }
}
