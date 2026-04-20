package InvestTrack.manage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

public class ManageInvestmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_investment);

        findViewById(R.id.btn_cancel_manage).setOnClickListener(view -> finish());
        findViewById(R.id.btn_update_investment).setOnClickListener(view -> {
            Toast.makeText(this, R.string.investment_updated, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
