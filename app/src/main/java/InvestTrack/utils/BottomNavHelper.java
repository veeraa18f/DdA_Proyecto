package InvestTrack.utils;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.favorites.FavoritesActivity;
import InvestTrack.home.HomeActivity;
import InvestTrack.profile.ProfileActivity;

public final class BottomNavHelper {

    private BottomNavHelper() {
    }

    public static void setup(AppCompatActivity activity, BottomNavigationView bottomNavigationView, int currentItemId) {
        bottomNavigationView.setSelectedItemId(currentItemId);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int selectedItemId = item.getItemId();
            if (selectedItemId == currentItemId) {
                return true;
            }

            Intent intent = buildIntent(activity, selectedItemId);
            if (intent == null) {
                return false;
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivity(intent);
            activity.finish();
            return true;
        });
    }

    private static Intent buildIntent(AppCompatActivity activity, int selectedItemId) {
        if (selectedItemId == R.id.menu_home) {
            return new Intent(activity, HomeActivity.class);
        }

        if (selectedItemId == R.id.menu_favorites) {
            return new Intent(activity, FavoritesActivity.class);
        }

        if (selectedItemId == R.id.menu_profile) {
            return new Intent(activity, ProfileActivity.class);
        }

        return null;
    }
}
