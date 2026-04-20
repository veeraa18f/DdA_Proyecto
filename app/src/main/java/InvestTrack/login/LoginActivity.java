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
        setContentView(R.layout.activity_login);
        setTitle(R.string.app_name);

        EditText emailInput = findViewById(R.id.login_email_input);
        EditText passwordInput = findViewById(R.id.login_password_input);

        findViewById(R.id.login_button).setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (!email.contains("@") || password.length() < 6) {
                Toast.makeText(this, R.string.login_error_invalid_credentials, Toast.LENGTH_SHORT).show();
                return;
            }
            AppPreferences.setLoggedIn(this, true);
            openHome();
        });

        findViewById(R.id.login_signup_button).setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.login_guest_button).setOnClickListener(view -> {
            AppPreferences.enableGuestMode(this);
            openHome();
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
