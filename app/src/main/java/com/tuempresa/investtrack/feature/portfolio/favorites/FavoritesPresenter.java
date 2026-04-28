package com.tuempresa.investtrack.feature.portfolio.favorites;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.tuempresa.investtrack.core.navigation.AppMediator;
import com.tuempresa.investtrack.core.navigation.HomeToDetailState;
import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.R;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private final AppMediator mediator;
    private final Context context;
    private WeakReference<FavoritesContract.View> view;
    private FavoritesContract.Model model;

    public FavoritesPresenter(Context context) {
        this(AppMediator.getInstance(), context);
    }

    public FavoritesPresenter(AppMediator mediator, Context context) {
        this.mediator = mediator;
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<FavoritesContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(FavoritesContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.favorites_guest_unavailable,
                    "Log in to use favorites."
            ));
            currentView().navigateToPreviousScreen();
            return;
        }

        refreshFavorites();
    }

    @Override
    public void onResumeCalled() {
        if (!model.isGuestMode()) {
            refreshFavorites();
        }
    }

    @Override
    public void onAssetClicked(String assetId) {
        mediator.setNextDetailScreenState(new HomeToDetailState(assetId));
        currentView().navigateToDetailScreen();
    }

    @Override
    public void onDestroyCalled() {
        if (view != null) {
            view.clear();
        }
    }

    private void refreshFavorites() {
        FavoritesViewModel viewModel = new FavoritesViewModel();
        for (Asset asset : model.getFavoriteAssets()) {
            viewModel.assets.add(buildAssetViewModel(asset));
        }
        viewModel.empty = viewModel.assets.isEmpty();
        currentView().onRefreshViewWithUpdatedData(viewModel);
    }

    private FavoriteAssetViewModel buildAssetViewModel(Asset asset) {
        FavoriteAssetViewModel viewModel = new FavoriteAssetViewModel();
        viewModel.assetId = asset.getId();
        viewModel.name = asset.getName();
        viewModel.ticker = asset.getTicker();
        viewModel.quantityText = formatQuantity(asset);
        viewModel.priceText = formatCurrency(asset.getCurrentPrice());
        viewModel.logoDrawableName = asset.getLogoDrawableName();
        return viewModel;
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        if (Math.abs(amount - Math.rint(amount)) > 0.005) {
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
        } else {
            formatter.setMinimumFractionDigits(0);
            formatter.setMaximumFractionDigits(0);
        }
        return formatter.format(amount);
    }

    private String formatQuantity(Asset asset) {
        if ("crypto".equalsIgnoreCase(asset.getType())) {
            DecimalFormat formatter = new DecimalFormat(
                    "0.####",
                    DecimalFormatSymbols.getInstance(Locale.US)
            );
            return formatter.format(asset.getQuantity()) + " " + asset.getTicker();
        }

        if (Math.abs(asset.getQuantity() - 1) < 0.0001) {
            return getString(R.string.quantity_one_share, "1 share");
        }
        return getString(
                R.string.quantity_shares_format,
                "%1$d shares",
                Math.round(asset.getQuantity())
        );
    }

    private String getString(int stringRes, String fallback, Object... args) {
        if (context == null) {
            return args.length == 0 ? fallback : String.format(Locale.US, fallback, args);
        }
        return context.getString(stringRes, args);
    }

    private FavoritesContract.View currentView() {
        FavoritesContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Favorites view is not attached.");
        }
        return currentView;
    }
}
