package com.tuempresa.investtrack.feature.portfolio.manage;

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

import java.lang.ref.WeakReference;

import com.tuempresa.investtrack.core.navigation.AppMediator;
import com.tuempresa.investtrack.common.ui.BottomNavHelper;

public class ManageInvestmentActivity extends AppCompatActivity implements ManageInvestmentContract.View {

    private static final String KEY_ASSET_ID = "asset_id";
    private static final String KEY_PRICE_INPUT = "price_input";
    private static final String KEY_QUANTITY_INPUT = "quantity_input";

    private EditText priceInput;
    private EditText quantityInput;
    private ImageView assetLogo;
    private TextView assetNameText;
    private TextView assetMetaText;
    private TextView currentPriceText;
    private TextView quantityText;
    private TextView previewValueText;
    private TextView previewProfitText;
    private TextView previewReturnText;
    private ManageInvestmentContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_investment);

        priceInput = findViewById(R.id.input_manage_price);
        quantityInput = findViewById(R.id.input_manage_quantity);
        assetLogo = findViewById(R.id.iv_manage_logo);
        assetNameText = findViewById(R.id.tv_manage_asset_name);
        assetMetaText = findViewById(R.id.tv_manage_asset_meta);
        currentPriceText = findViewById(R.id.tv_manage_current_price);
        quantityText = findViewById(R.id.tv_manage_quantity);
        previewValueText = findViewById(R.id.tv_manage_preview_value);
        previewProfitText = findViewById(R.id.tv_manage_preview_profit);
        previewReturnText = findViewById(R.id.tv_manage_preview_return);

        ManageInvestmentContract.Presenter manageInvestmentPresenter =
                new ManageInvestmentPresenter(this);
        manageInvestmentPresenter.injectView(new WeakReference<>(this));
        manageInvestmentPresenter.injectModel(new ManageInvestmentModel(this));
        injectPresenter(manageInvestmentPresenter);

        setupPositionPreview();

        findViewById(R.id.btn_cancel_manage).setOnClickListener(view ->
                presenter.onCancelButtonPressed());
        findViewById(R.id.btn_update_investment).setOnClickListener(view ->
                presenter.onUpdatePositionClicked(
                        priceInput.getText().toString(),
                        quantityInput.getText().toString()
                ));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_manage);
        BottomNavHelper.setup(this, bottomNavigationView, R.id.menu_home);

        presenter.onCreateCalled(buildState(savedInstanceState));
        restoreInputState(savedInstanceState);
    }

    private void setupPositionPreview() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence value, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
                presenter.onPositionChanged(
                        priceInput.getText().toString(),
                        quantityInput.getText().toString()
                );
            }

            @Override
            public void afterTextChanged(Editable value) {
            }
        };
        priceInput.addTextChangedListener(watcher);
        quantityInput.addTextChangedListener(watcher);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyCalled();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter.getCurrentAssetId() != null) {
            outState.putString(KEY_ASSET_ID, presenter.getCurrentAssetId());
        }
        outState.putString(KEY_PRICE_INPUT, priceInput.getText().toString());
        outState.putString(KEY_QUANTITY_INPUT, quantityInput.getText().toString());
    }

    @Override
    public void injectPresenter(ManageInvestmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshViewWithUpdatedData(ManageInvestmentViewModel viewModel) {
        assetLogo.setImageResource(resolveDrawable(viewModel.logoDrawableName));
        assetNameText.setText(viewModel.name);
        assetMetaText.setText(viewModel.metaText);
        currentPriceText.setText(viewModel.currentPriceText);
        quantityText.setText(viewModel.quantityText);
        priceInput.setText(viewModel.priceInputText);
        quantityInput.setText(viewModel.quantityInputText);
        onRefreshPreviewWithUpdatedData(viewModel);
    }

    @Override
    public void onRefreshPreviewWithUpdatedData(ManageInvestmentViewModel viewModel) {
        int resultColor = ContextCompat.getColor(
                this,
                viewModel.previewPositive ? R.color.colorPositive : R.color.colorNegative
        );
        previewValueText.setText(viewModel.previewValueText);
        previewProfitText.setText(viewModel.previewProfitText);
        previewReturnText.setText(viewModel.previewReturnText);
        previewProfitText.setTextColor(resultColor);
        previewReturnText.setTextColor(resultColor);
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

    private int resolveDrawable(String drawableName) {
        int drawableId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        return drawableId == 0 ? R.drawable.ic_chart_line : drawableId;
    }

    private com.tuempresa.investtrack.core.navigation.DetailToManageState buildState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String assetId = savedInstanceState.getString(KEY_ASSET_ID);
            if (assetId != null && !assetId.trim().isEmpty()) {
                return new com.tuempresa.investtrack.core.navigation.DetailToManageState(assetId);
            }
        }
        return AppMediator.getInstance().getNextManageScreenState();
    }

    private void restoreInputState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        String restoredPrice = savedInstanceState.getString(KEY_PRICE_INPUT);
        String restoredQuantity = savedInstanceState.getString(KEY_QUANTITY_INPUT);
        if (restoredPrice != null) {
            priceInput.setText(restoredPrice);
        }
        if (restoredQuantity != null) {
            quantityInput.setText(restoredQuantity);
        }
    }
}
