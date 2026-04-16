package InvestTrack.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.login.LoginActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.row_edit_profile).setOnClickListener(view -> showComingSoonMessage());
        findViewById(R.id.row_language).setOnClickListener(view -> showComingSoonMessage());
        findViewById(R.id.row_appearance).setOnClickListener(view -> showComingSoonMessage());
        findViewById(R.id.row_terms).setOnClickListener(view -> showComingSoonMessage());
        findViewById(R.id.row_logout).setOnClickListener(view -> logout());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_profile);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindProfile();
    }

    private void bindProfile() {
        TextView profileName = findViewById(R.id.tv_profile_name);
        TextView profileIdentifier = findViewById(R.id.tv_profile_identifier);

        if (AppPreferences.isGuestMode(this)) {
            profileName.setText(R.string.profile_guest_name);
            profileIdentifier.setText(R.string.profile_guest_id);
            return;
        }

        profileName.setText(AppPreferences.getUserName(this));
        profileIdentifier.setText(AppPreferences.getUserEmail(this));
    }

    private void showComingSoonMessage() {
        Toast.makeText(this, R.string.profile_feature_soon, Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        AppPreferences.logout(this);
        Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
