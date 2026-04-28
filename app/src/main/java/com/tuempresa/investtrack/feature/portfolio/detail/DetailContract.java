package com.tuempresa.investtrack.feature.portfolio.detail;

import java.lang.ref.WeakReference;

import com.tuempresa.investtrack.data.model.Asset;

public interface DetailContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(DetailViewModel viewModel);

        void onRefreshFavoriteWithUpdatedData(DetailFavoriteViewModel viewModel);

        void navigateToManageScreen();

        void navigateToPreviousScreen();

        void showError(String message);
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled(DetailState state);

        void onManageInvestmentClicked();

        void onFavoriteClicked();

        void onChartRangeSelected(String chartRange);

        void onBackButtonPressed();

        void onDestroyCalled();

        String getCurrentAssetId();

        String getSelectedChartRange();
    }

    interface Model {
        Asset getAssetById(String assetId);

        boolean isGuestMode();

        boolean isFavorite(String assetId);

        boolean toggleFavorite(String assetId);
    }
}
