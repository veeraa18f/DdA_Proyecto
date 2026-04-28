package com.tuempresa.investtrack.feature.portfolio.detail;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.tuempresa.investtrack.core.navigation.AppMediator;
import com.tuempresa.investtrack.core.navigation.DetailToManageState;
import com.tuempresa.investtrack.core.navigation.HomeToDetailState;
import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.R;

public class DetailPresenter implements DetailContract.Presenter {

    public static final String CHART_RANGE_DAY = "day";
    public static final String CHART_RANGE_WEEK = "week";
    public static final String CHART_RANGE_MONTH = "month";
    public static final String CHART_RANGE_YEAR = "year";

    private static final String DEFAULT_ASSET_ID = "msft";

    private final AppMediator mediator;
    private final Context context;
    private WeakReference<DetailContract.View> view;
    private DetailContract.Model model;
    private String currentAssetId;
    private String selectedChartRange = CHART_RANGE_MONTH;

    public DetailPresenter() {
        this(AppMediator.getInstance(), null);
    }

    public DetailPresenter(Context context) {
        this(AppMediator.getInstance(), context);
    }

    public DetailPresenter(AppMediator mediator) {
        this(mediator, null);
    }

    public DetailPresenter(AppMediator mediator, Context context) {
        this.mediator = mediator;
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<DetailContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(DetailContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled(DetailState state) {
        selectedChartRange = resolveChartRange(state);
        currentAssetId = resolveAssetId(state);
        refreshCurrentAsset();
    }

    @Override
    public void onManageInvestmentClicked() {
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.guest_demo_read_only,
                    "Guest mode is a fixed demo. Log in to edit investments."
            ));
            return;
        }

