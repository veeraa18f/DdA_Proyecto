package InvestTrack.register;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btn_register_back).setOnClickListener(view -> finish());
        findViewById(R.id.btn_register_submit).setOnClickListener(view -> {
            Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
