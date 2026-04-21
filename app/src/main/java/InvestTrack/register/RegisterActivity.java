package InvestTrack.register;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import InvestTrack.data.InvestTrackRepository;
import InvestTrack.data.UserEntity;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private InvestTrackRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repository = InvestTrackRepository.getInstance(this);
        nameInput = findViewById(R.id.input_register_name);
        emailInput = findViewById(R.id.input_register_email);
        passwordInput = findViewById(R.id.input_register_password);

        findViewById(R.id.btn_register_back).setOnClickListener(view -> finish());
        findViewById(R.id.btn_register_submit).setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, R.string.profile_error_invalid_username, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, R.string.profile_error_invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, R.string.login_error_invalid_credentials, Toast.LENGTH_SHORT).show();
            return;
        }

        UserEntity user = repository.createUser(name, email, password);
        if (user == null) {
            Toast.makeText(this, R.string.register_error_duplicate_email, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