        mediator.setNextManageScreenState(new DetailToManageState(currentAssetId));
        currentView().navigateToManageScreen();
    }

    @Override
    public void onFavoriteClicked() {
        if (currentAssetId == null) {
            return;
        }
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.favorites_guest_unavailable,
                    "Log in to use favorites."
            ));
            return;
        }

        boolean favorite = model.toggleFavorite(currentAssetId);
        refreshFavoriteState();
        currentView().showError(getString(
                favorite ? R.string.favorite_added : R.string.favorite_removed,
                favorite ? "Added to favorites." : "Removed from favorites."
        ));
    }

    @Override
    public void onChartRangeSelected(String chartRange) {
        if (!isSupportedChartRange(chartRange)) {
            return;
        }
        selectedChartRange = chartRange;
        refreshCurrentAsset();
    }

    @Override
    public void onBackButtonPressed() {
        currentView().navigateToPreviousScreen();
    }

    @Override
    public void onDestroyCalled() {
        if (view != null) {
            view.clear();
        }
    }

    @Override
    public String getCurrentAssetId() {
        return currentAssetId;
    }

    @Override
    public String getSelectedChartRange() {
        return selectedChartRange;
    }

    private void refreshCurrentAsset() {
        Asset asset = model.getAssetById(currentAssetId);
        if (asset == null) {
            currentView().showError(getString(R.string.detail_error_asset_not_found, "Asset not found."));
            return;
        }

        currentView().onRefreshViewWithUpdatedData(buildViewModel(asset));
        refreshFavoriteState();
    }

    private void refreshFavoriteState() {
        currentView().onRefreshFavoriteWithUpdatedData(buildFavoriteViewModel());
    }

    private DetailFavoriteViewModel buildFavoriteViewModel() {
        boolean guestMode = model.isGuestMode();
        boolean favorite = !guestMode && model.isFavorite(currentAssetId);

        DetailFavoriteViewModel viewModel = new DetailFavoriteViewModel();
        viewModel.guestMode = guestMode;
        viewModel.favorite = favorite;
        if (guestMode) {
            viewModel.statusText = getString(
                    R.string.favorites_guest_unavailable,
                    "Log in to use favorites."
            );
            viewModel.contentDescription = getString(
                    R.string.cd_add_favorite,
                    "Add to favorites"
            );
            return viewModel;
        }

        viewModel.statusText = getString(
                favorite ? R.string.detail_favorite_in : R.string.detail_favorite_out,
                favorite ? "In favorites" : "Not in favorites"
        );
        viewModel.contentDescription = getString(
                favorite ? R.string.cd_remove_favorite : R.string.cd_add_favorite,
                favorite ? "Remove from favorites" : "Add to favorites"
        );
        return viewModel;
    }

    private String resolveAssetId(DetailState state) {
        if (state != null && hasText(state.assetId)) {
            return state.assetId;
        }

        HomeToDetailState nextState = mediator.getNextDetailScreenState();
        if (nextState != null && hasText(nextState.assetId)) {
            return nextState.assetId;
        }

        return DEFAULT_ASSET_ID;
    }

    private String resolveChartRange(DetailState state) {
        if (state != null && isSupportedChartRange(state.selectedChartRange)) {
            return state.selectedChartRange;
        }
        return CHART_RANGE_MONTH;
    }

    private DetailViewModel buildViewModel(Asset asset) {
        DetailViewModel viewModel = new DetailViewModel();
        viewModel.assetId = asset.getId();
        viewModel.name = asset.getName();
        viewModel.tickerTypeText = formatAssetType(asset) + " - " + asset.getTicker();
        viewModel.priceText = formatCurrency(asset.getCurrentPrice(), false);
        viewModel.quantityText = formatQuantity(asset);
        viewModel.currentValueText = formatCurrency(asset.getCurrentValue(), true);
        viewModel.profitText = formatSignedWholeCurrency(asset.getProfit());
        double profitPercent = calculateProfitPercent(asset);
        double chartPercent = calculateRangePercent(profitPercent, selectedChartRange);
        viewModel.profitPercentText = formatSignedPercent(profitPercent);
        viewModel.profitSummaryText = viewModel.profitText + " (" + viewModel.profitPercentText + ")";
        viewModel.selectedChartRange = selectedChartRange;
        viewModel.chartChangeText = formatSignedPercent(chartPercent);
        viewModel.chartDrawableName = getChartDrawableName(selectedChartRange);
        viewModel.logoDrawableName = asset.getLogoDrawableName();
        viewModel.profitPositive = asset.getProfit() >= 0;
        viewModel.chartPositive = chartPercent >= 0;
        return viewModel;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
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

    private double calculateProfitPercent(Asset asset) {
        double invested = asset.getAveragePrice() * asset.getQuantity();
        if (Math.abs(invested) < 0.0001) {
            return 0;
        }
        return asset.getProfit() / invested * 100;
    }

    private double calculateRangePercent(double profitPercent, String chartRange) {
        if (CHART_RANGE_DAY.equals(chartRange)) {
            return profitPercent * 0.08;
        }
        if (CHART_RANGE_WEEK.equals(chartRange)) {
            return profitPercent * 0.18;
        }
        if (CHART_RANGE_YEAR.equals(chartRange)) {
            return profitPercent;
        }
        return profitPercent * 0.42;
    }

    private String getChartDrawableName(String chartRange) {
        if (CHART_RANGE_DAY.equals(chartRange)) {
            return "ic_chart_line_day";
        }
        if (CHART_RANGE_WEEK.equals(chartRange)) {
            return "ic_chart_line_week";
        }
        if (CHART_RANGE_YEAR.equals(chartRange)) {
            return "ic_chart_line_year";
        }
        return "ic_chart_line_month";
    }

    private String formatSignedPercent(double value) {
        DecimalFormat formatter = new DecimalFormat(
                "0.#",
                DecimalFormatSymbols.getInstance(Locale.US)
        );
        String sign = value >= 0 ? "+" : "-";
        return sign + formatter.format(Math.abs(value)) + "%";
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
        if ("crypto".equalsIgnoreCase(asset.getType())) {
            return getString(R.string.asset_type_crypto, "Crypto");
        }
        return getString(R.string.asset_type_stock, "Stock");
    }

    private boolean isSupportedChartRange(String chartRange) {
        return CHART_RANGE_DAY.equals(chartRange)
                || CHART_RANGE_WEEK.equals(chartRange)
                || CHART_RANGE_MONTH.equals(chartRange)
                || CHART_RANGE_YEAR.equals(chartRange);
    }

    private String getString(int stringRes, String fallback, Object... args) {
        if (context == null) {
            return args.length == 0 ? fallback : String.format(Locale.US, fallback, args);
        }
        return context.getString(stringRes, args);
    }

    private DetailContract.View currentView() {
        DetailContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Detail view is not attached.");
        }
        return currentView;
    }
}
