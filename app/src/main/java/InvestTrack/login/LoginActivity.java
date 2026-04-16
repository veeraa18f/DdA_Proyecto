package InvestTrack.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import InvestTrack.home.HomeActivity;
import InvestTrack.register.RegisterActivity;
import InvestTrack.utils.AppPreferences;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppPreferences.hasActiveSession(this)) {
            openHome();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.et_login_email);
        EditText passwordInput = findViewById(R.id.et_login_password);

        findViewById(R.id.btn_login).setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!isValidCredentials(email, password)) {
                Toast.makeText(this, R.string.login_error_invalid_credentials, Toast.LENGTH_SHORT).show();
                return;
            }

            String displayName = AppPreferences.getUserName(this);
            if ("Alicia Torres".equals(displayName)) {
                displayName = buildNameFromEmail(email);
            }

            AppPreferences.saveUser(this, displayName, email);
            AppPreferences.setLoggedIn(this, true);

            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
            openHome();
        });

        findViewById(R.id.btn_open_register).setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.btn_guest_mode).setOnClickListener(view -> {
            AppPreferences.enableGuestMode(this);
            Toast.makeText(this, R.string.guest_mode_enabled, Toast.LENGTH_SHORT).show();
            openHome();
        });
    }

    private boolean isValidCredentials(String email, String password) {
        return email.contains("@") && password.length() >= 6;
    }

    private String buildNameFromEmail(String email) {
        String localPart = email.split("@")[0].replace('.', ' ').trim();
        if (localPart.isEmpty()) {
            return "Investor";
        }

        return Character.toUpperCase(localPart.charAt(0)) + localPart.substring(1);
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
