package InvestTrack.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.detail.DetailActivity;
import InvestTrack.favorites.FavoritesActivity;
import InvestTrack.utils.BottomNavHelper;

import java.lang.ref.WeakReference;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private HomeContract.Presenter presenter;
    private LayoutInflater inflater;
    private LinearLayout assetContainer;
    private TextView balanceText;
    private TextView balanceChangeText;
    private EditText searchInput;
    private TextView filterAllButton;
    private TextView filterStockButton;
    private TextView filterCryptoButton;
    private TextView emptyAssetsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inflater = LayoutInflater.from(this);
        assetContainer = findViewById(R.id.container_home_assets);
        balanceText = findViewById(R.id.tv_home_balance_value);
        balanceChangeText = findViewById(R.id.tv_home_balance_change);
        searchInput = findViewById(R.id.input_home_search);
        filterAllButton = findViewById(R.id.filter_home_all);
        filterStockButton = findViewById(R.id.filter_home_stock);
        filterCryptoButton = findViewById(R.id.filter_home_crypto);
        emptyAssetsText = findViewById(R.id.tv_home_empty_assets);

        presenter = new HomePresenter(this);
        injectPresenter(presenter);
        presenter.injectView(new WeakReference<>(this));
        presenter.injectModel(new HomeModel(this));
        setupSearchAndFilters();
        presenter.onCreateCalled();

        findViewById(R.id.card_home_favorite_preview).setOnClickListener(view ->
                startActivity(new Intent(this, FavoritesActivity.class)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_home);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);
    }

    @Override
    public void injectPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(HomeViewModel viewModel) {
        balanceText.setText(viewModel.totalBalanceText);
        balanceChangeText.setText(viewModel.totalChangeText);
        updateFilterControls(viewModel.selectedFilter);
        renderAssets(viewModel);
    }

    @Override
    public void navigateToDetailScreen() {
        startActivity(new Intent(this, DetailActivity.class));
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
}
