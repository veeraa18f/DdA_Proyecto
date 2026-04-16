package InvestTrack.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.manage.ManageInvestmentActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;
import InvestTrack.utils.PortfolioFormatter;
import InvestTrack.utils.PortfolioMetrics;

public class DetailActivity extends AppCompatActivity {

    private ImageButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        favoriteButton = findViewById(R.id.btn_detail_favorite);

        findViewById(R.id.btn_detail_back).setOnClickListener(view -> finish());
        favoriteButton.setOnClickListener(view -> toggleFavorite());
        findViewById(R.id.btn_manage_investment).setOnClickListener(view ->
                startActivity(new Intent(this, ManageInvestmentActivity.class)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_detail);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindPortfolioData();
        updateFavoriteIcon();
    }

    private void bindPortfolioData() {
        double currentPrice = AppPreferences.getCurrentPrice(this);
        double quantity = AppPreferences.getQuantity(this);
        double averagePrice = AppPreferences.getAveragePrice();
        PortfolioMetrics metrics = PortfolioMetrics.from(currentPrice, quantity, averagePrice);

        ((TextView) findViewById(R.id.tv_detail_header_ticker)).setText(getString(R.string.detail_ticker));
        ((TextView) findViewById(R.id.tv_detail_company_name)).setText(getString(R.string.detail_company_name_dynamic));
        ((TextView) findViewById(R.id.tv_detail_ticker)).setText(getString(R.string.detail_ticker));

        String price = PortfolioFormatter.formatCurrency(currentPrice);
        String change = PortfolioFormatter.formatSignedPercent(metrics.getReturnPercentage());
        String marketValue = PortfolioFormatter.formatCurrency(metrics.getMarketValue());
        String profit = PortfolioFormatter.formatSignedCurrency(metrics.getProfit());
        String average = PortfolioFormatter.formatCurrency(averagePrice) + " avg";

        ((TextView) findViewById(R.id.tv_detail_price)).setText(price);
        ((TextView) findViewById(R.id.tv_detail_change)).setText(change);
        ((TextView) findViewById(R.id.tv_detail_stat_price)).setText(price);
        ((TextView) findViewById(R.id.tv_detail_stat_change)).setText(change);
        ((TextView) findViewById(R.id.tv_detail_position_amount)).setText(PortfolioFormatter.formatQuantity(quantity));
        ((TextView) findViewById(R.id.tv_detail_position_avg_price)).setText(average);
        ((TextView) findViewById(R.id.tv_detail_position_value)).setText(marketValue + " market value");
        ((TextView) findViewById(R.id.tv_detail_position_value_card)).setText(marketValue);
        ((TextView) findViewById(R.id.tv_detail_profit_value)).setText(profit);
        ((TextView) findViewById(R.id.tv_detail_return_value)).setText(change);
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
        favoriteButton.setImageResource(
                AppPreferences.isFavorite(this) ? R.drawable.ic_star_filled : R.drawable.ic_star
        );
    }
}
