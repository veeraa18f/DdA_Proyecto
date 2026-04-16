package InvestTrack.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.detail.DetailActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;

public class FavoritesActivity extends AppCompatActivity {

    private View favoriteCard;
    private View duplicateCard;
    private TextView hintLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoriteCard = findViewById(R.id.card_favorite_primary);
        duplicateCard = findViewById(R.id.card_favorite_secondary);
        hintLabel = findViewById(R.id.tv_favorites_hint);

        favoriteCard.setOnClickListener(view -> startActivity(new Intent(this, DetailActivity.class)));
        duplicateCard.setOnClickListener(view -> startActivity(new Intent(this, DetailActivity.class)));
        duplicateCard.setVisibility(View.GONE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_favorites);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_favorites);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFavorites();
    }

    private void refreshFavorites() {
        boolean hasFavorites = AppPreferences.isFavorite(this);

        favoriteCard.setVisibility(hasFavorites ? View.VISIBLE : View.GONE);
        hintLabel.setText(hasFavorites ? R.string.favorites_available_message : R.string.favorites_empty_message);
    }
}
