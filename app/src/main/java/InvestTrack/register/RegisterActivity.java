package InvestTrack.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import InvestTrack.home.HomeActivity;
import InvestTrack.utils.AppPreferences;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText nameInput = findViewById(R.id.et_register_name);
        EditText emailInput = findViewById(R.id.et_register_email);
        EditText passwordInput = findViewById(R.id.et_register_password);
        EditText confirmPasswordInput = findViewById(R.id.et_register_confirm_password);

        findViewById(R.id.btn_register).setOnClickListener(view -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, R.string.register_error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@") || password.length() < 6 || !password.equals(confirmPassword)) {
                Toast.makeText(this, R.string.register_error_password_match, Toast.LENGTH_SHORT).show();
                return;
            }

            AppPreferences.saveUser(this, name, email);
            AppPreferences.setLoggedIn(this, true);

            Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
            openHome();
        });

        findViewById(R.id.btn_back_to_login).setOnClickListener(view -> finish());
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
