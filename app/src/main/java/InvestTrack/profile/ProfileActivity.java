package InvestTrack.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import InvestTrack.login.LoginActivity;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;
import InvestTrack.utils.LanguageHelper;
import InvestTrack.utils.ThemeHelper;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePhoto;
    private TextView languageValueText;
    private TextView profileNameText;
    private TextView profileEmailText;
    private TextView profilePhoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePhoto = findViewById(R.id.iv_profile_photo);
        languageValueText = findViewById(R.id.tv_profile_language_value);
        profileNameText = findViewById(R.id.tv_profile_name);
        profileEmailText = findViewById(R.id.tv_profile_email);
        profilePhoneText = findViewById(R.id.tv_profile_phone);
        updateProfilePhoto();
        updateLanguageValue();
        updateProfileData();

        profilePhoto.setOnClickListener(view -> showProfilePhotoDialog());
        findViewById(R.id.btn_profile_change_photo).setOnClickListener(view ->
                showProfilePhotoDialog());
        findViewById(R.id.btn_profile_edit).setOnClickListener(view ->
                startActivity(new Intent(this, EditProfileActivity.class)));
        findViewById(R.id.row_profile_language).setOnClickListener(view ->
                showLanguageDialog());

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

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileData();
    }

    private void showProfilePhotoDialog() {
        String[] options = getResources().getStringArray(R.array.profile_photo_options);
        int selectedOption = AppPreferences.getProfilePhotoIndex(this) - 1;

        new AlertDialog.Builder(this)
                .setTitle(R.string.profile_change_photo)
                .setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
                    AppPreferences.setProfilePhotoIndex(this, which + 1);
                    updateProfilePhoto();
                    dialog.dismiss();
                })
                .show();
    }

    private void showLanguageDialog() {
        String[] options = getResources().getStringArray(R.array.language_options);
        String currentLanguage = AppPreferences.getLanguageCode(this);
        int selectedOption = LanguageHelper.LANGUAGE_SPANISH.equals(currentLanguage) ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle(R.string.profile_option_language)
                .setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
                    String languageCode = which == 1
                            ? LanguageHelper.LANGUAGE_SPANISH
                            : LanguageHelper.LANGUAGE_ENGLISH;
                    AppPreferences.setLanguageCode(this, languageCode);
                    dialog.dismiss();
                    LanguageHelper.setLanguage(languageCode);
                    recreate();
                })
                .show();
    }

    private void updateProfilePhoto() {
        profilePhoto.setImageResource(getProfilePhotoDrawable(
                AppPreferences.getProfilePhotoIndex(this)
        ));
    }

    private void updateLanguageValue() {
        String currentLanguage = AppPreferences.getLanguageCode(this);
        languageValueText.setText(
                LanguageHelper.LANGUAGE_SPANISH.equals(currentLanguage)
                        ? R.string.language_spanish
                        : R.string.language_english
        );
    }

    private void updateProfileData() {
        profileNameText.setText(AppPreferences.getProfileUsername(this));
        profileEmailText.setText(AppPreferences.getProfileEmail(this));
        profilePhoneText.setText(AppPreferences.getProfilePhone(this));
    }

    private int getProfilePhotoDrawable(int profilePhotoIndex) {
        if (profilePhotoIndex == 2) {
            return R.drawable.profile_photo_2;
        }
        if (profilePhotoIndex == 3) {
            return R.drawable.profile_photo_3;
        }
        return R.drawable.profile_photo_1;
    }
}
