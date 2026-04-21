package InvestTrack.manage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuempresa.investtrack.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import InvestTrack.app.AppMediator;
import InvestTrack.app.DetailToManageState;
import InvestTrack.data.Asset;
import InvestTrack.data.InvestTrackRepository;
import InvestTrack.utils.AppPreferences;
import InvestTrack.utils.BottomNavHelper;

public class ManageInvestmentActivity extends AppCompatActivity {

    private EditText priceInput;
    private ImageView assetLogo;
    private TextView assetNameText;
    private TextView assetMetaText;
    private TextView currentPriceText;
    private TextView quantityText;
    private TextView previewValueText;
    private TextView previewProfitText;
    private TextView previewReturnText;
    private InvestTrackRepository repository;
    private Asset currentAsset;
    private String assetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_investment);

        repository = InvestTrackRepository.getInstance(this);
        if (AppPreferences.isGuestMode(this)) {
            Toast.makeText(this, R.string.guest_demo_read_only, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        priceInput = findViewById(R.id.input_manage_price);
        assetLogo = findViewById(R.id.iv_manage_logo);
        assetNameText = findViewById(R.id.tv_manage_asset_name);
        assetMetaText = findViewById(R.id.tv_manage_asset_meta);
        currentPriceText = findViewById(R.id.tv_manage_current_price);
        quantityText = findViewById(R.id.tv_manage_quantity);
        previewValueText = findViewById(R.id.tv_manage_preview_value);
        previewProfitText = findViewById(R.id.tv_manage_preview_profit);
        previewReturnText = findViewById(R.id.tv_manage_preview_return);
        assetId = resolveAssetId();
        bindCurrentAsset();
        setupPricePreview();

        findViewById(R.id.btn_cancel_manage).setOnClickListener(view -> finish());
        findViewById(R.id.btn_update_investment).setOnClickListener(view -> updateInvestment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_manage);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);
    }

    private String resolveAssetId() {
        DetailToManageState state = AppMediator.getInstance().getNextManageScreenState();
        if (state == null || state.assetId == null || state.assetId.trim().isEmpty()) {
            return "msft";
        }
        return state.assetId;
    }

    private void bindCurrentAsset() {
        currentAsset = repository.getAssetById(AppPreferences.getActiveUserId(this), assetId);
        if (currentAsset == null) {
            Toast.makeText(this, R.string.detail_error_asset_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        assetLogo.setImageResource(resolveDrawable(currentAsset.getLogoDrawableName()));
        assetNameText.setText(currentAsset.getName());
        assetMetaText.setText(getString(
                R.string.manage_asset_meta_format,
                formatAssetType(currentAsset),
                currentAsset.getTicker()
        ));
        currentPriceText.setText(getString(
                R.string.manage_current_price_format,
                formatCurrency(currentAsset.getCurrentPrice())
        ));
        quantityText.setText(getString(
                R.string.manage_quantity_format,
                formatQuantity(currentAsset)
        ));
        priceInput.setText(String.format(Locale.US, "%.2f", currentAsset.getCurrentPrice()));
        updatePreview(currentAsset.getCurrentPrice());
    }

    private void setupPricePreview() {
        priceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence value, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
                double price = parsePositiveDouble(value == null ? "" : value.toString());
                if (price >= 0) {
                    updatePreview(price);
                }
            }

            @Override
            public void afterTextChanged(Editable value) {
            }
        });
    }

    private void updateInvestment() {
        double price = parsePositiveDouble(priceInput.getText().toString());

        if (price < 0 || currentAsset == null) {
            Toast.makeText(this, R.string.manage_error_invalid_values, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!repository.updateAssetPosition(
                AppPreferences.getActiveUserId(this),
                assetId,
                price,
                currentAsset.getQuantity()
        )) {
            Toast.makeText(this, R.string.detail_error_asset_not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, R.string.investment_updated, Toast.LENGTH_SHORT).show();
        finish();
    }

    private double parsePositiveDouble(String rawValue) {
        if (rawValue == null) {
            return -1;
        }
        try {
            return Double.parseDouble(rawValue.trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private void updatePreview(double price) {
        if (currentAsset == null) {
            return;
        }

        double quantity = currentAsset.getQuantity();
        double value = price * quantity;
        double profit = (price - currentAsset.getAveragePrice()) * quantity;
        double invested = currentAsset.getAveragePrice() * quantity;
        double returnPercent = Math.abs(invested) < 0.0001 ? 0 : profit / invested * 100;
        int resultColor = ContextCompat.getColor(
                this,
                profit >= 0 ? R.color.colorPositive : R.color.colorNegative
        );

        previewValueText.setText(formatCurrency(value));
        previewProfitText.setText(formatSignedCurrency(profit));
        previewReturnText.setText(formatSignedPercent(returnPercent));
        previewProfitText.setTextColor(resultColor);
        previewReturnText.setTextColor(resultColor);
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount);
    }

    private String formatSignedCurrency(double amount) {
        String sign = amount >= 0 ? "+" : "-";
        return sign + formatCurrency(Math.abs(amount));
    }

    private String formatSignedPercent(double amount) {
        DecimalFormat formatter = new DecimalFormat(
                "0.##",
                DecimalFormatSymbols.getInstance(new Locale("es", "ES"))
        );
        String sign = amount >= 0 ? "+" : "-";
        return sign + formatter.format(Math.abs(amount)) + "%";
    }

    private String formatQuantity(Asset asset) {
        DecimalFormat formatter = new DecimalFormat(
                "0.####",
                DecimalFormatSymbols.getInstance(new Locale("es", "ES"))
        );
        if ("crypto".equalsIgnoreCase(asset.getType())) {
            return formatter.format(asset.getQuantity()) + " " + asset.getTicker();
        }
        return formatter.format(asset.getQuantity());
    }

    private String formatAssetType(Asset asset) {
        if ("crypto".equalsIgnoreCase(asset.getType())) {
            return getString(R.string.asset_type_crypto);
        }
        return getString(R.string.asset_type_stock);
    }

    private int resolveDrawable(String drawableName) {
        int drawableId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        return drawableId == 0 ? R.drawable.ic_chart_line : drawableId;
    }
}
