package com.tuempresa.investtrack.feature.portfolio.add;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import java.lang.ref.WeakReference;

public class AddInvestmentActivity extends AppCompatActivity implements AddInvestmentContract.View {

    private EditText nameInput;
    private EditText tickerInput;
    private EditText currentPriceInput;
    private EditText quantityInput;
    private EditText averagePriceInput;
    private RadioGroup typeGroup;
    private AddInvestmentContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investment);

        nameInput = findViewById(R.id.input_add_name);
        tickerInput = findViewById(R.id.input_add_ticker);
        currentPriceInput = findViewById(R.id.input_add_current_price);
        quantityInput = findViewById(R.id.input_add_quantity);
        averagePriceInput = findViewById(R.id.input_add_average_price);
        typeGroup = findViewById(R.id.group_add_type);

        AddInvestmentContract.Presenter addInvestmentPresenter = new AddInvestmentPresenter(this);
        addInvestmentPresenter.injectView(new WeakReference<>(this));
        addInvestmentPresenter.injectModel(new AddInvestmentModel(this));
        injectPresenter(addInvestmentPresenter);

        findViewById(R.id.btn_add_investment).setOnClickListener(view ->
                presenter.onAddInvestmentClicked(
                        nameInput.getText().toString(),
                        tickerInput.getText().toString(),
                        getSelectedType(),
                        currentPriceInput.getText().toString(),
                        quantityInput.getText().toString(),
                        averagePriceInput.getText().toString()
                ));
        findViewById(R.id.btn_cancel_add_investment).setOnClickListener(view ->
                presenter.onCancelButtonPressed());

        presenter.onCreateCalled();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    public void injectPresenter(AddInvestmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(AddInvestmentViewModel viewModel) {
        typeGroup.check("Crypto".equalsIgnoreCase(viewModel.selectedType)
                ? R.id.option_add_crypto
                : R.id.option_add_stock);
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

    private String getSelectedType() {
        return typeGroup.getCheckedRadioButtonId() == R.id.option_add_crypto
                ? "Crypto"
                : "Stock";
    }
}
