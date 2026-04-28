package com.tuempresa.investtrack.feature.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;
import com.tuempresa.investtrack.feature.auth.login.LoginActivity;
import com.tuempresa.investtrack.feature.profile.edit.EditProfileActivity;
import com.tuempresa.investtrack.common.ui.BottomNavHelper;

import java.lang.ref.WeakReference;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {

    private ImageView profilePhoto;
    private TextView languageValueText;
    private TextView profileNameText;
    private TextView profileEmailText;
    private TextView profilePhoneText;
    private SwitchCompat darkModeSwitch;
    private ProfileContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePhoto = findViewById(R.id.iv_profile_photo);
        languageValueText = findViewById(R.id.tv_profile_language_value);
        profileNameText = findViewById(R.id.tv_profile_name);
        profileEmailText = findViewById(R.id.tv_profile_email);
        profilePhoneText = findViewById(R.id.tv_profile_phone);
        darkModeSwitch = findViewById(R.id.switch_profile_dark_mode);

        ProfileContract.Presenter profilePresenter = new ProfilePresenter(this);
        profilePresenter.injectView(new WeakReference<>(this));
        profilePresenter.injectModel(new ProfileModel(this));
        injectPresenter(profilePresenter);

        profilePhoto.setOnClickListener(view -> presenter.onProfilePhotoClicked());
        findViewById(R.id.btn_profile_change_photo).setOnClickListener(view ->
                presenter.onProfilePhotoClicked());
        findViewById(R.id.btn_profile_edit).setOnClickListener(view ->
                presenter.onEditProfileClicked());
        findViewById(R.id.row_profile_language).setOnClickListener(view ->
                presenter.onLanguageClicked());

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                presenter.onDarkModeChanged(isChecked));

        findViewById(R.id.btn_profile_logout).setOnClickListener(view ->
                presenter.onLogoutClicked());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_profile);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_profile);

        presenter.onCreateCalled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResumeCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(ProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(ProfileViewModel viewModel) {
        profilePhoto.setImageResource(viewModel.profilePhotoDrawable);
        languageValueText.setText(viewModel.languageText);
        profileNameText.setText(viewModel.username);
        profileEmailText.setText(viewModel.email);
        profilePhoneText.setText(viewModel.phone);
        darkModeSwitch.setChecked(viewModel.darkModeEnabled);
    }

    @Override
    public void showProfilePhotoOptions(int selectedOption) {
        String[] options = getResources().getStringArray(R.array.profile_photo_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.profile_change_photo)
                .setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
                    presenter.onProfilePhotoSelected(which);
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void showLanguageOptions(int selectedOption) {
        String[] options = getResources().getStringArray(R.array.language_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.profile_option_language)
                .setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
                    dialog.dismiss();
                    presenter.onLanguageSelected(which);
                })
                .show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToEditProfileScreen() {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    @Override
    public void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void recreateScreen() {
        recreate();
    }
}
