package com.tuempresa.investtrack.feature.portfolio.home;

import android.content.Context;

import java.util.List;

import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class HomeModel implements HomeContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public HomeModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public List<Asset> getAssets() {
        return repository.getAllAssets(AppPreferences.getActiveUserId(context));
    }

    @Override
    public List<Asset> getAvailableCatalogAssets() {
        return repository.getAvailableCatalogAssets(AppPreferences.getActiveUserId(context));
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
    public boolean addCatalogAsset(String assetId, double quantity) {
        return repository.addCatalogAsset(AppPreferences.getActiveUserId(context), assetId, quantity);
    }

    @Override
    public boolean removeAsset(String assetId) {
        return repository.removeAsset(AppPreferences.getActiveUserId(context), assetId);
    }
}
