package InvestTrack.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.manage.ManageInvestmentActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;

import java.lang.ref.WeakReference;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final String KEY_ASSET_ID = "asset_id";

    private DetailContract.Presenter presenter;
    private ImageButton favoriteButton;
    private ImageView assetLogo;
    private TextView assetNameText;
    private TextView tickerTypeText;
    private TextView priceText;
    private TextView quantityText;
    private TextView valueText;
    private TextView profitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        favoriteButton = findViewById(R.id.btn_detail_favorite);
        assetLogo = findViewById(R.id.iv_detail_logo);
        assetNameText = findViewById(R.id.tv_detail_asset_name);
        tickerTypeText = findViewById(R.id.tv_detail_asset_meta);
        priceText = findViewById(R.id.tv_detail_asset_price);
        quantityText = findViewById(R.id.tv_detail_asset_quantity);
        valueText = findViewById(R.id.tv_detail_asset_value);
        profitText = findViewById(R.id.tv_detail_asset_profit);

        presenter = new DetailPresenter();
        injectPresenter(presenter);
        presenter.injectView(new WeakReference<>(this));
        presenter.injectModel(new DetailModel(this));

        findViewById(R.id.btn_detail_back).setOnClickListener(view -> presenter.onBackButtonPressed());
        favoriteButton.setOnClickListener(view -> toggleFavorite());
        findViewById(R.id.btn_manage_investment).setOnClickListener(view ->
                presenter.onManageInvestmentClicked());

        updateFavoriteIcon();
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
        assetLogo.setImageResource(resolveDrawable(viewModel.logoDrawableName));
        assetNameText.setText(viewModel.name);
        tickerTypeText.setText(viewModel.tickerTypeText);
        priceText.setText(viewModel.priceText);
        quantityText.setText(viewModel.quantityText);
        valueText.setText(viewModel.currentValueText);
        profitText.setText(viewModel.profitText);
        profitText.setTextColor(ContextCompat.getColor(
                this,
                viewModel.profitPositive ? R.color.colorPositive : R.color.colorNegative
        ));
        profitText.setBackgroundResource(
                viewModel.profitPositive ? R.drawable.bg_profit_badge : R.drawable.bg_loss_badge
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null && presenter.getCurrentAssetId() != null) {
            outState.putString(KEY_ASSET_ID, presenter.getCurrentAssetId());
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroyCalled();
        }
        super.onDestroy();
    }

    private void toggleFavorite() {
        boolean isFavorite = AppPreferences.toggleFavorite(this);
        updateFavoriteIcon();
        Toast.makeText(
                this,
                isFavorite ? R.string.favorite_added : R.string.favorite_removed,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void updateFavoriteIcon() {
        favoriteButton.setImageResource(R.drawable.ic_favorite_star);
    }

    private DetailState buildState(Bundle savedInstanceState) {
        DetailState state = new DetailState();
        if (savedInstanceState != null) {
            state.assetId = savedInstanceState.getString(KEY_ASSET_ID);
        }
        return state;
    }

    private int resolveDrawable(String drawableName) {
        int drawableId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        return drawableId == 0 ? R.drawable.logo_microsoft : drawableId;
    }
}
