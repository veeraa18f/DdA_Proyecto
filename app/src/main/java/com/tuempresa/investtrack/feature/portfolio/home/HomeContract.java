package com.tuempresa.investtrack.feature.portfolio.home;

import java.lang.ref.WeakReference;
import java.util.List;

import com.tuempresa.investtrack.data.model.Asset;

public interface HomeContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(HomeViewModel viewModel);

        void navigateToDetailScreen();

        void navigateToAddInvestmentScreen();

        void showAddInvestmentOptions(List<HomeAssetViewModel> assets);

        void showCatalogInvestmentQuantityInput(HomeAssetViewModel asset);

        void showRemoveInvestmentOptions(List<HomeAssetViewModel> assets);

        void showError(String message);
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onCreateCalled(HomeState state);

        void onSearchQueryChanged(String query);

        void onFilterSelected(String filter);

        void onAssetClicked(String assetId);

        void onAddInvestmentClicked();

        void onCatalogInvestmentSelected(String assetId);

        void onCatalogInvestmentQuantityConfirmed(String assetId, String quantity);

        void onCustomInvestmentSelected();

        void onRemoveInvestmentClicked();

        void onRemoveInvestmentSelected(String assetId);

        void onDestroyCalled();
    }

    interface Model {
        List<Asset> getAssets();

        List<Asset> getAvailableCatalogAssets();

        boolean isGuestMode();

        boolean isFavorite(String assetId);

        boolean addCatalogAsset(String assetId, double quantity);

        boolean removeAsset(String assetId);
    }
}
