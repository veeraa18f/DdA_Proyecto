package InvestTrack.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.detail.DetailActivity;
import InvestTrack.favorites.FavoritesActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;
import InvestTrack.utils.PortfolioFormatter;
import InvestTrack.utils.PortfolioMetrics;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.card_home_primary_investment).setOnClickListener(view ->
                startActivity(new Intent(this, DetailActivity.class)));

        findViewById(R.id.card_home_secondary_investment).setOnClickListener(view ->
                startActivity(new Intent(this, DetailActivity.class)));

        findViewById(R.id.card_home_favorite_preview).setOnClickListener(view ->
                startActivity(new Intent(this, FavoritesActivity.class)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_home);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindSummary();
    }

    private void bindSummary() {
        double currentPrice = AppPreferences.getCurrentPrice(this);
        double quantity = AppPreferences.getQuantity(this);
        double averagePrice = AppPreferences.getAveragePrice();
        PortfolioMetrics metrics = PortfolioMetrics.from(currentPrice, quantity, averagePrice);

        String greetingName = AppPreferences.isGuestMode(this)
                ? getString(R.string.profile_guest_name)
                : AppPreferences.getGreetingName(this);

        TextView greeting = findViewById(R.id.tv_home_greeting);
        TextView balance = findViewById(R.id.tv_home_balance);
        TextView change = findViewById(R.id.tv_home_balance_change);

        greeting.setText(getString(R.string.home_greeting_format, greetingName));
        balance.setText(PortfolioFormatter.formatCurrency(metrics.getMarketValue()));
        change.setText(PortfolioFormatter.formatSignedPercent(metrics.getReturnPercentage()));
        change.setBackgroundResource(
                metrics.getReturnPercentage() >= 0 ? R.drawable.bg_profit_badge : R.drawable.bg_loss_badge
        );
    }
}
