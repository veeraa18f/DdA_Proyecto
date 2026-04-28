package com.tuempresa.investtrack.feature.portfolio.manage;

import android.content.Context;

import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class ManageInvestmentModel implements ManageInvestmentContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public ManageInvestmentModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public boolean isGuestMode() {
        return AppPreferences.isGuestMode(context);
    }

    @Override
    public Asset getAssetById(String assetId) {
        return repository.getAssetById(AppPreferences.getActiveUserId(context), assetId);
    }

    @Override
    public boolean updateAssetPosition(String assetId, double currentPrice, double quantity) {
        return repository.updateAssetPosition(
                AppPreferences.getActiveUserId(context),
                assetId,
                currentPrice,
                quantity
        );
    }
}
