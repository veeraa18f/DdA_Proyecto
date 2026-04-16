package InvestTrack.manage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tuempresa.investtrack.R;

import java.util.Locale;

import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.PortfolioFormatter;
import InvestTrack.utils.PortfolioMetrics;

public class ManageInvestmentActivity extends AppCompatActivity {

    private EditText priceInput;
    private EditText quantityInput;
    private TextView currentValueView;
    private TextView currentProfitView;
    private TextView currentReturnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_investment);

        priceInput = findViewById(R.id.et_manage_price);
        quantityInput = findViewById(R.id.et_manage_quantity);
        currentValueView = findViewById(R.id.tv_manage_current_value);
        currentProfitView = findViewById(R.id.tv_manage_current_profit);
        currentReturnView = findViewById(R.id.tv_manage_current_return);

        priceInput.setText(String.format(Locale.US, "%.2f", AppPreferences.getCurrentPrice(this)));
        quantityInput.setText(String.format(Locale.US, "%.2f", AppPreferences.getQuantity(this)));

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        priceInput.addTextChangedListener(watcher);
        quantityInput.addTextChangedListener(watcher);

        findViewById(R.id.btn_manage_back).setOnClickListener(view -> finish());
        findViewById(R.id.btn_cancel_manage).setOnClickListener(view -> finish());
        findViewById(R.id.btn_update_investment).setOnClickListener(view -> saveInvestment());

        refreshPreview();
    }

    private void saveInvestment() {
        double price = parseInput(priceInput);
        double quantity = parseInput(quantityInput);
        if (price <= 0 || quantity <= 0) {
            Toast.makeText(this, R.string.manage_invalid_values, Toast.LENGTH_SHORT).show();
            return;
        }

        AppPreferences.saveInvestment(this, price, quantity);
        Toast.makeText(this, R.string.investment_updated, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void refreshPreview() {
        double price = parseInput(priceInput);
        double quantity = parseInput(quantityInput);
        PortfolioMetrics metrics = PortfolioMetrics.from(
                Math.max(price, 0),
                Math.max(quantity, 0),
                AppPreferences.getAveragePrice()
        );

        currentValueView.setText(PortfolioFormatter.formatCurrency(metrics.getMarketValue()));
        currentProfitView.setText(PortfolioFormatter.formatSignedCurrency(metrics.getProfit()));
        currentReturnView.setText(PortfolioFormatter.formatSignedPercent(metrics.getReturnPercentage()));

        int profitColor = ContextCompat.getColor(
                this,
                metrics.getProfit() >= 0 ? R.color.colorPositive : R.color.colorNegative
        );
        currentProfitView.setTextColor(profitColor);
        currentReturnView.setTextColor(profitColor);
    }

    private double parseInput(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            return 0;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}
