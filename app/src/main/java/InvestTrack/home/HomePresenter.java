package InvestTrack.home;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import InvestTrack.app.AppMediator;
import InvestTrack.app.HomeToDetailState;
import InvestTrack.data.Asset;
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
        if (model == null) {
            currentView().showError(getString(R.string.home_error_data_not_ready, "Home data is not ready."));
            return;
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
        if (FILTER_STOCK.equals(filter) || FILTER_CRYPTO.equals(filter)) {
            selectedFilter = filter;
        } else {
            selectedFilter = FILTER_ALL;
        }
        refreshView();
    }

    @Override
    public void onAssetClicked(String assetId) {
        HomeToDetailState state = new HomeToDetailState(assetId);
        mediator.setNextDetailScreenState(state);
        currentView().navigateToDetailScreen();
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

        viewModel.totalBalanceText = formatCurrency(totalValue, true);
        viewModel.totalChangeText = getString(
                R.string.home_total_profit_format,
                "%1$s total profit",
                formatSignedWholeCurrency(totalProfit)
        );
        viewModel.searchQuery = searchQuery;
        viewModel.selectedFilter = selectedFilter;
        return viewModel;
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
