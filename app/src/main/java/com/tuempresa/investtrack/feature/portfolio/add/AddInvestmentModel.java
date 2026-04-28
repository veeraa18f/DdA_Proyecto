package com.tuempresa.investtrack.feature.portfolio.add;

import android.content.Context;

import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class AddInvestmentModel implements AddInvestmentContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public AddInvestmentModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public boolean isGuestMode() {
        return AppPreferences.isGuestMode(context);
    }

    @Override
    public boolean addAsset(
            String name,
            String ticker,
            String type,
            double currentPrice,
            double quantity,
            double averagePrice
    ) {
        return repository.addAsset(
                AppPreferences.getActiveUserId(context),
                name,
                ticker,
                type,
                currentPrice,
                quantity,
                averagePrice
        );
    }
}
