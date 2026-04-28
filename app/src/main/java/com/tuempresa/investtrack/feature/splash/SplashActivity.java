package com.tuempresa.investtrack.feature.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.tuempresa.investtrack.R;

import com.tuempresa.investtrack.feature.auth.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 2200;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable openLoginRunnable = this::openLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(openLoginRunnable, SPLASH_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openLoginRunnable);
        super.onDestroy();
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
