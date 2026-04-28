package com.tuempresa.investtrack.feature.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import com.tuempresa.investtrack.feature.auth.forgot.ForgotPasswordActivity;
import com.tuempresa.investtrack.feature.portfolio.home.HomeActivity;
import com.tuempresa.investtrack.feature.auth.register.RegisterActivity;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private EditText emailInput;
    private EditText passwordInput;
    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.app_name);

        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);

        LoginContract.Presenter loginPresenter = new LoginPresenter(this);
        loginPresenter.injectView(new WeakReference<>(this));
        loginPresenter.injectModel(new LoginModel(this));
        injectPresenter(loginPresenter);

        findViewById(R.id.login_button).setOnClickListener(view ->
                presenter.onLoginClicked(
                        emailInput.getText().toString(),
                        passwordInput.getText().toString()
                ));

        findViewById(R.id.login_signup_button).setOnClickListener(view ->
                presenter.onRegisterClicked());

        findViewById(R.id.login_forgot_password).setOnClickListener(view ->
                presenter.onForgotPasswordClicked());

        findViewById(R.id.login_guest_button).setOnClickListener(view ->
                presenter.onGuestModeClicked());

        presenter.onCreateCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(LoginViewModel viewModel) {
        emailInput.setText(viewModel.email);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToRegisterScreen() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void navigateToForgotPasswordScreen() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}
