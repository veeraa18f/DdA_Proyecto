package com.tuempresa.investtrack.feature.portfolio.add;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.tuempresa.investtrack.common.validation.InputValidator;
import com.tuempresa.investtrack.common.formatting.NumberParser;
import com.tuempresa.investtrack.R;

public class AddInvestmentPresenter implements AddInvestmentContract.Presenter {

    private final Context context;
    private WeakReference<AddInvestmentContract.View> view;
    private AddInvestmentContract.Model model;

    public AddInvestmentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void injectView(WeakReference<AddInvestmentContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(AddInvestmentContract.Model model) {
        this.model = model;
    }

    @Override
    public void onCreateCalled() {
        if (model.isGuestMode()) {
            currentView().showError(getString(
                    R.string.guest_demo_read_only,
                    "Guest mode is a fixed demo. Log in to edit investments."
            ));
            currentView().navigateToPreviousScreen();
            return;
        }

        currentView().onRefreshViewWithUpdatedData(new AddInvestmentViewModel());
    }

    @Override
    public void onAddInvestmentClicked(
            String name,
            String ticker,
            String type,
            String currentPrice,
            String quantity,
            String averagePrice
    ) {
        String normalizedName = InputValidator.normalize(name);
        String normalizedTicker = InputValidator.normalize(ticker);
        double parsedCurrentPrice = NumberParser.parsePositiveDouble(currentPrice);
        double parsedQuantity = NumberParser.parsePositiveDouble(quantity);
        double parsedAveragePrice = NumberParser.parsePositiveDouble(averagePrice);

        if (normalizedName.isEmpty() || normalizedTicker.isEmpty()) {
            currentView().showError(getString(
                    R.string.add_error_missing_text,
                    "Enter a name and ticker."
            ));
            return;
        }

        if (parsedCurrentPrice < 0 || parsedQuantity < 0 || parsedAveragePrice < 0) {
            currentView().showError(getString(
                    R.string.manage_error_invalid_values,
                    "Enter a valid price and quantity."
            ));
            return;
        }

        if (!model.addAsset(
                normalizedName,
                normalizedTicker,
                InputValidator.normalize(type),
                parsedCurrentPrice,
                parsedQuantity,
                parsedAveragePrice
        )) {
            currentView().showError(getString(
                    R.string.add_error_duplicate_asset,
                    "That ticker already exists."
            ));
            return;
        }

        currentView().showSuccess(getString(
                R.string.add_success,
                "Investment added."
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

    private String getString(int stringRes, String fallback, Object... args) {
        if (context == null) {
            return args.length == 0 ? fallback : String.format(Locale.US, fallback, args);
        }
        return context.getString(stringRes, args);
    }

    private AddInvestmentContract.View currentView() {
        AddInvestmentContract.View currentView = view == null ? null : view.get();
        if (currentView == null) {
            throw new IllegalStateException("Add investment view is not attached.");
        }
        return currentView;
    }
}
