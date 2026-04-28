package com.tuempresa.investtrack.feature.auth.forgot;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import java.lang.ref.WeakReference;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordContract.View {

    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private ForgotPasswordContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.input_forgot_email);
        passwordInput = findViewById(R.id.input_forgot_password);
        confirmPasswordInput = findViewById(R.id.input_forgot_confirm_password);

        ForgotPasswordContract.Presenter forgotPasswordPresenter = new ForgotPasswordPresenter(this);
        forgotPasswordPresenter.injectView(new WeakReference<>(this));
        forgotPasswordPresenter.injectModel(new ForgotPasswordModel(this));
        injectPresenter(forgotPasswordPresenter);

        findViewById(R.id.btn_forgot_submit).setOnClickListener(view ->
                presenter.onResetPasswordClicked(
                        emailInput.getText().toString(),
                        passwordInput.getText().toString(),
                        confirmPasswordInput.getText().toString()
                ));
        findViewById(R.id.btn_forgot_back).setOnClickListener(view ->
                presenter.onBackButtonPressed());

        presenter.onCreateCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(ForgotPasswordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(ForgotPasswordViewModel viewModel) {
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
