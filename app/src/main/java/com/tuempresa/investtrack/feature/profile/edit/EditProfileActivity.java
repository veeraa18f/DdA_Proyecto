package com.tuempresa.investtrack.feature.profile.edit;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import java.lang.ref.WeakReference;

public class EditProfileActivity extends AppCompatActivity implements EditProfileContract.View {

    private EditText usernameInput;
    private EditText phoneInput;
    private EditText emailInput;
    private EditProfileContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        usernameInput = findViewById(R.id.input_edit_profile_username);
        phoneInput = findViewById(R.id.input_edit_profile_phone);
        emailInput = findViewById(R.id.input_edit_profile_email);

        EditProfileContract.Presenter editProfilePresenter = new EditProfilePresenter(this);
        editProfilePresenter.injectView(new WeakReference<>(this));
        editProfilePresenter.injectModel(new EditProfileModel(this));
        injectPresenter(editProfilePresenter);

        findViewById(R.id.btn_edit_profile_save).setOnClickListener(view ->
                presenter.onSaveProfileClicked(
                        usernameInput.getText().toString(),
                        phoneInput.getText().toString(),
                        emailInput.getText().toString()
                ));
        findViewById(R.id.btn_edit_profile_cancel).setOnClickListener(view ->
                presenter.onCancelButtonPressed());

        presenter.onCreateCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(EditProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(EditProfileViewModel viewModel) {
        usernameInput.setText(viewModel.username);
        phoneInput.setText(viewModel.phone);
        emailInput.setText(viewModel.email);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToPreviousScreen() {
        finish();
    }
}
