package com.tuempresa.investtrack.feature.portfolio.home;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tuempresa.investtrack.core.navigation.AppMediator;
import com.tuempresa.investtrack.core.navigation.HomeToDetailState;
import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.common.formatting.NumberParser;
import com.tuempresa.investtrack.R;

public class HomePresenter implements HomeContract.Presenter {

    public static final String FILTER_ALL = "All";
    public static final String FILTER_STOCK = "Stock";
    public static final String FILTER_CRYPTO = "Crypto";

    private final AppMediator mediator;
    private final Context context;
    private WeakReference<HomeContract.View> view;
    private HomeContract.Model model;
    private List<Asset> allAssets = new ArrayList<>();
    private List<Asset> availableCatalogAssets = new ArrayList<>();
    private String searchQuery = "";
    private String selectedFilter = FILTER_ALL;

    public HomePresenter() {
        this(AppMediator.getInstance(), null);
    }

    public HomePresenter(Context context) {
        this(AppMediator.getInstance(), context);
    }

    public HomePresenter(AppMediator mediator) {
        this(mediator, null);
    }

    public HomePresenter(AppMediator mediator, Context context) {
        this.mediator = mediator;
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<HomeContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(HomeContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        onCreateCalled(null);
    }

    @Override
    public void onCreateCalled(HomeState state) {
        if (model == null) {
            currentView().showError(getString(R.string.home_error_data_not_ready, "Home data is not ready."));
            return;
        }

        if (state != null) {
            searchQuery = state.searchQuery == null ? "" : state.searchQuery.trim();
            onFilterSelectedWithoutRefresh(state.selectedFilter);
        }

        allAssets = model.getAssets();
        refreshView();
    }

    @Override
    public void onSearchQueryChanged(String query) {
        searchQuery = query == null ? "" : query.trim();
        refreshView();
    }

    @Override
    public void onFilterSelected(String filter) {
        onFilterSelectedWithoutRefresh(filter);
        refreshView();
    }

    private void onFilterSelectedWithoutRefresh(String filter) {
        if (FILTER_STOCK.equals(filter) || FILTER_CRYPTO.equals(filter)) {
            selectedFilter = filter;
        } else {
            selectedFilter = FILTER_ALL;
        }
    }

    @Override
    public void onAssetClicked(String assetId) {
        HomeToDetailState state = new HomeToDetailState(assetId);
        mediator.setNextDetailScreenState(state);
        currentView().navigateToDetailScreen();
    }

    @Override
    public void onAddInvestmentClicked() {
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.guest_demo_read_only,
                    "Guest mode is a fixed demo. Log in to edit investments."
            ));
            return;
        }

        availableCatalogAssets = model.getAvailableCatalogAssets();
        currentView().showAddInvestmentOptions(buildAddOptions(availableCatalogAssets));
    }

    @Override
    public void onCatalogInvestmentSelected(String assetId) {
        Asset selectedAsset = findAssetById(availableCatalogAssets, assetId);
        if (selectedAsset == null) {
            currentView().showError(getString(
                    R.string.add_error_duplicate_asset,
                    "That ticker already exists."
            ));
            return;
        }

        currentView().showCatalogInvestmentQuantityInput(buildAssetViewModel(selectedAsset));
    }

    @Override
    public void onCatalogInvestmentQuantityConfirmed(String assetId, String quantity) {
        double parsedQuantity = NumberParser.parsePositiveDouble(quantity);
        if (parsedQuantity <= 0) {
            currentView().showError(getString(
                    R.string.manage_error_invalid_values,
                    "Enter a valid price and quantity."
            ));
            return;
        }

        if (!model.addCatalogAsset(assetId, parsedQuantity)) {
            currentView().showError(getString(
                    R.string.add_error_duplicate_asset,
                    "That ticker already exists."
            ));
            return;
        }

        allAssets = model.getAssets();
        currentView().showError(getString(R.string.add_success, "Investment added."));
        refreshView();
    }

    @Override
    public void onCustomInvestmentSelected() {
        currentView().navigateToAddInvestmentScreen();
    }

    @Override
    public void onRemoveInvestmentClicked() {
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.guest_demo_read_only,
                    "Guest mode is a fixed demo. Log in to edit investments."
            ));
            return;
        }

        if (allAssets.isEmpty()) {
            return;
        }

        currentView().showRemoveInvestmentOptions(buildRemoveOptions());
    }

    @Override
    public void onRemoveInvestmentSelected(String assetId) {
        if (!model.removeAsset(assetId)) {
            currentView().showError(getString(
                    R.string.remove_error_failed,
                    "Could not remove that investment."
            ));
            return;
        }

        allAssets = model.getAssets();
        currentView().showError(getString(R.string.remove_success, "Investment removed."));
        refreshView();
    }

    @Override
    public void onDestroyCalled() {
        if (view != null) {
            view.clear();
        }
    }

    private void refreshView() {
        currentView().onRefreshViewWithUpdatedData(buildViewModel(filterAssets()));
    }

    private List<Asset> filterAssets() {
        List<Asset> filteredAssets = new ArrayList<>();
        String normalizedQuery = searchQuery.toLowerCase(Locale.US);

        for (Asset asset : allAssets) {
            if (!matchesFilter(asset) || !matchesSearch(asset, normalizedQuery)) {
                continue;
            }
            filteredAssets.add(asset);
        }

        return filteredAssets;
    }

    private boolean matchesFilter(Asset asset) {
        return FILTER_ALL.equals(selectedFilter)
                || selectedFilter.equalsIgnoreCase(asset.getType());
    }

    private boolean matchesSearch(Asset asset, String normalizedQuery) {
        if (normalizedQuery.isEmpty()) {
            return true;
        }

        return asset.getName().toLowerCase(Locale.US).contains(normalizedQuery)
                || asset.getTicker().toLowerCase(Locale.US).contains(normalizedQuery)
                || asset.getType().toLowerCase(Locale.US).contains(normalizedQuery);
    }

    private HomeViewModel buildViewModel(List<Asset> visibleAssets) {
        HomeViewModel viewModel = new HomeViewModel();
        double totalValue = 0;
        double totalProfit = 0;

        for (Asset asset : allAssets) {
            totalValue += asset.getCurrentValue();
            totalProfit += asset.getProfit();
        }

        for (Asset asset : visibleAssets) {
            viewModel.assets.add(buildAssetViewModel(asset));
        }

        if (!model.isGuestMode()) {
            for (Asset asset : allAssets) {
                if (model.isFavorite(asset.getId())) {
                    viewModel.favoriteAssets.add(buildAssetViewModel(asset));
                }
            }
        }

        viewModel.totalBalanceText = formatCurrency(totalValue, true);
        viewModel.totalChangeText = getString(
                R.string.home_total_profit_format,
                "%1$s total profit",
                formatSignedWholeCurrency(totalProfit)
        );
        viewModel.searchQuery = searchQuery;
        viewModel.selectedFilter = selectedFilter;
        viewModel.canRemoveInvestment = !model.isGuestMode() && !allAssets.isEmpty();
        return viewModel;
    }

    private List<HomeAssetViewModel> buildRemoveOptions() {
        List<HomeAssetViewModel> options = new ArrayList<>();
        for (Asset asset : allAssets) {
            options.add(buildAssetViewModel(asset));
        }
        return options;
    }

    private List<HomeAssetViewModel> buildAddOptions(List<Asset> assets) {
        List<HomeAssetViewModel> options = new ArrayList<>();
        for (Asset asset : assets) {
            options.add(buildAssetViewModel(asset));
        }
        return options;
    }

    private HomeAssetViewModel buildAssetViewModel(Asset asset) {
        HomeAssetViewModel viewModel = new HomeAssetViewModel();
        viewModel.assetId = asset.getId();
        viewModel.name = asset.getName();
        viewModel.ticker = asset.getTicker();
        viewModel.type = formatAssetType(asset);
        viewModel.priceText = formatCurrency(asset.getCurrentPrice(), false);
        viewModel.quantityText = formatQuantity(asset);
        viewModel.profitText = formatSignedWholeCurrency(asset.getProfit());
        viewModel.logoDrawableName = asset.getLogoDrawableName();
        viewModel.profitPositive = asset.getProfit() >= 0;
        return viewModel;
    }

    private Asset findAssetById(List<Asset> assets, String assetId) {
        for (Asset asset : assets) {
            if (asset.getId().equals(assetId)) {
                return asset;
            }
        }
        return null;
    }

    private String formatCurrency(double amount, boolean keepCents) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        if (keepCents || Math.abs(amount - Math.rint(amount)) > 0.005) {
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
        } else {
            formatter.setMinimumFractionDigits(0);
            formatter.setMaximumFractionDigits(0);
        }
        return formatter.format(amount);
    }

    private String formatSignedWholeCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);
        String sign = amount >= 0 ? "+" : "-";
        return sign + formatter.format(Math.abs(amount));
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

    private String formatAssetType(Asset asset) {
        if (FILTER_CRYPTO.equalsIgnoreCase(asset.getType())) {
            return getString(R.string.asset_type_crypto, "Crypto");
        }
        return getString(R.string.asset_type_stock, "Stock");
    }

    private String getString(int stringRes, String fallback, Object... args) {
        if (context == null) {
            return args.length == 0 ? fallback : String.format(Locale.US, fallback, args);
        }
        return context.getString(stringRes, args);
    }

    private HomeContract.View currentView() {
        HomeContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Home view is not attached.");
        }
        return currentView;
    }
}
