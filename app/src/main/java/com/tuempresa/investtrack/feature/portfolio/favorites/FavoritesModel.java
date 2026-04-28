package com.tuempresa.investtrack.feature.portfolio.favorites;

import android.content.Context;

import java.util.List;

import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class FavoritesModel implements FavoritesContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public FavoritesModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public boolean isGuestMode() {
        return AppPreferences.isGuestMode(context);
    }

    @Override
    public List<Asset> getFavoriteAssets() {
        return repository.getFavoriteAssets(AppPreferences.getActiveUserId(context));
    }
}
