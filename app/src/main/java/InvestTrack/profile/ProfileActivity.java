package InvestTrack.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.login.LoginActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;
import InvestTrack.utils.ThemeHelper;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SwitchCompat darkModeSwitch = findViewById(R.id.switch_profile_dark_mode);
        darkModeSwitch.setChecked(AppPreferences.isDarkModeEnabled(this));
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppPreferences.setDarkModeEnabled(this, isChecked);
            ThemeHelper.setDarkMode(isChecked);
        });

        findViewById(R.id.btn_profile_logout).setOnClickListener(view -> {
            AppPreferences.logout(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_profile);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_profile);
    }
}
