package com.tuempresa.investtrack.feature.portfolio.favorites;

import java.lang.ref.WeakReference;
import java.util.List;

import com.tuempresa.investtrack.data.model.Asset;

public interface FavoritesContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(FavoritesViewModel viewModel);

        void navigateToDetailScreen();

        void navigateToPreviousScreen();

        void showError(String message);
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onResumeCalled();

        void onAssetClicked(String assetId);

        void onDestroyCalled();
    }

    interface Model {
        boolean isGuestMode();

        List<Asset> getFavoriteAssets();
    }
}
