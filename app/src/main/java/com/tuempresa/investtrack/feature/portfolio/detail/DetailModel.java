package com.tuempresa.investtrack.feature.portfolio.detail;

import android.content.Context;

import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class DetailModel implements DetailContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public DetailModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public Asset getAssetById(String assetId) {
        return repository.getAssetById(AppPreferences.getActiveUserId(context), assetId);
    }

    @Override
    public boolean isGuestMode() {
        return AppPreferences.isGuestMode(context);
    }

    @Override
    public boolean isFavorite(String assetId) {
        return repository.isFavorite(AppPreferences.getActiveUserId(context), assetId);
    }

    @Override
    public boolean toggleFavorite(String assetId) {
        return repository.toggleFavorite(AppPreferences.getActiveUserId(context), assetId);
    }
}
