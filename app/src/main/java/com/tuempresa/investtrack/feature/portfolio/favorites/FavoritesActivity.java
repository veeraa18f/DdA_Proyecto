package com.tuempresa.investtrack.feature.portfolio.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import java.lang.ref.WeakReference;

import com.tuempresa.investtrack.feature.portfolio.detail.DetailActivity;
import com.tuempresa.investtrack.common.ui.BottomNavHelper;

public class FavoritesActivity extends AppCompatActivity implements FavoritesContract.View {

    private LayoutInflater inflater;
    private LinearLayout favoriteContainer;
    private TextView emptyText;
    private FavoritesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        inflater = LayoutInflater.from(this);
        favoriteContainer = findViewById(R.id.container_favorites_assets);
        emptyText = findViewById(R.id.tv_favorites_empty);

        FavoritesContract.Presenter favoritesPresenter = new FavoritesPresenter(this);
        favoritesPresenter.injectView(new WeakReference<>(this));
        favoritesPresenter.injectModel(new FavoritesModel(this));
        injectPresenter(favoritesPresenter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_favorites);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_favorites);

        presenter.onCreateCalled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResumeCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(FavoritesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(FavoritesViewModel viewModel) {
        favoriteContainer.removeAllViews();

        for (int index = 0; index < viewModel.assets.size(); index++) {
            FavoriteAssetViewModel asset = viewModel.assets.get(index);
            View card = inflater.inflate(R.layout.item_favorite, favoriteContainer, false);
            bindFavorite(card, asset);
            card.setOnClickListener(view -> presenter.onAssetClicked(asset.assetId));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = getResources().getDimensionPixelSize(
                    index == 0 ? R.dimen.spacing_xxl : R.dimen.spacing_md
            );
            favoriteContainer.addView(card, params);
        }

        emptyText.setVisibility(viewModel.empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToDetailScreen() {
        startActivity(new Intent(this, DetailActivity.class));
    }

    @Override
    public void navigateToPreviousScreen() {
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void bindFavorite(View card, FavoriteAssetViewModel asset) {
        ((ImageView) card.findViewById(R.id.iv_favorite_logo))
                .setImageResource(resolveDrawable(asset.logoDrawableName));
        ((TextView) card.findViewById(R.id.tv_favorite_name)).setText(asset.name);
        ((TextView) card.findViewById(R.id.tv_favorite_ticker)).setText(asset.ticker);
        ((TextView) card.findViewById(R.id.tv_favorite_quantity)).setText(asset.quantityText);
        ((TextView) card.findViewById(R.id.tv_favorite_price)).setText(asset.priceText);
    }

    private int resolveDrawable(String drawableName) {
        int drawableId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        return drawableId == 0 ? R.drawable.logo_microsoft : drawableId;
    }
}
