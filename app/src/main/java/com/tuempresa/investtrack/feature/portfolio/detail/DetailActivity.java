package com.tuempresa.investtrack.feature.portfolio.detail;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import com.tuempresa.investtrack.feature.portfolio.manage.ManageInvestmentActivity;
import com.tuempresa.investtrack.common.ui.BottomNavHelper;

import java.lang.ref.WeakReference;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final String KEY_ASSET_ID = "asset_id";
    private static final String KEY_CHART_RANGE = "chart_range";

    private DetailContract.Presenter presenter;
    private ImageButton favoriteButton;
    private ImageView assetLogo;
    private ImageView chartImage;
    private TextView assetNameText;
    private TextView tickerTypeText;
    private TextView priceText;
    private TextView quantityText;
    private TextView valueText;
    private TextView profitText;
    private TextView statPriceText;
    private TextView statChangeText;
    private TextView chartChangeText;
    private TextView positionProfitText;
    private TextView favoriteStatusText;
    private TextView rangeDayText;
    private TextView rangeWeekText;
    private TextView rangeMonthText;
    private TextView rangeYearText;
    private String currentAssetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        favoriteButton = findViewById(R.id.btn_detail_favorite);
        assetLogo = findViewById(R.id.iv_detail_logo);
        chartImage = findViewById(R.id.iv_detail_chart);
        assetNameText = findViewById(R.id.tv_detail_asset_name);
        tickerTypeText = findViewById(R.id.tv_detail_asset_meta);
        priceText = findViewById(R.id.tv_detail_asset_price);
        quantityText = findViewById(R.id.tv_detail_asset_quantity);
        valueText = findViewById(R.id.tv_detail_asset_value);
        profitText = findViewById(R.id.tv_detail_asset_profit);
        statPriceText = findViewById(R.id.tv_detail_stat_price);
        statChangeText = findViewById(R.id.tv_detail_stat_change);
        chartChangeText = findViewById(R.id.tv_detail_chart_change);
        positionProfitText = findViewById(R.id.tv_detail_position_profit);
        favoriteStatusText = findViewById(R.id.tv_detail_favorite_status);
        rangeDayText = findViewById(R.id.btn_detail_range_day);
        rangeWeekText = findViewById(R.id.btn_detail_range_week);
        rangeMonthText = findViewById(R.id.btn_detail_range_month);
        rangeYearText = findViewById(R.id.btn_detail_range_year);

        presenter = new DetailPresenter(this);
        injectPresenter(presenter);
        presenter.injectView(new WeakReference<>(this));
        presenter.injectModel(new DetailModel(this));

        findViewById(R.id.btn_detail_back).setOnClickListener(view -> presenter.onBackButtonPressed());
        favoriteButton.setOnClickListener(view -> presenter.onFavoriteClicked());
        findViewById(R.id.btn_manage_investment).setOnClickListener(view ->
                presenter.onManageInvestmentClicked());
        rangeDayText.setOnClickListener(view ->
                presenter.onChartRangeSelected(DetailPresenter.CHART_RANGE_DAY));
        rangeWeekText.setOnClickListener(view ->
                presenter.onChartRangeSelected(DetailPresenter.CHART_RANGE_WEEK));
        rangeMonthText.setOnClickListener(view ->
                presenter.onChartRangeSelected(DetailPresenter.CHART_RANGE_MONTH));
        rangeYearText.setOnClickListener(view ->
                presenter.onChartRangeSelected(DetailPresenter.CHART_RANGE_YEAR));

        presenter.onCreateCalled(buildState(savedInstanceState));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_detail);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);
    }

    @Override
    public void injectPresenter(DetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(DetailViewModel viewModel) {
        currentAssetId = viewModel.assetId;
        assetLogo.setImageResource(resolveDrawable(viewModel.logoDrawableName));
        chartImage.setImageResource(resolveDrawable(
                viewModel.chartDrawableName,
                R.drawable.ic_chart_line_month
        ));
        assetNameText.setText(viewModel.name);
        tickerTypeText.setText(viewModel.tickerTypeText);
        priceText.setText(viewModel.priceText);
        quantityText.setText(viewModel.quantityText);
        valueText.setText(viewModel.currentValueText);
        profitText.setText(viewModel.profitSummaryText);
        statPriceText.setText(viewModel.priceText);
        statChangeText.setText(viewModel.chartChangeText);
        chartChangeText.setText(viewModel.chartChangeText);
        positionProfitText.setText(viewModel.profitSummaryText);
        int profitColor = ContextCompat.getColor(
                this,
                viewModel.profitPositive ? R.color.colorPositive : R.color.colorNegative
        );
        int chartColor = ContextCompat.getColor(
                this,
                viewModel.chartPositive ? R.color.colorPositive : R.color.colorNegative
        );
        profitText.setTextColor(profitColor);
        statChangeText.setTextColor(chartColor);
        chartChangeText.setTextColor(chartColor);
        positionProfitText.setTextColor(profitColor);
        profitText.setBackgroundResource(
                viewModel.profitPositive ? R.drawable.bg_profit_badge : R.drawable.bg_loss_badge
        );
        chartChangeText.setBackgroundResource(
                viewModel.chartPositive ? R.drawable.bg_profit_badge : R.drawable.bg_loss_badge
        );
        updateSelectedChartRange(viewModel.selectedChartRange);
    }

    @Override
    public void onRefreshFavoriteWithUpdatedData(DetailFavoriteViewModel viewModel) {
        favoriteButton.setImageResource(
                viewModel.favorite ? R.drawable.ic_favorite_star : R.drawable.ic_favorite_outline
        );
        favoriteButton.setContentDescription(viewModel.contentDescription);
        favoriteStatusText.setText(viewModel.statusText);
        favoriteStatusText.setTextColor(ContextCompat.getColor(
                this,
                viewModel.favorite ? R.color.colorPositive : R.color.colorTextSecondary
        ));
        favoriteStatusText.setBackgroundResource(
                viewModel.favorite ? R.drawable.bg_profit_badge : R.drawable.bg_chip
        );
    }

    @Override
    public void navigateToManageScreen() {
        startActivity(new Intent(this, ManageInvestmentActivity.class));
    }

    @Override
    public void navigateToPreviousScreen() {
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null && currentAssetId != null) {
            DetailState state = new DetailState();
            state.assetId = currentAssetId;
            state.selectedChartRange = presenter.getSelectedChartRange();
            presenter.onCreateCalled(state);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null && presenter.getCurrentAssetId() != null) {
            outState.putString(KEY_ASSET_ID, presenter.getCurrentAssetId());
        }
        if (presenter != null && presenter.getSelectedChartRange() != null) {
            outState.putString(KEY_CHART_RANGE, presenter.getSelectedChartRange());
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroyCalled();
        }
        super.onDestroy();
    }

    private DetailState buildState(Bundle savedInstanceState) {
        DetailState state = new DetailState();
        if (savedInstanceState != null) {
            state.assetId = savedInstanceState.getString(KEY_ASSET_ID);
            state.selectedChartRange = savedInstanceState.getString(KEY_CHART_RANGE);
        }
        return state;
    }

    private int resolveDrawable(String drawableName) {
        return resolveDrawable(drawableName, R.drawable.logo_microsoft);
    }

    private int resolveDrawable(String drawableName, int fallbackDrawableId) {
        int drawableId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        return drawableId == 0 ? fallbackDrawableId : drawableId;
    }

    private void updateSelectedChartRange(String selectedRange) {
        updateChartRangeButton(
                rangeDayText,
                DetailPresenter.CHART_RANGE_DAY.equals(selectedRange)
        );
        updateChartRangeButton(
                rangeWeekText,
                DetailPresenter.CHART_RANGE_WEEK.equals(selectedRange)
        );
        updateChartRangeButton(
                rangeMonthText,
                DetailPresenter.CHART_RANGE_MONTH.equals(selectedRange)
        );
        updateChartRangeButton(
                rangeYearText,
                DetailPresenter.CHART_RANGE_YEAR.equals(selectedRange)
        );
    }

    private void updateChartRangeButton(TextView rangeText, boolean selected) {
        rangeText.setBackgroundResource(selected ? R.drawable.bg_chip_selected : 0);
        rangeText.setTextColor(ContextCompat.getColor(
                this,
                selected ? R.color.colorTextPrimary : R.color.colorTextSecondary
        ));
        rangeText.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
    }
}
