package com.tuempresa.investtrack.feature.portfolio.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import com.tuempresa.investtrack.feature.portfolio.detail.DetailActivity;
import com.tuempresa.investtrack.feature.portfolio.add.AddInvestmentActivity;
import com.tuempresa.investtrack.common.ui.BottomNavHelper;

import java.lang.ref.WeakReference;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_SELECTED_FILTER = "selected_filter";

    private HomeContract.Presenter presenter;
    private LayoutInflater inflater;
    private LinearLayout assetContainer;
    private TextView balanceText;
    private TextView balanceChangeText;
    private EditText searchInput;
    private Button removeInvestmentButton;
    private TextView filterAllButton;
    private TextView filterStockButton;
    private TextView filterCryptoButton;
    private TextView emptyAssetsText;
    private LinearLayout favoriteContainer;
    private TextView emptyFavoritesText;
    private String currentSelectedFilter = HomePresenter.FILTER_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inflater = LayoutInflater.from(this);
        assetContainer = findViewById(R.id.container_home_assets);
        balanceText = findViewById(R.id.tv_home_balance_value);
        balanceChangeText = findViewById(R.id.tv_home_balance_change);
        searchInput = findViewById(R.id.input_home_search);
        removeInvestmentButton = findViewById(R.id.btn_home_remove_investment);
        filterAllButton = findViewById(R.id.filter_home_all);
        filterStockButton = findViewById(R.id.filter_home_stock);
        filterCryptoButton = findViewById(R.id.filter_home_crypto);
        emptyAssetsText = findViewById(R.id.tv_home_empty_assets);
        favoriteContainer = findViewById(R.id.container_home_favorites);
        emptyFavoritesText = findViewById(R.id.tv_home_empty_favorites);

        presenter = new HomePresenter(this);
        injectPresenter(presenter);
        presenter.injectView(new WeakReference<>(this));
        presenter.injectModel(new HomeModel(this));
        setupSearchAndFilters();
        findViewById(R.id.btn_home_add_investment).setOnClickListener(view ->
                presenter.onAddInvestmentClicked());
        removeInvestmentButton.setOnClickListener(view -> presenter.onRemoveInvestmentClicked());
        presenter.onCreateCalled(buildState(savedInstanceState));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_home);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onCreateCalled();
        }
    }

    @Override
    public void injectPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(HomeViewModel viewModel) {
        currentSelectedFilter = viewModel.selectedFilter;
        if (!searchInput.getText().toString().equals(viewModel.searchQuery)) {
            searchInput.setText(viewModel.searchQuery);
        }
        balanceText.setText(viewModel.totalBalanceText);
        balanceChangeText.setText(viewModel.totalChangeText);
        updateFilterControls(viewModel.selectedFilter);
        updateRemoveInvestmentButton(viewModel.canRemoveInvestment);
        renderAssets(viewModel);
        renderFavorites(viewModel);
    }

    @Override
    public void navigateToDetailScreen() {
        startActivity(new Intent(this, DetailActivity.class));
    }

    @Override
    public void navigateToAddInvestmentScreen() {
        startActivity(new Intent(this, AddInvestmentActivity.class));
    }

    @Override
    public void showAddInvestmentOptions(List<HomeAssetViewModel> assets) {
        String[] labels = new String[assets.size() + 1];
        for (int index = 0; index < assets.size(); index++) {
            HomeAssetViewModel asset = assets.get(index);
            labels[index] = asset.name + " - " + asset.ticker;
        }
        labels[assets.size()] = getString(R.string.add_custom_option);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_select_title)
                .setItems(labels, (dialog, which) -> {
                    if (which == assets.size()) {
                        presenter.onCustomInvestmentSelected();
                        return;
                    }
                    presenter.onCatalogInvestmentSelected(assets.get(which).assetId);
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    @Override
    public void showCatalogInvestmentQuantityInput(HomeAssetViewModel asset) {
        EditText quantityInput = new EditText(this);
        quantityInput.setId(R.id.input_add_catalog_quantity);
        quantityInput.setHint(R.string.add_quantity_hint);
        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        quantityInput.setSingleLine(true);
        quantityInput.setSelectAllOnFocus(true);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_quantity_title)
                .setMessage(asset.name + " - " + asset.ticker)
                .setView(quantityInput)
                .setPositiveButton(R.string.action_add_investment, (dialog, which) ->
                        presenter.onCatalogInvestmentQuantityConfirmed(
                                asset.assetId,
                                quantityInput.getText().toString()
                        ))
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    @Override
    public void showRemoveInvestmentOptions(List<HomeAssetViewModel> assets) {
        String[] labels = new String[assets.size()];
        for (int index = 0; index < assets.size(); index++) {
            HomeAssetViewModel asset = assets.get(index);
            labels[index] = asset.name + " - " + asset.ticker;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_select_title)
                .setItems(labels, (dialog, which) ->
                        presenter.onRemoveInvestmentSelected(assets.get(which).assetId))
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroyCalled();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_QUERY, searchInput.getText().toString());
        outState.putString(KEY_SELECTED_FILTER, currentSelectedFilter);
    }

    private void renderAssets(HomeViewModel viewModel) {
        assetContainer.removeAllViews();
        emptyAssetsText.setVisibility(viewModel.assets.isEmpty() ? View.VISIBLE : View.GONE);

        for (int index = 0; index < viewModel.assets.size(); index++) {
            HomeAssetViewModel asset = viewModel.assets.get(index);
            View card = inflater.inflate(R.layout.item_investment, assetContainer, false);
            bindInvestment(card, asset);
            card.setOnClickListener(view -> presenter.onAssetClicked(asset.assetId));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = getResources().getDimensionPixelSize(
                    index == 0 ? R.dimen.spacing_lg : R.dimen.spacing_md
            );
            assetContainer.addView(card, params);
        }
    }

    private void bindInvestment(View card, HomeAssetViewModel asset) {
        ((ImageView) card.findViewById(R.id.iv_investment_logo))
                .setImageResource(resolveDrawable(asset.logoDrawableName));
        ((TextView) card.findViewById(R.id.tv_investment_name)).setText(asset.name);
        ((TextView) card.findViewById(R.id.tv_investment_ticker)).setText(asset.ticker);
        ((TextView) card.findViewById(R.id.tv_investment_type)).setText(asset.type);
        ((TextView) card.findViewById(R.id.tv_investment_price)).setText(asset.priceText);
        ((TextView) card.findViewById(R.id.tv_investment_quantity)).setText(asset.quantityText);

        TextView profitText = card.findViewById(R.id.tv_investment_profit);
        profitText.setText(asset.profitText);
        profitText.setTextColor(ContextCompat.getColor(
                this,
                asset.profitPositive ? R.color.colorPositive : R.color.colorNegative
        ));
        profitText.setBackgroundResource(
                asset.profitPositive ? R.drawable.bg_profit_badge : R.drawable.bg_loss_badge
        );
    }

    private void renderFavorites(HomeViewModel viewModel) {
        favoriteContainer.removeAllViews();
        emptyFavoritesText.setVisibility(viewModel.favoriteAssets.isEmpty() ? View.VISIBLE : View.GONE);

        for (int index = 0; index < viewModel.favoriteAssets.size(); index++) {
            HomeAssetViewModel asset = viewModel.favoriteAssets.get(index);
            View card = inflater.inflate(R.layout.item_favorite, favoriteContainer, false);
            bindFavorite(card, asset);
            card.setOnClickListener(view -> presenter.onAssetClicked(asset.assetId));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = getResources().getDimensionPixelSize(
                    index == 0 ? R.dimen.spacing_lg : R.dimen.spacing_md
            );
            favoriteContainer.addView(card, params);
        }
    }

    private void bindFavorite(View card, HomeAssetViewModel asset) {
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

    private void setupSearchAndFilters() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence value, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
                presenter.onSearchQueryChanged(value.toString());
            }

            @Override
            public void afterTextChanged(Editable value) {
            }
        });

        filterAllButton.setOnClickListener(view -> presenter.onFilterSelected(HomePresenter.FILTER_ALL));
        filterStockButton.setOnClickListener(view -> presenter.onFilterSelected(HomePresenter.FILTER_STOCK));
        filterCryptoButton.setOnClickListener(view -> presenter.onFilterSelected(HomePresenter.FILTER_CRYPTO));
    }

    private void updateFilterControls(String selectedFilter) {
        updateFilterButton(filterAllButton, HomePresenter.FILTER_ALL.equals(selectedFilter));
        updateFilterButton(filterStockButton, HomePresenter.FILTER_STOCK.equals(selectedFilter));
        updateFilterButton(filterCryptoButton, HomePresenter.FILTER_CRYPTO.equals(selectedFilter));
    }

    private void updateFilterButton(TextView button, boolean selected) {
        button.setBackgroundResource(selected ? R.drawable.bg_chip_selected : R.drawable.bg_chip);
        button.setTextColor(ContextCompat.getColor(
                this,
                selected ? R.color.colorTextPrimary : R.color.colorTextSecondary
        ));
    }

    private void updateRemoveInvestmentButton(boolean enabled) {
        removeInvestmentButton.setEnabled(enabled);
        removeInvestmentButton.setBackgroundResource(
                enabled ? R.drawable.bg_button_secondary : R.drawable.bg_button_disabled
        );
        removeInvestmentButton.setTextColor(ContextCompat.getColor(
                this,
                enabled ? R.color.colorTextPrimary : R.color.colorTextSecondary
        ));
    }

    private HomeState buildState(Bundle savedInstanceState) {
        HomeState state = new HomeState();
        if (savedInstanceState != null) {
            state.searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            state.selectedFilter = savedInstanceState.getString(KEY_SELECTED_FILTER);
        }
        return state;
    }
}
