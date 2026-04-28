package com.tuempresa.investtrack.feature.portfolio.manage;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.tuempresa.investtrack.core.navigation.AppMediator;
import com.tuempresa.investtrack.core.navigation.DetailToManageState;
import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.common.validation.InputValidator;
import com.tuempresa.investtrack.common.formatting.NumberParser;
import com.tuempresa.investtrack.R;

public class ManageInvestmentPresenter implements ManageInvestmentContract.Presenter {

    private static final String DEFAULT_ASSET_ID = "msft";

    private final AppMediator mediator;
    private final Context context;
    private final Locale displayLocale = new Locale("es", "ES");
    private WeakReference<ManageInvestmentContract.View> view;
    private ManageInvestmentContract.Model model;
    private Asset currentAsset;
    private String currentAssetId;

    public ManageInvestmentPresenter(Context context) {
        this(AppMediator.getInstance(), context);
    }

    public ManageInvestmentPresenter(AppMediator mediator, Context context) {
        this.mediator = mediator;
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<ManageInvestmentContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ManageInvestmentContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled(DetailToManageState state) {
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.guest_demo_read_only,
                    "Guest mode is a fixed demo. Log in to edit investments."
            ));
            currentView().navigateToPreviousScreen();
            return;
        }

        currentAssetId = resolveAssetId(state);
        currentAsset = model.getAssetById(currentAssetId);
        if (currentAsset == null) {
            currentView().showError(getString(
                    R.string.detail_error_asset_not_found,
                    "Asset not found."
            ));
            currentView().navigateToPreviousScreen();
            return;
        }

        currentView().onRefreshViewWithUpdatedData(buildViewModel(
                currentAsset.getCurrentPrice(),
                currentAsset.getQuantity()
        ));
    }

    @Override
    public void onPositionChanged(String price, String quantity) {
        if (currentAsset == null) {
            return;
        }

        double parsedPrice = NumberParser.parsePositiveDouble(price);
        double parsedQuantity = NumberParser.parsePositiveDouble(quantity);
        if (parsedPrice >= 0 && parsedQuantity >= 0) {
            currentView().onRefreshPreviewWithUpdatedData(buildViewModel(parsedPrice, parsedQuantity));
        }
    }

    @Override
    public void onUpdatePositionClicked(String price, String quantity) {
        double parsedPrice = NumberParser.parsePositiveDouble(price);
        double parsedQuantity = NumberParser.parsePositiveDouble(quantity);
        if (parsedPrice < 0 || parsedQuantity < 0 || currentAsset == null) {
            currentView().showError(getString(
                    R.string.manage_error_invalid_values,
                    "Enter a valid price and quantity."
            ));
            return;
        }

        if (!model.updateAssetPosition(
                currentAssetId,
                parsedPrice,
                parsedQuantity
        )) {
            currentView().showError(getString(
                    R.string.detail_error_asset_not_found,
                    "Asset not found."
            ));
            return;
        }

        currentView().showSuccess(getString(
                R.string.investment_updated,
                "Investment updated."
        ));
        currentView().navigateToPreviousScreen();
    }

    @Override
    public void onCancelButtonPressed() {
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

    private ManageInvestmentViewModel buildViewModel(double price, double quantity) {
        double value = price * quantity;
        double profit = (price - currentAsset.getAveragePrice()) * quantity;
        double invested = currentAsset.getAveragePrice() * quantity;
        double returnPercent = Math.abs(invested) < 0.0001 ? 0 : profit / invested * 100;

        ManageInvestmentViewModel viewModel = new ManageInvestmentViewModel();
        viewModel.assetId = currentAsset.getId();
        viewModel.name = currentAsset.getName();
        viewModel.metaText = getString(
                R.string.manage_asset_meta_format,
                "%1$s - %2$s",
                formatAssetType(currentAsset),
                currentAsset.getTicker()
        );
        viewModel.currentPriceText = getString(
                R.string.manage_current_price_format,
                "Current price: %1$s",
                formatCurrency(currentAsset.getCurrentPrice())
        );
        viewModel.quantityText = getString(
                R.string.manage_quantity_format,
                "Quantity: %1$s",
                formatQuantity(currentAsset)
        );
        viewModel.priceInputText = String.format(Locale.US, "%.2f", currentAsset.getCurrentPrice());
        viewModel.quantityInputText = formatQuantityValue(currentAsset.getQuantity());
        viewModel.previewValueText = formatCurrency(value);
        viewModel.previewProfitText = formatSignedCurrency(profit);
        viewModel.previewReturnText = formatSignedPercent(returnPercent);
        viewModel.logoDrawableName = currentAsset.getLogoDrawableName();
        viewModel.previewPositive = profit >= 0;
        return viewModel;
    }

    private String resolveAssetId(DetailToManageState state) {
        if (state != null && InputValidator.hasText(state.assetId)) {
            return state.assetId;
        }

        DetailToManageState nextState = mediator.getNextManageScreenState();
        if (nextState != null && InputValidator.hasText(nextState.assetId)) {
            return nextState.assetId;
        }

        return DEFAULT_ASSET_ID;
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(displayLocale);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount);
    }

    private String formatSignedCurrency(double amount) {
        String sign = amount >= 0 ? "+" : "-";
        return sign + formatCurrency(Math.abs(amount));
    }

    private String formatSignedPercent(double amount) {
        DecimalFormat formatter = new DecimalFormat(
                "0.##",
                DecimalFormatSymbols.getInstance(displayLocale)
        );
        String sign = amount >= 0 ? "+" : "-";
        return sign + formatter.format(Math.abs(amount)) + "%";
    }

    private String formatQuantity(Asset asset) {
        DecimalFormat formatter = new DecimalFormat(
                "0.####",
                DecimalFormatSymbols.getInstance(displayLocale)
        );
        if ("crypto".equalsIgnoreCase(asset.getType())) {
            return formatter.format(asset.getQuantity()) + " " + asset.getTicker();
        }
        return formatter.format(asset.getQuantity());
    }

    private String formatQuantityValue(double quantity) {
        DecimalFormat formatter = new DecimalFormat(
                "0.####",
                DecimalFormatSymbols.getInstance(Locale.US)
        );
        return formatter.format(quantity);
    }

    private String formatAssetType(Asset asset) {
        if ("crypto".equalsIgnoreCase(asset.getType())) {
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

    private ManageInvestmentContract.View currentView() {
        ManageInvestmentContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Manage investment view is not attached.");
        }
        return currentView;
    }
}
