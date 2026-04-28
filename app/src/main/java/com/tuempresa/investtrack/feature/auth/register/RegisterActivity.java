package com.tuempresa.investtrack.feature.auth.register;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import java.lang.ref.WeakReference;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private RegisterContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.input_register_name);
        emailInput = findViewById(R.id.input_register_email);
        passwordInput = findViewById(R.id.input_register_password);

        RegisterContract.Presenter registerPresenter = new RegisterPresenter(this);
        registerPresenter.injectView(new WeakReference<>(this));
        registerPresenter.injectModel(new RegisterModel(this));
        injectPresenter(registerPresenter);

        findViewById(R.id.btn_register_back).setOnClickListener(view ->
                presenter.onBackButtonPressed());
        findViewById(R.id.btn_register_submit).setOnClickListener(view ->
                presenter.onRegisterClicked(
                        nameInput.getText().toString(),
                        emailInput.getText().toString(),
                        passwordInput.getText().toString()
                ));

        presenter.onCreateCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(RegisterViewModel viewModel) {
        nameInput.setText(viewModel.name);
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
