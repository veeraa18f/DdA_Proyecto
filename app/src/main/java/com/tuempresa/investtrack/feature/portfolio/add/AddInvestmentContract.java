package com.tuempresa.investtrack.feature.portfolio.add;

import java.lang.ref.WeakReference;

public interface AddInvestmentContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(AddInvestmentViewModel viewModel);

        void showError(String message);

        void showSuccess(String message);

        void navigateToPreviousScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onAddInvestmentClicked(
                String name,
                String ticker,
                String type,
                String currentPrice,
                String quantity,
                String averagePrice
        );

        void onCancelButtonPressed();

        void onDestroyCalled();
    }

    interface Model {
        boolean isGuestMode();

        boolean addAsset(
                String name,
                String ticker,
                String type,
                double currentPrice,
                double quantity,
                double averagePrice
        );
    }
}
